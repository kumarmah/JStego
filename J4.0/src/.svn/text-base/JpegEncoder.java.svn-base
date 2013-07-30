// Version 1.0a

// Copyright (C) 1998, James R. Weeks and BioElectroMech.

// Visit BioElectroMech at www.obrador.com.  Email James@obrador.com.

// See license.txt for details about the allowed used of this software.

// This software is based in part on the work of the Independent JPEG Group.

// See IJGreadme.txt for details about the Independent JPEG Group's license.

// This encoder is inspired by the Java Jpeg encoder by Florian Raemy,

// studwww.eurecom.fr/~raemy.

// It borrows a great deal of code and structure from the Independent

// Jpeg Group's Jpeg 6a library, Copyright Thomas G. Lane.

// See license.txt for details.

//HOW IT WORKS :

//JpegEncoder jpg = new JpegEncoder(image, quality, outStream);

// jpg.Compress();

import java.applet.Applet;

import java.awt.*;

import java.awt.image.*;

import java.io.*;

import java.util.*;

import java.lang.*;

/*

 * JpegEncoder - The JPEG main program which performs a jpeg compression of

 * an image.

 */

public class JpegEncoder extends Frame

{

	Thread runner;

	BufferedOutputStream outStream;

	Image image;

	JpegInfo JpegObj;

	Huffman Huf;

	DCT1 dct;

	int imageHeight, imageWidth;
        public static int componentCount=0;
	int Quality;

	int code;
	int dqt0[]=new int[64];
	int dqt1[]=new int[64];

	private byte[] huffbuffer;

	private byte[] dqtbuffer;
	public static int[] jpegNaturalOrder = {

	0, 1, 8, 16, 9, 2, 3, 10,

	17, 24, 32, 25, 18, 11, 4, 5,

	12, 19, 26, 33, 40, 48, 41, 34,

	27, 20, 13, 6, 7, 14, 21, 28,

	35, 42, 49, 56, 57, 50, 43, 36,

	29, 22, 15, 23, 30, 37, 44, 51,

	58, 59, 52, 45, 38, 31, 39, 46,

	53, 60, 61, 54, 47, 55, 62, 63,

	};
	//ArrayList[] compdata=new ArrayList[3];
	//ArrayList comp1data=null;
	//ArrayList comp2data=null;
	JPEGFrame frame=null;
	public void main(String infilename, String outfile, JPEGFrame frame) throws FileNotFoundException, IOException{
		Image redball = Toolkit.getDefaultToolkit().getImage ( infilename);
		OutputStream out=new FileOutputStream(outfile);
	//	this.compdata[0]=comp0data;
	//	this.compdata[1]=comp1data;
	//	this.compdata[2]=comp2data;
		this.frame=frame;
                componentCount=frame.getComponentCount();
//		JpegEncoder je=new JpegEncoder(redball,100,out);
		init(redball,100,out);
		Compress();

	out.close();
        }
	public void init(Image image, int quality, OutputStream out)

	{

		MediaTracker tracker = new MediaTracker(this);

		tracker.addImage(image, 0);

		try {

			tracker.waitForID(0);

		}

		catch (InterruptedException e) {

			// Got to do something?

		}

		/*

		 * Quality of the image.

		 * 0 to 100 and from bad image quality, high compression to good

		 * image quality low compression

		 */

		Quality = quality;

		/*

		 * Getting picture information

		 * It takes the Width, Height and RGB scans of the image.

		 */

		JpegObj = new JpegInfo(image,componentCount);
		for(int i=0;i<frame.getComponentCount();i++){
			JPEGComponent comp = (JPEGComponent) frame.components
			.get(i);
			JpegObj.HsampFactor[i]=comp.factorH;
			JpegObj.VsampFactor[i]=comp.factorV;
			//System.out.println(JpegObj.HsampFactor[i]+"   "+JpegObj.VsampFactor[i]);

	}
		if(componentCount>1)
                JpegObj.getYCCArray();
                else
                JpegObj.getYArray();

		imageHeight = JpegObj.imageHeight;

		imageWidth = JpegObj.imageWidth;

		outStream = new BufferedOutputStream(out);

		dct = new DCT1(Quality);
                if(componentCount>1)
		dct.setQtables(dqt0, dqt1);
                else
 		dct.setQtables(dqt0, null);



		Huf = new Huffman(imageWidth, imageHeight,componentCount,frame);

	}

//	public void setQuality(int quality) {
//
//		dct = new DCT1(quality);
//
//	}

//	public int getQuality() {
//
//		return Quality;
//
//	}
	public void setQtable(byte[] dqtbuffer, JPEGQTable[] tables, int index){

		this.dqtbuffer=dqtbuffer;
		if(index==0){
		dqt0=tables[0].getTable();
		dqt1=dqt0;
		}
		else if(index==1)
		dqt1=tables[1].getTable();

	}

