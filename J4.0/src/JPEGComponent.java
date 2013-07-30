/* JPEGComponent.java --
   Copyright (C)  2005  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */



import java.util.ArrayList;
import java.io.IOException;
import java.awt.image.WritableRaster;


/**
 * This class holds the methods to decode and write a component information to
 * a raster.
 */

public class JPEGComponent
{   boolean verbose=false;

  public byte factorH, factorV, component_id, quant_id;
  public int width = 0, height = 0, maxV = 0, maxH = 0;
  public HuffmanTable ACTable;
  public HuffmanTable DCTable;
  public int[] quantizationTable;
  public short previousDC = 0;
  ArrayList data = new ArrayList();
private int mcu_height;
private int mcu_width;
public int mcu_rows;
public int mcu_cols;

public int factorUpHorizontal;

public int factorUpVertical;


  /**
   * Initializes the component
   * 
   * @param id
   * @param factorHorizontal
   * @param factorVertical
   * @param quantizationID
   */
  public JPEGComponent(byte id, byte factorHorizontal, byte factorVertical,
                       byte quantizationID, int height, int width)
  {
    component_id = id;
    factorH = factorHorizontal;
    factorV = factorVertical;
    quant_id = quantizationID;
    this.height=height;
    this.width=width;
  }

  /**
   * If a restart marker is found with too little of an MCU count (i.e. our
   * Restart Interval is 63 and we have 61 we copy the last MCU until it's
   * full)
   * 
   * @param index
   * @param length
   */
  public void padMCU(int index, int length)
  {
    short[] src = (short[]) data.get(index - 1);
    for (int i = 0; i < length; i++)
      data.add(index, src);
  }

  /**
   * Reset the interval by setting the previous DC value
   */
  public void resetInterval()
  {
    previousDC = 0;
  }

  /**
   * Run the Quantization backward method on all of the block data.
   */
  public void quantitizeData()
  {
    for (int i = 0; i < data.size(); i++)
      {
        short[] mydata = (short[]) data.get(i);
        for (int j = 0; j < mydata.length; j++)
          mydata[j] *= quantizationTable[j];
      }
  }
  public void quantitize2D_Data()
  {
	  short temp1[]=new short[64];
	  for(int i=0;i<quantizationTable.length;i++)
		  temp1[i]=(short) quantizationTable[i];
	  short [][] temp=ZigZag.decode8x8_map(temp1);
    for (int i = 0; i < data.size(); i++)
      {
        short[][] mydata = (short[][]) data.get(i);
        for (int j = 0; j < mydata.length; j++)
            for (int k = 0; k < mydata[0].length; k++)
            	          mydata[j][k] *= temp[j][k];
      }
  }
  public short[][] quantitize2D_Data(short[][]mydata)
  {
	  short temp1[]=new short[64];
	  for(int i=0;i<quantizationTable.length;i++)
		  temp1[i]=(short) quantizationTable[i];
	  short [][] temp=ZigZag.decode8x8_map(temp1);
   short [][] data=new short[8][8];
        for (int j = 0; j < mydata.length; j++)
            for (int k = 0; k < mydata[0].length; k++)
            	         data[j][k]= (short) (mydata[j][k] * temp[j][k]);
     return data; 
  }


  public void setDCTable(JPEGHuffmanTable table)
  {
    DCTable = new HuffmanTable(table);
  }
 public HuffmanTable getDCTable()
  {
    return DCTable;
  }
 public HuffmanTable getACTable()
  {
    return ACTable;
  }

  public void setACTable(JPEGHuffmanTable table)
  {
    ACTable = new HuffmanTable(table);
  }

  /**
   * Run the Inverse DCT method on all of the block data
   */
  public void idctData(DCT myDCT)
  {
    for (int i = 0; i < data.size(); i++)
      data.add(i,myDCT.fast_idct(ZigZag.decode8x8_map((short[]) data.remove(i))));
  }
  public void idct2D_Data(DCT myDCT)
  {
    for (int i = 0; i < data.size(); i++)
      data.add(i,myDCT.fast_idct((short[][])data.remove(i)));
  }
  public short[][] idct2D_Data(DCT myDCT, short[][]data)
  {
      return myDCT.fast_idct(data);
  }

