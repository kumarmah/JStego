/* HuffmanTable.java --
   Copyright (C)  2006  Free Software Foundation, Inc.

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



import java.io.IOException;



/**
 * This Object construct a JPEGHuffmanTable which can be used to encode/decode
 * a scan from a JPEG codec stream. The table must be initalized with either a
 * BITS byte amount and a Huffman Table Value for decoding or a Huffman Size
 * and Huffman Code table for encoding.
 */
public class HuffmanTable
{
  public final static int HUFFMAN_MAX_TABLES = 4;

  private int[]  huffcode = new int[256];
  private int[]  huffsize = new int[256];
  private int[]  EHUFCO;
  private int[]  EHUFSI;
  private int[]  valptr = new int[16];
  private int[]  mincode = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                            -1,-1,-1};
  private int[]  maxcode = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                            -1, -1, -1};
  private short[] huffval;
  private short[] bits;

  static byte JPEG_DC_TABLE = 0;
  static byte JPEG_AC_TABLE = 1;
  static int counter=0;
  private short lastk = 0;

  int type=-1;
  public HuffmanTable(JPEGHuffmanTable table)
  {
	  huffval = table.getValues();
    bits = table.getLengths();
    type=table.JPEG_TABLE;
     generateSizeTable();
    generateCodeTable();
    generateDecoderTables();
  // System.out.println(type+"type");
    
  }

  /**
   * Generated from FIGURE C.1 - Generation of table of Huffman code sizes on
   * ISO DIS 10918-1. Requirements and Guidelines
   */
  private void generateSizeTable()
  {
    short index=0;
    for(short i=0; i < bits.length ; i++)
      {
        for(short j=0; j < bits[i] ; j++)
          {
            huffsize[index] = (short) (i+1);
         //System.out.println(type+"   "+index + "index"+"   "+huffsize[index]);
         index++;
         }
        huffsize [index] = 0 ;
      }
   // System.out.println();
    lastk = index;
  }

  /**
   * Generated from FIGURE C.2 - Generation of table of Huffman codes on
   * ISO DIS 10918-1. Requirements and Guidelines
   */
  private void generateCodeTable()
  {
    short k=0;
    int si = huffsize[0];
    int code = 0;
    for(short i=0; i < huffsize.length ; i++)
      {
    	if(huffsize[k]==0)break;
        while(huffsize[k]==si)
          {
            huffcode[k] = code;
            code++;
            k++;
          }
        code <<= 1;
        si++;
      }
  }

  /**
   * Generated from FIGURE F.15 - Generation of decode table generation on
   * ISO DIS 10918-1. Requirements and Guidelines
   */
  private void generateDecoderTables()
  {
	
  short jj=0;
	  for (int ii=0; ii < 16; ++ ii)
	  {
	     // ii is the index into Huffman code lengths
	     // jj is the index into Huffman code values
	     if (bits [ii] != 0)
	     {
	        // The jj'th Huffman value is the first with a Huffman code
	        // of length ii.
	        valptr [ii] = jj ;
	        mincode [ii] = huffcode [jj] ;
	        jj += bits [ii] ;
	        maxcode [ii] = huffcode [jj - 1] ;
	     }
	     else
	     {
	        // There are no Huffman codes of length (ii + 1).
	        maxcode [ii] = -1 ;
	        // An illegal value > maxcode[]
	        mincode [ii] = -1 ;
	        valptr [ii] = 0 ;
	     }

	  
	  
	  
//	  short bitcount = 0;
//    for(int i=0; i < 16 ; i++)
//      {
//        if(bits[i]!=0)
//          valptr[i] = bitcount;
//        for(int j=0 ; j < bits[i] ; j++)
//          {
//            if(huffcode[j+bitcount] < mincode[i] || mincode[i] == -1)
//              mincode[i] = huffcode[j+bitcount];
//
//            if(huffcode[j+bitcount] > maxcode[i])
//              maxcode[i] = huffcode[j+bitcount];
//          }
//        if(mincode[i]!=-1)
//          valptr[i] = (short) (valptr[i] - mincode[i]);
//        bitcount += bits[i];
      }
  }

  /**
   * Generated from FIGURE C.3 - Generation of Order Codes and tables EHUFCO
   * and EHUFSI from the ISO DIS 10918-1. Requirements and Guidelines
   */
  public void orderCodes(boolean isDC)
  {
    EHUFCO = new int[isDC ? 15 : 255];
    EHUFSI = new int[isDC ? 15 : 255];

    for (int p=0; p < lastk ; p++)
      {
        int i = huffval[p];
        if(i < 0 || i > EHUFCO.length || EHUFSI[i]!=0)
          System.err.println("Error, bad huffman table.");
        EHUFCO[i] = huffcode[p];
        EHUFSI[i] = huffsize[p];
      }
  }

  /**
   * Generated from FIGURE F.12 - Extending the sign bit of a decoded value in on
   * ISO DIS 10918-1. Requirements and Guidelines<p>
   *
   * @param diff TODO
   * @param t TODO
   * @return TODO
   */
  public static int extend(int diff, int t)
  {
    int Vt = (int)Math.pow(2,(t-1));
    if(diff<Vt)
      {
        Vt=(-1 << t)+1;
        diff=diff+Vt;
      }
    return diff;
  }

  /**
   * Generated from FIGURE F.16 - Procedure for DECODE on
   * ISO DIS 10918-1. Requirements and Guidelines<p>
   *
   * This function takes in a dynamic amount of bits and using the Huffman
   * table returns information on how many bits must be read in to a byte in
   * order to reconstruct said byte.
   *
   * @param JPEGStream the bits of the data stream.
   */
  public int decode(JPEGImageInputStream JPEGStream)
    throws IOException, JPEGException
    
  {
	 
//int a= JPEGStream.readByte();
// a= JPEGStream.readByte();
//a=256+a;
//System.out.println("******************************************");
//	  for(int i=0;i<32;i++){
//		  int code = JPEGStream.readByte() ;
//		  if(code<0)code=256+code;
//      System.out.print(code+"  ");
//  } 
	  int code = JPEGStream.readBit() ;
	   //   System.out.print(code);

	   int codelength ; // Called I in the standard.

	   // Here we are taking advantage of the fact that 1 bits are used as
	   // a prefix to the longer codes.
	   for (codelength = 0 ;
	        (codelength <16 && code > maxcode [codelength]) ;
	        ++ codelength)
	   {
		   int code1 = JPEGStream.readBit() ;
		    //  System.out.print(code1);

	      code = (int) (code << 1);
	      code=code| code1 ;
	     // System.out.print("code="+code);
	   }
	      //System.out.print("codefin="+code);

//	   if (codelength >= 1JpegMaxHuffmanCodeLength)
//	    throw EJpegBadData ("Bad Huffman Code Length") ;

	   // Now we have a Huffman code of length (codelength + 1) that
	   // is somewhere in the range
	   // mincode [codelength]..maxcode [codelength].

	   // This code is the (offset + 1)'th code of (codelength + 1) ;
	   int offset = code - mincode [codelength] ;

	   // valptr [codelength] is the first code of length (codelength + 1)
	   // so now we can look up the value for the Huffman code in the table.
	   int index = valptr [codelength] + offset ;
//	int a=(int)huff_values [index] ;
//	cout<< "index data........."<<dec<<a<<endl ;
	    //  System.out.println("   "+(++counter)+"    index="+index+"    val="+huffval [index]);
//	      if(counter==202){
	    	  
//	    	System.out.println("here");
//	      }
	   return huffval [index] ;

	  
	  
	  
//    int i=0;
//    short code = (short) JPEGStream.readBits(1);
//    while(code > maxcode[i]&& i <16)
//      {
//        i++;
//        code <<= 1;
//        code |= JPEGStream.readBits(1);
//      }
//    int offset = code - mincode [i] ;
//
//    // valptr [codelength] is the first code of length (codelength + 1)
//    // so now we can look up the value for the Huffman code in the table.
//    int index = valptr [i] + offset ;
//    return huffval [index] ;
// 
 }
}
