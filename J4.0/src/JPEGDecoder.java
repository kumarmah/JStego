/* JPEGDecoder.java --
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteOrder;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JPEGDecoder implements Constants {

    byte majorVersion;
    byte minorVersion;
    byte units;
    short Xdensity;
    short Ydensity;
    byte Xthumbnail;
    byte Ythumbnail;
    byte[] thumbnail;
    boolean isgui = false;
    JPEGFrame frame = null;
    BufferedImage image;
    JPEGComponent comp1 = null;
    JPEGComponent comp2 = null;
    int width;
    boolean isgraph = false;
    boolean prevWasEven = false;
    byte R[][] = new byte[8][8];
    byte G[][] = new byte[8][8];
    byte B[][] = new byte[8][8];
     
    private int zeroLimit;
      public long totaltime_2comp = 0;
    int height;
     
    public String inputImageFile;
     public boolean verbose;
    public boolean system;
    public String verfilename;
    
    
    byte marker;
     int lastIndexModified = 0;
    int valueModified = 0;
    boolean isZipped = false;
    public static int componentCount = 0;
    public boolean error = false;
    /**
     * This decoder expects JFIF 1.02 encoding.
     */
    public static final byte MAJOR_VERSION = (byte) 1;
    public static final byte MINOR_VERSION = (byte) 2;
    /**
     * The length of the JFIF field not including thumbnail data.
     */
    public static final short JFIF_FIXED_LENGTH = 16;
    /**
     * The length of the JFIF extension field not including extension data.
     */
    public static final short JFXX_FIXED_LENGTH = 8;
    private JPEGImageInputStream jpegStream;
    ArrayList jpegFrames = new ArrayList();
    JPEGHuffmanTable[] dcTables = new JPEGHuffmanTable[2];
    JPEGHuffmanTable[] acTables = new JPEGHuffmanTable[2];
    JPEGQTable[] qTables = new JPEGQTable[4];
    public JpegEncoder je = new JpegEncoder();
    PrintStream printStream; // declare a print stream object
     private boolean eoiflag = false;
    