  /**
   * This scales up the component size based on the factor size. This
   * calculates everyting up automatically so it's simply ran at the end of
   * the frame to normalize the size of all of the components.
 * @param subblock 
 * @param index 
   */
  public short[][] scaleByFactors(int subblock,short[][] data)
  {
     factorUpVertical = maxV / factorV;
     factorUpHorizontal = maxH / factorH;
    if(factorUpHorizontal==1 && factorUpVertical==1)
    	return data;
    ArrayList temp=new ArrayList();
    short[][] dest=null;
    if (factorUpVertical > 1)
      {
            short[][] src = data;
             dest =
              new short[src.length * factorUpVertical][src[0].length];
            for (int j = 0; j < src.length; j++)
              {
                for (int u = 0; u < factorUpVertical; u++)
                  {
                    dest[j * factorUpVertical + u] = src[j];
                  }
              }
            //temp.add(dest);
      }

    if (factorUpHorizontal > 1)
      {
          // short[][] src = (short[][]) temp.remove(0);
    	short[][] src=dest;
    	if(src==null) src=data;
    	dest =
              new short[src.length][src[0].length * factorUpHorizontal];
            for (int j = 0; j < src.length; j++)
              {
                for (int u = 0; u < src[0].length; u++)
                  {
                    for (int v = 0; v < factorUpHorizontal; v++)
                      dest[j][u * factorUpHorizontal + v] = src[j][u];
                  }
              }
            
      }
    
    int counter1 = 0;

	short temp2[][] = new short[8][8];
	int xindex = 0;
	int yindex = 0;
	for (yindex = 0; yindex < dest.length; yindex++) {
		for (xindex = 0; xindex < dest[0].length; xindex++) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//System.out.println(j+"   "+i+"  "+(j+yindex)+"    "+(i+xindex));
                     if (i + xindex < dest[0].length
                             && j + yindex < dest.length)
    						temp2[j][i] = dest[j + yindex][i + xindex];
				}
			}
			if((counter1++)==subblock){
				return temp2;
			}
			xindex += 7;
		}
		yindex += 7;
	}
  return temp2;
  }
  /**
   * This write the block of data to the raster throwing out anything that
   * spills over the raster width or height.
   * 
   * @param raster
   * @param data
   * @param compIndex
   * @param x
   * @param y
   */
  public void writeBlock(WritableRaster raster, short[][] data,
                         int compIndex, int x, int y)
  {
    for (int yIndex = 0; yIndex < data.length; yIndex++)
      {
        for (int xIndex = 0; xIndex < data[yIndex].length; xIndex++)
          {
            // The if statement is needed because blocks can spill over the
            // frame width because they are padded to make sure we keep the
            // height of the block the same as the width of the block
            if (x + xIndex < raster.getWidth()
                && y + yIndex < raster.getHeight())
              raster.setSample(x + xIndex, y + yIndex, compIndex,
                               data[yIndex][xIndex]);
          }
      }
  }

  /**
   * This writes data to a raster block, so really it's reading not writing
   * but it writes the data to the raster block by factor size in a zig zag
   * fashion. This has the helper function writeBlock which does the actual
   * writing.
   * 
   * @param raster
   * @param componentIndex
   */
  public void formatData(int componentIndex)
  {
    int x = 0, y = 0, lastblockheight = 0, incrementblock = 0;
    short [][] tempdata=new short[height][width];
    // Keep looping through all of the blocks until there are no more.
    while(data.size() > 0)
      {
        int blockwidth = 0;
        int blockheight = 0;

        if (x >= width)
          {
            x = 0;
            y += incrementblock;
          }

        // Loop through the horizontal component blocks of the MCU first
        // then for each horizontal line write out all of the vertical
        // components
       //factorV=1;
       // factorH=1;
        for (int factorVIndex = 0; factorVIndex < factorV; factorVIndex++)
          {
            blockwidth = 0;

            for (int factorHIndex = 0; factorHIndex < factorH; factorHIndex++)
              {
                // Captures the width of this block so we can increment the
                // X coordinate
                short[][] blockdata = (short[][]) data.remove(0);

                // Writes the data at the specific X and Y coordinate of
                // this component
                for (int yIndex = 0; yIndex < blockdata.length; yIndex++)
                {
                  for (int xIndex = 0; xIndex < blockdata[yIndex].length; xIndex++)
                    {
                      // The if statement is needed because blocks can spill over the
                      // frame width because they are padded to make sure we keep the
                      // height of the block the same as the width of the block
                      if (x + xIndex < width
                          && y + yIndex < height)
                        tempdata[y + yIndex][x + xIndex]=blockdata[yIndex][xIndex];
                    //	  raster.setSample(x + xIndex, y + yIndex, componentIndex,
                     //   		blockdata[yIndex][xIndex]);
                    }
                }
                blockwidth += blockdata[0].length;
                x += blockdata[0].length;
                blockheight = blockdata.length;
              }
            y += blockheight;
            x -= blockwidth;
            lastblockheight += blockheight;
          }
        y -= lastblockheight;
        incrementblock = lastblockheight;
        lastblockheight = 0;
        x += blockwidth;
      }
data=null;
data=new ArrayList();
  int counter1 = 0;

		short temp2[][] = new short[8][8];
		int xindex = 0;
		int yindex = 0;
		for (yindex = 0; yindex < height; yindex++) {
			for (xindex = 0; xindex < width; xindex++) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						//System.out.println(j+"   "+i+"  "+(j+yindex)+"    "+(i+xindex));
	                     if (i + xindex < width
	                             && j + yindex < height)
	    						temp2[j][i] = tempdata[j + yindex][i + xindex];
					}
				}

				data.add(counter1++, temp2);
				temp2 = new short[8][8];
				xindex += 7;
			}
			yindex += 7;
		}

    
    if(verbose){
    System.out.println(" for comp id*********************** "+componentIndex);
  for(int i=0;i<tempdata.length;i++){
	  for(int j=0;j<tempdata[0].length;j++){
		  System.out.print(tempdata[i][j]+",");
	  }
	  System.out.println();
  }
  System.out.println();
  
    }
  }