	public void Compress() {

		WriteHeaders(outStream);

		WriteCompressedData(outStream);

		WriteEOI(outStream);

		try {

			outStream.flush();

		} catch (IOException e) {

			System.out.println("IO Error: " + e.getMessage());

		}

	}

	public void WriteCompressedData(BufferedOutputStream outStream) {

		int offset, i, j, r, c, a, b, temp = 0;

		int comp,  ypos, xblockoffset, yblockoffset;

		float inputArray[][];

	//	float dctArray1[][] = new float[8][8];

		//double dctArray2[][] = new double[8][8];

		int dctArray3[] = new int[8 * 8];

		/*

		 * This method controls the compression of the image.

		 * Starting at the upper left of the image, it compresses 8x8 blocks

		 * of data until the entire image has been compressed.

		 */

		int lastDCvalue[] = new int[JpegObj.NumberOfComponents];

		int zeroArray[] = new int[64]; // initialized to hold all zeros

		int Width = 0, Height = 0;

		int nothing = 0, not;

		int MinBlockWidth, MinBlockHeight;

		// This initial setting of MinBlockWidth and MinBlockHeight is done to

		// ensure they start with values larger than will actually be the case.

		MinBlockWidth = ((imageWidth % 8 != 0) ? (int) (Math
				.floor((double) imageWidth / 8.0) + 1) * 8 : imageWidth);

		MinBlockHeight = ((imageHeight % 8 != 0) ? (int) (Math
				.floor((double) imageHeight / 8.0) + 1) * 8 : imageHeight);

		for (comp = 0; comp < JpegObj.NumberOfComponents; comp++) {

			MinBlockWidth = Math.min(MinBlockWidth, JpegObj.BlockWidth[comp]);

			MinBlockHeight = Math
					.min(MinBlockHeight, JpegObj.BlockHeight[comp]);

		}

//		xpos = 0;
int d1[]=new int[3];
int d2[]=new int[64];
//int d3=0;

		for (r = 0; r < MinBlockHeight; r++) {

			for (c = 0; c < MinBlockWidth; c++) {

			//	xpos = c * 8;

			//	ypos = r * 8;

				//for(int i=0;i<)
				for (comp = 0; comp < JpegObj.NumberOfComponents; comp++) {

					Width = JpegObj.BlockWidth[comp];

					Height = JpegObj.BlockHeight[comp];

					//inputArray = (float[][]) JpegObj.Components[comp];
//					System.out.println(JpegObj.HsampFactor[i]+"   "+JpegObj.VsampFactor[i]);

					for (i = 0; i < JpegObj.VsampFactor[comp]; i++) {
						for (j = 0; j < JpegObj.HsampFactor[comp]; j++) {

							JPEGComponent comp1 = (JPEGComponent) frame.components
							.get(comp);

							try{
							short a1[]=(short[]) comp1.data.get(d1[comp]++);
							short a2[][]=ZigZag.decode8x8_map(a1);
							//double a1[][]=(double[][])compdata[comp].get(d1[comp]++);
								int l=0;
								for(int k1=0;k1<8;k1++)
									for(int k2=0;k2<8;k2++)
									d2[l++]=(int) a2[k1][k2];
							}catch(Exception e){
								System.out.println(comp+"    "+e+"   "+d1[comp]);
								System.exit(0);

							}
							//
								//							xblockoffset = j * 8;
//
//							yblockoffset = i * 8;
//
//							for (a = 0; a < 8; a++) {
//
//								for (b = 0; b < 8; b++) {
//
//									// I believe this is where the dirty line at the bottom of the image is
//
//									// coming from.  I need to do a check here to make sure I'm not reading past
//
//									// image data.
//
//									// This seems to not be a big issue right now. (04/04/98)
//
//									dctArray1[a][b] = inputArray[ypos
//											+ yblockoffset + a][xpos
//											+ xblockoffset + b];
//
//								}
//
//							}
//
//							// The following code commented out because on some images this technique
//
//							// results in poor right and bottom borders.
//
//							//                        if ((!JpegObj.lastColumnIsDummy[comp] || c < Width - 1) && (!JpegObj.lastRowIsDummy[comp] || r < Height - 1)) {
//
//							dctArray2 = dct.forwardDCT(dctArray1);
//
//							dctArray3 = dct.quantizeBlock(dctArray2,
//									JpegObj.QtableNumber[comp]);
//
////							dctArray3 = quantizeBlock(dctArray2,
////									JpegObj.QtableNumber[comp]);
//
//							//                        }
//
//							//                        else {
//
//							//                           zeroArray[0] = dctArray3[0];
//
//							//                           zeroArray[0] = lastDCvalue[comp];
//
//							//                           dctArray3 = zeroArray;
//
//							//                        }
//

//					if(comp==0)
//					double a[]=comp0data.get(index)
				Huf.HuffmanBlockEncoder(outStream, d2,
									lastDCvalue[comp],
									JpegObj.DCtableNumber[comp],
									JpegObj.ACtableNumber[comp]);

							lastDCvalue[comp] = d2[0];

						}

					}


				}

			}

		}

		Huf.flushBuffer(outStream);

	}
//	private int[] quantizeBlock(double[][] mydata, int no) {
//	    int tab[]=null;
//		double temp1[]=new double[64];
//		double temp[][]=new double[8][8];
//		double temp2[]=new double[64];
//	       if(no==0)
//	    	   tab=dqt0;
//	       else tab=dqt1;
//			for (int i = 0; i < 8; i++)
//				temp2[i]=tab[i];
//	       temp=ZigZag.decode8x8_map(temp2);
//			for (int i = 0; i < 8; i++)
//				for (int j = 0; j < 8; j++)
//	            temp[i][j]=mydata[i][j]/temp[i][j];
//			temp1=ZigZag.encode(temp);
//			int temp3[]=new int[64];
//			for (int i = 0; i < 8; i++){
//				temp3[i]=(int) Math.round(temp1[i]);
//				if (temp3[i] <= 0)
//					temp3[i] = 1;
//
//				if (temp3[i] >= 255)
//					temp3[i] = 255;
//			}
//			return temp3;
//		}
//
	public void WriteEOI(BufferedOutputStream out) {

		byte[] EOI = { (byte) 0xFF, (byte) 0xD9 };

		WriteMarker(EOI, out);

	}