//    int order = 1;
//    int startPoint = 1;
//    int exclude = 0;
    YCbCr_ColorSpace col = new YCbCr_ColorSpace();
    
    
 //   int BIT = 1;
    
   

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public JPEGDecoder(String inputImageFile, boolean verbose, boolean system, String verfilename) {
        try {
            this.inputImageFile = inputImageFile;
            this.verbose = verbose;
            this.system = system;
            this.verfilename = verfilename;
            System.out.println(" JPEG Decoder Started.......!!! " + inputImageFile);
            File file = new File(inputImageFile);
            ImageInputStream in = new FileImageInputStream(file);
            jpegStream = new JPEGImageInputStream(in);
            jpegStream.setByteOrder(ByteOrder.BIG_ENDIAN);
            if (jpegStream.findNextMarker() != JPEGMarker.SOI) {
                throw new JPEGException("Failed to find SOI marker.");
            } 
            FileOutputStream out; // declare a file output object // declare a file output object
            try {
                if (system) {
                    printStream = new PrintStream(System.out);
                } else {
                    // Create a new file output stream
                    out = new FileOutputStream(verfilename);
                    printStream = new PrintStream(out);
                }

            } catch (Exception e) {
                System.err.println("Error writing to file");
                error = true;
            }


        } catch (FileNotFoundException ex) {
            Logger.getLogger(JPEGDecoder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JPEGDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
   

    public void decode() throws Throwable {

        printStream.println(" Start of Image " + inputImageFile + "   ..!!!");
        // The frames in this jpeg are loaded into a list. There is
        // usually just one frame except in heirarchial progression where
        // there are multiple frames.

        // The restart interval defines how many MCU's we should have
        // between the 8-modulo restart marker. The restart markers allow
        // us to tell whether or not our decoding process is working
        // correctly, also if there is corruption in the image we can
        // recover with these restart intervals. (See RSTm DRI).
        int resetInterval = 0;

        // The JPEGDecoder constructor parses the JFIF field. At this
        // point jpegStream points to the first byte after the JFIF field.
        // for(int i=0;i<18;i++)
        // System.out.println(jpegStream.readByte());
        // Find the first marker after the JFIF field.
        marker = jpegStream.findNextMarker();
        // Check for a JFIF extension field directly following the JFIF
        // header and advance the current marker to the next marker in the
        // stream, if necessary.
        // decodeJFIFExtension();

        // Loop through until there are no more markers to read in, at
        // that point everything is loaded into the jpegFrames array and
        // can be processed.
        while (true) {
            switch (marker) {
                // APPn Application Reserved Information - Just throw this
                // information away because we wont be using it.
                case JPEGMarker.APP0:
                case JPEGMarker.APP1:
                case JPEGMarker.APP2:
                case JPEGMarker.APP3:
                case JPEGMarker.APP4:
                case JPEGMarker.APP5:
                case JPEGMarker.APP6:
                case JPEGMarker.APP7:
                case JPEGMarker.APP8:
                case JPEGMarker.APP9:
                case JPEGMarker.APP10:
                case JPEGMarker.APP11:
                case JPEGMarker.APP12:
                case JPEGMarker.APP13:
                case JPEGMarker.APP14:
                case JPEGMarker.APP15:
                case JPEGMarker.COM:

                    processApplication(marker);
                    break;

                // ****************************************************************************************************
                // ****************************************************************************************************
                case JPEGMarker.DQT:
                    // DQT non-SOF Marker - This defines the quantization
                    // coeffecients, this allows us to figure out the quality of
                    // compression and unencode the data. The data is loaded and
                    // then stored in to an array.
                    short quantizationLength = (short) (jpegStream.readShort() - 2);
                    int dqtcounter = 0;
                    byte dqtbuffer[] = new byte[quantizationLength + 4];
                    dqtbuffer[dqtcounter++] = (byte) 0xFF;
                    dqtbuffer[dqtcounter++] = (byte) 0xDB;

                    dqtbuffer[dqtcounter++] = (byte) ((quantizationLength + 2) >> 8);
                    dqtbuffer[dqtcounter++] = (byte) ((quantizationLength + 2) & 0xff);
                    //printStream.println("***********Start Quantization Table data**************************");
                    if (verbose) {
                        printStream.println("\nDefine Quantization Table => Length: " + (quantizationLength + 2));
                    }
                    for (int j = 0; j < quantizationLength / 65; j++) {
                        byte quantSpecs = jpegStream.readByte();
                        dqtbuffer[dqtcounter++] = (byte) quantSpecs;
                        byte tableprec = (byte) (quantSpecs >> 4);
                        byte tableind = (byte) (quantSpecs & 0x0f);

                        int[] quantData = new int[64];
                        if ((byte) (tableprec) == 0) // Precision 8 bit.
                        {
                            for (int i = 0; i < 64; i++) {
                                quantData[i] = jpegStream.readByte();
                                dqtbuffer[dqtcounter++] = (byte) quantData[i];
                            }

                        } else if ((byte) (tableprec) == 1) // Precision 16 bit.
                        {
                            for (int i = 0; i < 64; i++) {
                                quantData[i] = jpegStream.readShort();
                            }
                        }
                        qTables[(int) (tableind)] = new JPEGQTable(quantData);
                        je.setQtable(dqtbuffer, qTables, tableind);

                        //printStream.println("******** Table Precision:" + tableprec + "  Table index:" + tableind);
                        if (verbose) {
                            printQTable(tableind);
                        }
                    }
                    // if (verbose)
                    break;
                // ************************************************************************

                // ***********************************************************************
                case JPEGMarker.DHT:
                    // DHT non-SOF Marker - Huffman Table is required for decoding
                    // the JPEG stream, when we receive a marker we load in first
                    // the table length (16 bits), the table class (4 bits), table
                    // identifier (4 bits), then we load in 16 bytes and each byte
                    // represents the count of bytes to load in for each of the 16
                    // bytes. We load this into an array to use later and move on 4
                    // huffman tables can only be used in an image.

                    int huffmanLength = (jpegStream.readShort() - 2);
                    int huffcounter = 0;
                    byte huffbuffer[] = new byte[huffmanLength + 4];
                    huffbuffer[huffcounter++] = (byte) 0xFF;
                    huffbuffer[huffcounter++] = (byte) 0xC4;
                    huffbuffer[huffcounter++] = (byte) ((huffmanLength + 2) >> 8);
                    huffbuffer[huffcounter++] = (byte) ((huffmanLength + 2) & 0xff);
                    // printStream.println(huffbuffer[0]);
                    // printStream.println(huffbuffer[1]);

                   // printStream.println("*********** Start of Huffman Table Data ********");
                    if (verbose) {
                        printStream.println("\nDefine Huffman Table => Length: " + (huffmanLength + 2));
                    }
                    // Keep looping until we are out of length.
                    int index = huffmanLength;

                    // Multiple tables may be defined within a DHT marker. This
                    // will keep reading until there are no tables left, most
                    // of the time there are just one tables.
                    while (index > 0) {
                        // Read the identifier information and class
                        // information about the Huffman table, then read the
                        // 16 byte codelength in and read in the Huffman values
                        // and put it into table info.
                        byte huffmanInfo = jpegStream.readByte();
                        huffbuffer[huffcounter++] = huffmanInfo;
                        byte tableClass = (byte) (huffmanInfo >> 4);
                        byte huffmanIndex = (byte) (huffmanInfo & 0x0f);
                        short[] codeLength = new short[16];
                        // printStream.println(0xFF);
                        for (int i = 0; i < 16; i++) {
                            codeLength[i] = jpegStream.readByte();
                            huffbuffer[huffcounter++] = (byte) codeLength[i];
                        }
                        int huffmanValueLen = 0;
                        for (int i = 0; i < 16; i++) {
                            huffmanValueLen += codeLength[i];
                        }
                        index -= (huffmanValueLen + 17);
                        short[] huffmanVal = new short[huffmanValueLen];
                        for (int i = 0; i < huffmanVal.length; i++) {
                            huffmanVal[i] = (short) jpegStream.readByte();
                            huffbuffer[huffcounter++] = (byte) huffmanVal[i];
                            if (huffmanVal[i] < 0) {
                                huffmanVal[i] += 256;
                            }
                            // printStream.println(huffmanVal[i]);
                        }
                        // Assign DC Huffman Table.
                        if (tableClass == HuffmanTable.JPEG_DC_TABLE) {
                            dcTables[(int) huffmanIndex] = new JPEGHuffmanTable(
                                    codeLength, huffmanVal, 0);
                            if ((int) huffmanIndex == 0) {
                               // je.setDCLum(codeLength, huffmanVal);
                            } else {
                              //  je.setDCChrom(codeLength, huffmanVal);
                            }
                        } // Assign AC Huffman Table.
                        else if (tableClass == HuffmanTable.JPEG_AC_TABLE) {
                            acTables[(int) huffmanIndex] = new JPEGHuffmanTable(
                                    codeLength, huffmanVal, 1);
                            if ((int) huffmanIndex == 0) {
                               // je.setACLum(codeLength, huffmanVal);
                            } else {
                               // je.setACChrom(codeLength, huffmanVal);
                            }
                        }
                       // printStream.println("Table Class:" + tableClass + "  Table index:" + huffmanIndex);
                        if (verbose) {
                            printOneHTable(tableClass, huffmanIndex);
                        }
                    }
                    je.setHuffmanData(huffbuffer);
                    break;

                // *****************************************************************************************************************************

                case JPEGMarker.SOF0:
                    // SOFn Start of Frame Marker, Baseline DCT - This is the start
                    // of the frame header that defines certain variables that will
                    // be carried out through the rest of the encoding. Multiple
                    // frames are used in a heirarchiel system, however most JPEG's
                    // only contain a single frame.
                    jpegFrames.add(new JPEGFrame());
                    frame = (JPEGFrame) jpegFrames.get(jpegFrames.size() - 1);
                    // Skip the frame length.
                    int lengthsof0 = jpegStream.readShort();
                    byte precision = jpegStream.readByte();
                    height = jpegStream.readShort();
                    width = jpegStream.readShort();
                    int ccount = jpegStream.readByte();
                    if (ccount != 3 && ccount != 1) {
                        return;
                    }

                   // printStream.println("**************Start of Frame Data  *********************");
                    printStream.println("Start of Frame...... BaseLine DCT   Length:" + lengthsof0 + "  Precision:" + precision + "  Height:" + height + "  Width:" + width + "  Component count:" + ccount);

                    // Bits percision, either 8 or 12.
                    frame.setPrecision(precision);
                    // Scan lines = to the height of the frame.
                    frame.setScanLines((short) height);
                    // Scan samples per line = to the width of the frame.
                    frame.setSamplesPerLine((short) width);
                    // Number of Color Components (or channels).
                    frame.setComponentCount(ccount);

                    // Set the color mode for this frame, so far only 2 color
                    // modes are supported.

                    if (frame.getComponentCount() == 1) {
                        frame.setColorMode(JPEGFrame.JPEG_COLOR_GRAY);
                    } else if (frame.getComponentCount() == 3) {
                        frame.setColorMode(JPEGFrame.JPEG_COLOR_YCbCr);
                    } else {
                        throw new JPEGException(
                                "Invalid number of components in scan.");
                    }
                    // Add all of the necessary components to the frame.
                    for (int i = 0; i < frame.getComponentCount(); i++) {
                        frame.addComponent(jpegStream.readByte(), jpegStream.readByte(), jpegStream.readByte(), verbose, printStream);
                    }
                    break;

                case JPEGMarker.SOF2:
                    jpegFrames.add(new JPEGFrame());
                    frame = (JPEGFrame) jpegFrames.get(jpegFrames.size() - 1);
                    // Skip the frame length.
                    int lengthsof1 = jpegStream.readShort();
                    byte precision1 = jpegStream.readByte();
                    short height1 = jpegStream.readShort();
                    short width1 = jpegStream.readShort();
                    byte ccount1 = jpegStream.readByte();

                    printStream.println("Start of Frame SOF2 \n Progressive DCT \nLength:" + lengthsof1 + "\nPrecision:" + precision1 + "\nHeight:" + height1 + "\nWidth:" + width1 + "\nComponent count:" + ccount1);

                    // Bits percision, either 8 or 12.
                    frame.setPrecision(precision1);
                    // Scan lines = to the height of the frame.
                    frame.setScanLines(height1);
                    // Scan samples per line = to the width of the frame.
                    frame.setSamplesPerLine(width1);
                    // Number of Color Components (or channels).
                    frame.setComponentCount(ccount1);

                    // Set the color mode for this frame, so far only 2 color
                    // modes are supported.
                    if (frame.getComponentCount() == 1) {
                        frame.setColorMode(JPEGFrame.JPEG_COLOR_GRAY);
                        printStream.println("***************Image is Grayscale");

                    } else {
                        frame.setColorMode(JPEGFrame.JPEG_COLOR_YCbCr);
                        printStream.println("*************** Image is RGB Type ");

                    }
                    // Add all of the necessary components to the frame.
                    for (int i = 0; i < frame.getComponentCount(); i++) {
                        frame.addComponent(jpegStream.readByte(), jpegStream.readByte(), jpegStream.readByte(), verbose, printStream);
                    }
                    break;
                case JPEGMarker.SOS:
                    // SOS non-SOF Marker - Start Of Scan Marker, this is where the
                    // actual data is stored in a interlaced or non-interlaced with
                    // from 1-4 components of color data, if three components most
                    // likely a YCrCb model, this is a fairly complex process.

                    // Read in the scan length.
                    // printStream.println(jpegStream.getpos());

                    int length = jpegStream.readShort();
                    printStream.println("*********Start of Scan Data  **************");
                    printStream.println("Start Of Scan => Length:" + length);

                    // Number of components in the scan.
                    byte numberOfComponents = jpegStream.readByte();
                    byte[] componentSelector = new byte[numberOfComponents];
                    for (int i = 0; i < numberOfComponents; i++) {
                        // Component ID, packed byte containing the Id for the
                        // AC table and DC table.
                        byte componentID = jpegStream.readByte();
                        byte tableInfo = jpegStream.readByte();
                        int dctab = (int) (tableInfo >> 4);
                        int actab = (int) (tableInfo & 0x0f);
                        JPEGHuffmanTable dctable = dcTables[dctab];
                        JPEGHuffmanTable actable = acTables[actab];
                        frame.setHuffmanTables((byte) componentID, actable,
                                dctable);
                        // frame.setHuffmanTables((byte)componentID,
                        // (JPEGHuffmanTable)acTables[(int) (tableInfo >> 4)],
                        // dcTables[(int) (tableInfo & 0x0f)]);
                        componentSelector[i] = componentID;

                       // printStream.println("Component ID: " + componentID + "\n" + "  DC Entropy Table: " + dctab + "\n" + "  AC Entropy Table: " + actab);

                    }
                    byte startSpectralSelection = jpegStream.readByte();
                    byte endSpectralSelection = jpegStream.readByte();
                    byte successiveApproximation = jpegStream.readByte();
                    int successiveapproximationhigh = successiveApproximation >> 4; // Ah
                    // in
                    // standard
                    int successiveapproximationlow = successiveApproximation & 0x0F; // Al
                    // in
                    // standard

                   // printStream.println(" Spectral Selection Start: " + startSpectralSelection + "\n Spectral Selection End: " + endSpectralSelection + "\n Successive Approximation High: " + successiveapproximationhigh + "\n Successive Approximation Low: " + successiveapproximationlow);

                    // readSequentialScan();
                    // printStream.println(jpegStream.getpos());

                    // byte b=0;
                    // while((b=jpegStream.readByte())!=255){
                    // printStream.print(b+",");
                    // }
                    int mcuIndex = 0;
                    int mcuTotalIndex = 0;
                    // This loops through until a MarkerTagFound exception is
                    // found, if the marker tag is a RST (Restart Marker) it
                    // simply skips it and moves on this system does not handle
                    // corrupt data streams very well, it could be improved by
                    // handling misplaced restart markers.
                    System.gc();
                    while (true) {
                        try {

                            // Loop though capturing MCU, instruct each
                            // component to read in its necessary count, for
                            // scaling factors the components automatically
                            // read in how much they need

                            for (int compIndex = 0; compIndex < numberOfComponents; compIndex++) {
                                JPEGComponent comp = (JPEGComponent) frame.components.getComponentByID(componentSelector[compIndex]);
                                comp.readComponentMCU(jpegStream);
                            }
                            mcuIndex++;
                            mcuTotalIndex++;
                        } // We've found a marker, see if the marker is a restart
                        // marker or just the next marker in the stream. If
                        // it's the next marker in the stream break out of the
                        // while loop, if it's just a restart marker skip it
                        catch (JPEGMarkerFoundException bse) {
                            // Handle JPEG Restart Markers, this is where the
                            // count of MCU's per interval is compared with
                            // the count actually obtained, if it's short then
                            // pad on some MCU's ONLY for components that are
                            // greater than one. Also restart the DC prediction
                            // to zero.
                            marker = jpegStream.marker;
                            if (marker == JPEGMarker.RST0 || marker == JPEGMarker.RST1 || marker == JPEGMarker.RST2 || marker == JPEGMarker.RST3 || marker == JPEGMarker.RST4 || marker == JPEGMarker.RST5 || marker == JPEGMarker.RST6 || marker == JPEGMarker.RST7) {
                                for (int compIndex = 0; compIndex < numberOfComponents; compIndex++) {
                                    JPEGComponent comp = (JPEGComponent) frame.components.getComponentByID(componentSelector[compIndex]);
                                    if (compIndex > 1) {
                                        comp.padMCU(mcuTotalIndex, resetInterval - mcuIndex);
                                    }
                                    comp.resetInterval();
                                }
                                mcuTotalIndex += (resetInterval - mcuIndex);
                                mcuIndex = 0;
                            } else {
                                if (marker == (byte) JPEGMarker.EOI) {
                                    eoiflag = true;
                                }

                                // We're at the end of our scan, exit out.
                                break;
                            }
                        }
                    }
                    // System.exit(0);

                    break;
                case JPEGMarker.DRI:
                    // DRI - This defines the restart interval, if we have a
                    // restart interval when we reach our restart modulo calculate
                    // whether the count of MCU's specified in the restart
                    // interval have been reached, if they havent then pad with
                    // whatever MCU was last used, this is supposed to be a form of
                    // error recovery but it turns out that some JPEG encoders
                    // purposely cause missing MCU's on repeating MCU's to compress
                    // data even more (even though it adds an extra layer of
                    // complexity.. But since when is JPEG easy?
                    int lengthdri = jpegStream.readShort();
                    resetInterval = jpegStream.readShort();

                    printStream.println("Define Restart interval Length:" + lengthdri + "  Interval:" + resetInterval);

                    break;
                case JPEGMarker.DNL:
                    // DNL - This sets the height of the image. This is the Define
                    // Number Lines for the image, I'm not sure exactly why we need
                    // this but, whatever we'll abide.
                    frame.setScanLines(jpegStream.readShort());
                    break;
                case JPEGMarker.EOI:
                    // EOI - End of Image, this processes the frames and turns the
                    // frames into a buffered image.
                    eoiflag = true;

                    printStream.println("**********   End of Image reached  **************");

                    if (jpegFrames.size() == 0) {
                        return;
                    } else if (jpegFrames.size() == 1) {
                        JPEGComponent comp = null;
                        componentCount = frame.getComponentCount();
                        for (int i = 0; i < frame.getComponentCount(); i++) {
                            comp = (JPEGComponent) frame.components.get(i);
                            // comp.changetoTwoDimensional();

                            comp.setQuantizationTable(qTables[comp.quant_id].getTable());
                            comp = null;
                        }

                        // if (isEmbed)
                        // embedData(frame);
                        // else
                        // extractData(frame);
                        return;
                    }
                    break;
                case JPEGMarker.SOF1:
                    // ERROR - If we encounter any of the following marker codes
                    // error out with a codec exception, progressive, heirarchial,
                    // differential, arithmetic, lossless JPEG's are not supported.
                    // This is where enhancements can be made for future versions.
                    // Thankfully 99% of all JPEG's are baseline DCT.
                    throw new JPEGException("Unsupported Codec Type: Extended " + "Sequential DCT JPEG's Not-Supported");
                // case JPEGMarker.SOF2:
                // throw new JPEGException("Unsupported Codec Type: Progressive
                // DCT JPEG's Not-Supported");
                case JPEGMarker.SOF3:
                    throw new JPEGException("Unsupported Codec Type:" + " Lossless (sequential)");
                case JPEGMarker.SOF5:
                    throw new JPEGException("Unsupported Codec Type:" + " Differential sequential DCT");
                case JPEGMarker.SOF6:
                    throw new JPEGException("Unsupported Codec Type:" + " Differential progressive DCT");
                case JPEGMarker.SOF7:
                    throw new JPEGException("Unsupported Codec Type:" + " Differential lossless");
                case JPEGMarker.SOF9:
                case JPEGMarker.SOF10:
                case JPEGMarker.SOF11:
                case JPEGMarker.SOF13:
                case JPEGMarker.SOF14:
                case JPEGMarker.SOF15:
                    throw new JPEGException("Unsupported Codec Type:" + " Arithmetic Coding Frame");
                default:
                // Unknown marker found, ignore it.
            }
            if (!eoiflag) {
                marker = jpegStream.findNextMarker();
            }
        }
    }

    private void copyObjects(ArrayList comp1data2, ArrayList data) {
        double[][] temp = null;
        for (int i = 0; i < data.size(); i++) {
            double[][] a = new double[8][8];
            temp = (double[][]) data.get(i);
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    a[j][k] = temp[j][k];
                }
            }

            comp1data2.add(a);
        }

    }

    double round(double value, int decimalPlace) {
        double power_of_ten = 1;
        while (decimalPlace-- > 0) {
            power_of_ten *= 10.0;
        }
        return Math.round(value * power_of_ten) / power_of_ten;
    }

    

   

    

    
   
  
    // public int[] getHeaderBits(long len, int unumber) {
    // int bit[] = new int[HEADERLENGTH];
    // int count=0;
    // for (int i = 0; i < FILE_SIZE_LENGTH; i++) {
    // bit[count++] = (int) (len % 2);
    // len = len / 2;
    //
    // }
    // for (int j=0; j < UNIQUE_NUMBER_LENGTH; j++) {
    // bit[count++] = (int) (unumber % 2);
    // unumber = unumber / 2;
    //
    // }
    //
    // return bit;
    // }
    
//    private boolean getUpdatedResult(short[][] currentBlock, int[] qtable1,
//            DCT myDCT, short[][] cb, short[][] cr, int[] currentBits,
//            JPEGComponent comp, int blockIndex) {
//        short[][] temp1 = null;
//        short temp2[][] = null;
//        byte[] flat = null;
//        temp1 = comp.quantitize2D_Data(currentBlock);
//        temp2 = idctData(temp1, myDCT);
//
//        covert_to_RGB(temp2, cb, cr);
//        flat = flattenData();
//        // if(verbose){
//        // for(int i=0;i<flat.length;i++)
//        //
//        // //printStream.print(flat[i]+" ");
//        // printStream.println("######################################3 ");
//        // }
//        // printStream.println("######################################3 ");
//        if (matchData(currentBits, flat)) {
//            for (int i = 0; i < flat.length; i++) // printStream.print(flat[i]+" ");
//            {
//                return true;
//            }
//        }
//        return false;
//    }

//    private boolean matchData(int[] currentBits, byte[] flat) {
//        try {
//            md = MessageDigest.getInstance("MD5");
//            md.update(flat);
//            int des[] = new int[BIT * 8];
//            int count = 0;
//            byte[] res = md.digest();
//            byte a = 0;
//            // byte a = res[res.length - 1];
//            for (int i = 0; i < BIT; i++) {
//                a = res[res.length - (BIT + i)];
//                int[] temp = getBitsfromByte(a);
//                for (int j = 0; j < 8; j++) {
//                    des[count++] = temp[j];
//                    // System.out.println("here "+des[count-1]);
//                }
//            }
//            // printStream.print(flat[i]);
//            // System.out.println("here********** "+des);
//
//            for (int i = 0; i < bitsPerBlock; i++) {
//                // System.out.println(des[des.length-i-1]+"
//                // "+currentBits[bitsPerBlock - i - 1]);
//                if (des[des.length - i - 1] != currentBits[bitsPerBlock - i - 1]) {
//                    // System.out.println("*********************************");
//                    return false;
//                }
//            }
//            if (verbose) {
//                printStream.println("MD5 HASHED Byte: " + a);
//            }
//            // printStream.println();
//
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // TODO Auto-generated method stub
//        return true;
//    }

//    private byte[] flattenData() {
//        if (componentCount > 1) {
//            byte[] flat = new byte[64 * 3 + password.length()];
//            int k = 0;
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    flat[k++] = (byte) R[i][j];
//                    flat[k++] = (byte) G[i][j];
//                    flat[k++] = (byte) B[i][j];
//
//                }
//            }
//            byte buf[] = password.getBytes();
//            for (int l = 0; l < buf.length; l++) {
//                flat[k++] = buf[l];
//            }
//            return flat;
//        } else {
//
//            byte[] flat = new byte[64 + password.length()];
//            int k = 0;
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    flat[k++] = (byte) R[i][j];
//
//                }
//            }
//            byte buf[] = password.getBytes();
//            for (int l = 0; l < buf.length; l++) {
//                flat[k++] = buf[l];
//            }
//            return flat;
//
//        }
//    }
//
//    private void covert_to_RGB(short[][] y, short[][] cb, short[][] cr) {
//        float buf[] = new float[3];
//        float buf1[] = new float[3];
//        if (componentCount > 1) {
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    buf[0] = (short) y[i][j];
//                    buf[1] = (short) cb[i][j];
//                    buf[2] = (short) cr[i][j];
//                    buf1 = col.toRGB(buf);
//                    R[i][j] = (byte) buf[0];
//                    G[i][j] = (byte) buf[1];
//                    B[i][j] = (byte) buf[2];
//                    // if(i<2&&j<2)printStream.println(y[i][j]+" "+cb[i][j]+" "+cr[i][j]);
//
//                }
//            }
//
//        } else {
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    buf[0] = (float) y[i][j];
//                    R[i][j] = (byte) buf[0];
//                    // if(i<2&&j<2)printStream.println(y[i][j]+" "+cb[i][j]+" "+cr[i][j]);
//
//                }
//            }
//
//        }
//    }

    private short[][] idctData(short[][] temp1, DCT myDCT) {
        return myDCT.fast_idct(temp1);
    }

    private double[] quantitizeBlock(double[] mydata, int[] qtable1) {
        double temp1[] = new double[64];
        for (int j = 0; j < mydata.length; j++) {
            temp1[j] = mydata[j] * qtable1[j];
        }
        return temp1;
    }

   

    

    

    

    private void processApplication(byte marker2) throws IOException {
        int length = jpegStream.readShort();
        marker2 = (byte) (marker2 & (byte) 0x0F);
        jpegStream.skipBytes(length - 2);

        if (verbose) {
            if (marker2 == JPEGMarker.COM) {
                printStream.println("Comment Marker: Length:" + length);
            } else {
                printStream.println("Application Marker::" + marker2 + " Length:" + length);
            }
        }

    }

    private void printHuffTable() {
        printStream.println("Luminance DC Huffman Table");
        printOneHTable(0, 0);
        printStream.println("Crminance DC Huffman Table");
        printOneHTable(0, 1);
        printStream.println("Luminance AC Huffman Table");
        printOneHTable(1, 0);
        printStream.println("Crominance AC Huffman Table");
        printOneHTable(1, 1);

    }

    private void printOneHTable(int dcac, int lumcrom) {
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setMaximumIntegerDigits(3);
        myFormat.setMinimumIntegerDigits(2);
        FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);
        String out = "";
        short[] a;
        short[] b;
        if (dcac == 0) {
            a = dcTables[lumcrom].getLengths();
            b = dcTables[lumcrom].getValues();
        } else {
            a = acTables[lumcrom].getLengths();
            b = acTables[lumcrom].getValues();
        }
        int count = 0;
        for (int k = 0; k < 16; k++) {
            // out = myFormat.format(k+1,
            // new StringBuffer(), fp).toString();
            // out = getSpaces(5 - fp.getEndIndex()) + out;
            //
            // printStream.print(out);

            for (int i = 0; i < a[k]; i++) {
                out = myFormat.format(k + 1, new StringBuffer(), fp).toString();
                out = getSpaces(5 - fp.getEndIndex()) + out;

                printStream.print(out);

                out = myFormat.format(b[count++], new StringBuffer(), fp).toString();
                out = getSpaces(5 - fp.getEndIndex()) + out;

                printStream.print(out);
                printStream.println();
            }
            // printStream.println();
        }
        printStream.println();
        printStream.println();

        // a=qTables[1].getTable();
        // b=ZigZag.decode(a);

        // }
    }

    private void printQTable(int index) {

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setMaximumIntegerDigits(3);
        myFormat.setMinimumIntegerDigits(2);
        FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);
        String out = "";

        int a[] = qTables[index].getTable();
        int b[][] = ZigZag.decode(a);
        // printStream.println("Lumninence Quantization Table");

        // for (int k = 0; k < 2; k++) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                out = myFormat.format(b[i][j], new StringBuffer(), fp).toString();
                out = getSpaces(5 - fp.getEndIndex()) + out;

                printStream.print(out);
            }
            printStream.println();
        }
        // printStream.println();
        // printStream.println();
        // if (k == 0)
        // printStream.println("Crominence Quantization Table");

        // a = qTables[1].getTable();
        // b = ZigZag.decode(a);

        // }
        // TODO Auto-generated method stub

    }

    public static String getSpaces(int n) {

        StringBuffer sb = new StringBuffer(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        return sb.toString();

    }

    // If the current marker is APP0, tries to decode a JFIF extension
    // and advances the current marker to the next marker in the stream.
    private void decodeJFIFExtension() throws IOException {
        if (marker == JPEGMarker.APP0) {
            int length = jpegStream.readShort();

            if (length >= JFXX_FIXED_LENGTH) {
                byte[] identifier = new byte[5];
                jpegStream.read(identifier);
                if (identifier[0] != JPEGMarker.JFIF_J || identifier[1] != JPEGMarker.JFIF_F || identifier[2] != JPEGMarker.JFIF_X || identifier[3] != JPEGMarker.JFIF_X || identifier[4] != JPEGMarker.X00) // Not a JFXX field. Ignore it and continue.
                {
                    jpegStream.skipBytes(length - 7);
                } else {
                    byte extension_code = jpegStream.readByte();

                    switch (extension_code) {
                        case JPEGMarker.JFXX_JPEG:
                            // FIXME: add support for JFIF Extension:
                            // Thumbnail coded using JPEG.
                            jpegStream.skipBytes(length - 8);
                        case JPEGMarker.JFXX_ONE_BPP:
                            // FIXME: add support for JFIF Extension:
                            // Thumbnail stored using 1 byte/pixel.
                            jpegStream.skipBytes(length - 8);
                        case JPEGMarker.JFXX_THREE_BPP:
                            // FIXME: add support for JFIF Extension:
                            // Thumbnail stored using 3 bytes/pixel.
                            jpegStream.skipBytes(length - 8);
                    }
                }
            } else {
                // Unknown APP0 marker. Ignore it and continue.
                jpegStream.skipBytes(length - 2);
            }
            marker = jpegStream.findNextMarker();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    
//    void extract(String password, String extractoutfile) throws Exception,
//            Throwable {
//        this.isgui = false;
//        isEmbed = false;
//        this.password = password;
//        this.output2Console = false;
//        this.extractoutfile = extractoutfile;
//        this.verbose = false;
//        this.system = system;
//        this.verfilename = verfilename;
//
//        decode();
//        extractData(frame, 0, false);
//        long time1 = System.currentTimeMillis();
//
//        extractData(frame, 1, true);
//        extractData(frame, 2, true);
//        totaltime_2comp = System.currentTimeMillis() - time1;
//        ;
//
//        // TODO Auto-generated catch block
//
//    }

    
}