//  public void writeData(WritableRaster raster, int componentIndex)
//  {
//    int x = 0, y = 0, lastblockheight = 0, incrementblock = 0;
//
//    // Keep looping through all of the blocks until there are no more.
//    while(data.size() > 0)
//      {
//        if (x >= raster.getWidth())
//          {
//            x = 0;
//            y += 8;
//          }
//
//        // Loop through the horizontal component blocks of the MCU first
//        // then for each horizontal line write out all of the vertical
//        // components
//       //factorV=1;
//                // Captures the width of this block so we can increment the
//                // X coordinate
//                short[][] blockdata = (short[][]) data.remove(0);
//
//                // Writes the data at the specific X and Y coordinate of
//                // this component
//                writeBlock(raster, blockdata, componentIndex, x, y);
//                x+=8;
//                
//      }
//    
//  System.out.println(" for comp id "+componentIndex);
//  for(int i=0;i<32;i++){
//	  for(int j=0;j<32;j++){
//		  System.out.print(raster.getSampleDouble(j, i, componentIndex)+",");
//	  }
//	  System.out.println();
//  }
//  System.out.println();
//
//  }
//
  
  
  
  
  /**
   * Set the quantization table for this component.
   * 
   * @param quanttable
   */
  public void setQuantizationTable(int[] quanttable)
  {
    quantizationTable = quanttable;
  }

  void CalculateMcuDimensions ()
  {
    mcu_height = maxV * 8 ;
    mcu_width = maxH * 8;
    mcu_rows = (height + mcu_height - 1) / mcu_height ;
    mcu_cols = (width + mcu_width - 1) / mcu_width ;
    return ;
  }

  /**
   * Read in a partial MCU for this component
   * 
   * @param stream TODO
   * @throws JPEGException TODO
   * @throws IOException TODO
   */
  public void readComponentMCU(JPEGImageInputStream stream)
    throws JPEGException, IOException
  {
    for (int i = 0; i < factorH * factorV; i++)
      {
        short dc = decode_dc_coefficient(stream);
        short[] datablock = decode_ac_coefficients(stream);
        datablock[0] = dc;
        data.add(datablock);
        
      }
  }

  /**
   * Generated from text on F-22, F.2.2.1 - Huffman decoding of DC
   * coefficients on ISO DIS 10918-1. Requirements and Guidelines.
   * 
   * @param JPEGStream TODO
   *
   * @return TODO
   * @throws JPEGException TODO
   * @throws IOException TODO
   */
  public short decode_dc_coefficient(JPEGImageInputStream JPEGStream)
	throws JPEGException, IOException
  {
    int t = DCTable.decode(JPEGStream);
    short diff = (short) JPEGStream.readBits(t);
    diff = (short) HuffmanTable.extend((int) diff, t);
    diff = (short)(previousDC + diff);
    previousDC = diff;
    return diff;
  }

  /**
   * Generated from text on F-23, F.13 - Huffman decoded of AC coefficients
   * on ISO DIS 10918-1. Requirements and Guidelines.
   * 
   * @param JPEGStream TODO
   * @return TODO
   *
   * @throws JPEGException TODO
   * @throws IOException TODO
   */
  public short[] decode_ac_coefficients(JPEGImageInputStream JPEGStream)
    throws JPEGException, IOException
  {
    short[] zz = new short[64];

    for (int k = 1; k < 64; ++k)
      {
        int rs = ACTable.decode(JPEGStream);
        int rrrr = rs >> 4;
        int ssss = rs & 0xf;

        if (ssss != 0)
          {
            k += rrrr;
            short r1 = (short) JPEGStream.readBits(ssss);
            short s1 = (short) HuffmanTable.extend(r1, ssss);
            zz[k] = s1;
           // System.out.println(zz[k]);
          }
        else
          {
            if (rrrr != 15)
             break;// return (zz);
            k += 15;
          }
      }
    return zz;
  }