	public void WriteHeaders(BufferedOutputStream out) {

		int i, j, index, offset, length;

		int tempArray[];

		// the SOI marker

		byte[] SOI = { (byte) 0xFF, (byte) 0xD8 };

		WriteMarker(SOI, out);

		// The order of the following headers is quiet inconsequential.

		// the JFIF header

		byte JFIF[] = new byte[18];

		JFIF[0] = (byte) 0xff;

		JFIF[1] = (byte) 0xe0;

		JFIF[2] = (byte) 0x00;

		JFIF[3] = (byte) 0x10;
//******************* start of 5 byte indentifier **************************

                JFIF[4] = (byte) 0x4a;

		JFIF[5] = (byte) 0x46;

		JFIF[6] = (byte) 0x49;

		JFIF[7] = (byte) 0x46;

		JFIF[8] = (byte) 0x00;
//*******************8 end of five byte indentifier *********************

                JFIF[9] = (byte) 0x01;

		JFIF[10] = (byte) 0x00;
//**************** end of version indentifier ***************
		JFIF[11] = (byte) 0x00;
//***************** end of X and Y densities ********************* IMPORTANT
		JFIF[12] = (byte) 0x00;

		JFIF[13] = (byte) 0x01;
//******************************* end X DEnsity **************************
		JFIF[14] = (byte) 0x00;

		JFIF[15] = (byte) 0x01;
//****************************** END of Y density **********************
		JFIF[16] = (byte) 0x00;

		JFIF[17] = (byte) 0x00;

		WriteArray(JFIF, out);

		// Comment Header

		String comment = new String();

		comment = JpegObj.getComment();

		length = comment.length();

		byte COM[] = new byte[length + 4];

		COM[0] = (byte) 0xFF;

		COM[1] = (byte) 0xFE;

		COM[2] = (byte) ((length >> 8) & 0xFF);

		COM[3] = (byte) (length & 0xFF);

		java.lang.System.arraycopy(JpegObj.Comment.getBytes(), 0, COM, 4,
				JpegObj.Comment.length());

		WriteArray(COM, out);

		// The DQT header

		// 0 is the luminance index and 1 is the chrominance index

		byte DQT[] = new byte[134];

		DQT[0] = (byte) 0xFF;

		DQT[1] = (byte) 0xDB;

		DQT[2] = (byte) 0x00;
                if(componentCount==1)
                    DQT[3] = (byte) 0x43;
                else
                    DQT[3] = (byte) 0x84;

		offset = 4;

			DQT[offset++] = (byte) ((0 << 4) + 0);

			for (j = 0; j < 64; j++) {

				DQT[offset++] =(byte) dqt0[j];

			}
                        if(componentCount>1){
			DQT[offset++] = (byte) ((0 << 4) + 1);

			for (j = 0; j < 64; j++) {

				DQT[offset++] =(byte) dqt1[j];

			}
                        }

		WriteArray(DQT, out);
	//WriteArray(dqtbuffer, out);

		// Start of Frame Header

		byte SOF[] = new byte[19];

		SOF[0] = (byte) 0xFF;

		SOF[1] = (byte) 0xC0;

		SOF[2] = (byte) 0x00;

		if(componentCount>1)
                    SOF[3] = (byte) 17;
                else
                    SOF[3] = (byte) 11;

		SOF[4] = (byte) JpegObj.Precision;

		SOF[5] = (byte) ((JpegObj.imageHeight >> 8) & 0xFF);

		SOF[6] = (byte) ((JpegObj.imageHeight) & 0xFF);

		SOF[7] = (byte) ((JpegObj.imageWidth >> 8) & 0xFF);

		SOF[8] = (byte) ((JpegObj.imageWidth) & 0xFF);

		SOF[9] = (byte) JpegObj.NumberOfComponents;

		index = 10;

		for (i = 0; i < SOF[9]; i++) {

			SOF[index++] = (byte) JpegObj.CompID[i];

			SOF[index++] = (byte) ((JpegObj.HsampFactor[i] << 4) + JpegObj.VsampFactor[i]);

			SOF[index++] = (byte) JpegObj.QtableNumber[i];

		}

		WriteArray(SOF, out);

		// The DHT Header

		byte DHT1[], DHT2[], DHT3[], DHT4[];

		int bytes, temp, oldindex, intermediateindex;

		length = 2;

		index = 4;

		oldindex = 4;

		DHT1 = new byte[17];

		DHT4 = new byte[4];

		DHT4[0] = (byte) 0xFF;

		DHT4[1] = (byte) 0xC4;
                int loopcount=0;
                if(componentCount>1)
                    loopcount=4;
                else
                    loopcount=2;
		for (i = 0; i < loopcount; i++) {

			bytes = 0;

			DHT1[index++ - oldindex] = (byte) ((int[]) Huf.bits.elementAt(i))[0];

			for (j = 1; j < 17; j++) {

				temp = ((int[]) Huf.bits.elementAt(i))[j];

				DHT1[index++ - oldindex] = (byte) temp;

				bytes += temp;

			}

			intermediateindex = index;

			DHT2 = new byte[bytes];

			for (j = 0; j < bytes; j++) {

				DHT2[index++ - intermediateindex] = (byte) ((int[]) Huf.val
						.elementAt(i))[j];

			}

			DHT3 = new byte[index];

			java.lang.System.arraycopy(DHT4, 0, DHT3, 0, oldindex);

			java.lang.System.arraycopy(DHT1, 0, DHT3, oldindex, 17);

			java.lang.System.arraycopy(DHT2, 0, DHT3, oldindex + 17, bytes);

			DHT4 = DHT3;

			oldindex = index;

		}

		DHT4[2] = (byte) (((index - 2) >> 8) & 0xFF);

		DHT4[3] = (byte) ((index - 2) & 0xFF);

//		WriteArray(huffbuffer, out);
		WriteArray(DHT4, out);

		// Start of Scan Header

		byte SOS[] = new byte[14];

		SOS[0] = (byte) 0xFF;

		SOS[1] = (byte) 0xDA;

		SOS[2] = (byte) 0x00;

		if(componentCount>1)
                    SOS[3] = (byte) 12;
                else
                    SOS[3] = (byte) 8;

		SOS[4] = (byte) JpegObj.NumberOfComponents;

		index = 5;

		for (i = 0; i < SOS[4]; i++) {

			SOS[index++] = (byte) JpegObj.CompID[i];

			SOS[index++] = (byte) ((JpegObj.DCtableNumber[i] << 4) + JpegObj.ACtableNumber[i]);

		}

		SOS[index++] = (byte) JpegObj.Ss;

		SOS[index++] = (byte) JpegObj.Se;

		SOS[index++] = (byte) ((JpegObj.Ah << 4) + JpegObj.Al);

		WriteArray(SOS, out);

	}

	void WriteMarker(byte[] data, BufferedOutputStream out) {

		try {

			out.write(data, 0, 2);

		} catch (IOException e) {

			System.out.println("IO Error: " + e.getMessage());

		}

	}

	void WriteArray(byte[] data, BufferedOutputStream out) {

		int i, length;

		try {

			length = (((int) (data[2] & 0xFF)) << 8) + (int) (data[3] & 0xFF)
					+ 2;

			out.write(data, 0, length);

		} catch (IOException e) {

			System.out.println("IO Error: " + e.getMessage());

		}

	}
	public void setHuffmanData(byte[] huffbuffer) {
		this.huffbuffer=huffbuffer;
	}

}