//public void writeRaster(WritableRaster raster, int componentIndex) {
//	    int x = 0, y = 0, lastblockheight = 0, incrementblock = 0;
//
//	    // Keep looping through all of the blocks until there are no more.
//	    while(data.size() > 0)
//	      {
//	        int blockwidth = 0;
//	        int blockheight = 0;
//
//	        if (x >= raster.getWidth())
//	          {
//	            x = 0;
//	            y += incrementblock;
//	          }
//
//	        // Loop through the horizontal component blocks of the MCU first
//	        // then for each horizontal line write out all of the vertical
//	        // components
//	       //factorV=1;
//	       // factorH=1;
//	        for (int factorVIndex = 0; factorVIndex < factorV; factorVIndex++)
//	          {
//	            blockwidth = 0;
//
//	            for (int factorHIndex = 0; factorHIndex < factorH; factorHIndex++)
//	              {
//	                // Captures the width of this block so we can increment the
//	                // X coordinate
//	                short[][] blockdata = (short[][]) data.remove(0);
//
//	                // Writes the data at the specific X and Y coordinate of
//	                // this component
//	                writeBlock(raster, blockdata, componentIndex, x, y);
//	                blockwidth += blockdata[0].length;
//	                x += blockdata[0].length;
//	                blockheight = blockdata.length;
//	              }
//	            y += blockheight;
//	            x -= blockwidth;
//	            lastblockheight += blockheight;
//	          }
//	        y -= lastblockheight;
//	        incrementblock = lastblockheight;
//	        lastblockheight = 0;
//	        x += blockwidth;
//	      }
//	    System.out.println(" for comp id "+componentIndex);
//	  for(int i=0;i<32;i++){
//		  for(int j=0;j<32;j++){
//			  System.out.print(raster.getSampleDouble(j, i, componentIndex)+",");
//		  }
//		  System.out.println();
//	  }
//	  System.out.println();
//	  
//
//}
  public void writeRaster(WritableRaster raster, int componentIndex) {
	    int x = 0, y = 0, lastblockheight = 0, incrementblock = 0;

	    // Keep looping through all of the blocks until there are no more.
	    while(data.size() > 0)
	      {
	        int blockwidth = 0;
	        int blockheight = 0;

	        if (x >= raster.getWidth())
	          {
	            x = 0;
	            y += incrementblock;
	          }

	        // Loop through the horizontal component blocks of the MCU first
	        // then for each horizontal line write out all of the vertical
	        // components
	       //factorV=1;
	       // factorH=1;
	                short[][] blockdata = (short[][]) data.remove(0);

	                // Writes the data at the specific X and Y coordinate of
	                // this component
	                writeBlock(raster, blockdata, componentIndex, x, y);
	                x += blockdata[0].length;
	                blockheight = blockdata.length;
	           // y += blockheight;
	        incrementblock = 8;
	      }
	    if(verbose){
	    System.out.println(" for comp id "+componentIndex);
	  for(int i=0;i<32;i++){
		  for(int j=0;j<32;j++){
			  System.out.print(raster.getSampleDouble(j, i, componentIndex)+",");
		  }
		  System.out.println();
	  }
	  System.out.println();
	  
	    }
}

public void changetoTwoDimensional() {
	    for (int i = 0; i < data.size(); i++)
        {
	     short temp[]= (short[]) data.remove(i);
            data.add(i,ZigZag.decode8x8_map(temp));
        }
}
}
