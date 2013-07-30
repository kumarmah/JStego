
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mahendra Kumar
 */
public class Extract1 implements Constants {

    JPEGDecoder decoder = null;
    // private boolean isFreckle = false;
    public int[] negativeStopsRelative;
    public int[] positiveStopsRelative;
    public int oneStopsRelative;
    public int[] estimatesstops;
    public int[] actualstop;
    //public int[] tableChanged = new int[4];
    // public int[] tableRemain = new int[4];
    //public int[] tableTotal = new int[4];
    long noOfZeros = 0;
    public int TOTALBytesEmbedded[] = new int[3];
    public int TOTALZeros[] = new int[3];
    public int TOTALBlocks[] = new int[3];
    public int TotalEstimatedCapacity[] = new int[3];
    int seed = 0;
    int seed1 = 0; // for the randomization inside databits block
    MessageDigest md = null;
    private String password;
    private String inputData;
    private boolean inputFromFile = true;
    private String embedinfilename;
    private String outimageFilename;
    int bytesEmbedded = 0;
    int blocksDiscarded = 0;
    int histChangeBefore = 0;
    int histChangeAfter = 0;
    Random rand = null;
    private Random rand1;
    int totalblocks = 0;
    private int coeffchanged;
    public double efficiency = 0.0;
    int posBinremain[][];
    int negBinremain[][];
    int posEstimatedCap[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    int negEstimatedCap[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    int posRunningCap[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    int negRunningCap[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    int posBinchanged[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT - 1];
    int negBinchanged[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT - 1];
    boolean posBooleanStop[][] = new boolean[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    boolean negBooleanStop[][] = new boolean[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT / 2];
    int negOnesRunningCap = 0;
    int posOnesRunningCap = 0;
    int posOneEstimated = 0;
    int negOneEstimated = 0;
    int posOne2Zero = 0;
    int negOne2Zero = 0;
    int posOneRemain = 0;
    int negOneRemain = 0;
    boolean posOneStop = false;
    boolean negOneStop = false;
    int zero2NegOne = 0;
    int zero2PosOne = 0;

    public Extract1(JPEGDecoder decoder, String password, String embedinfilename, String outimageFilename) {
        this.decoder = decoder;
        this.password = password;
        this.embedinfilename = embedinfilename;
        this.outimageFilename = outimageFilename;
    }

    public int extractData(JPEGFrame frame, int comp222Number, int offsetForData, double BPNZ)
            throws Throwable {
        // decoder.printStream.println("*******************************************************");
        JPEGComponent comp = null;
        // for finding the positive and negative number of frekles before embed
        comp = (JPEGComponent) frame.components.get(0);
        ///Arraylist a1 =

//         ArrayList compCopy = new ArrayList();
//         for(int i=0;i<comp.data.size();i++){
//             short[] temp= (short[]) comp.data.get(i);
//             short temp2[]= new short[temp.length];
//            System.arraycopy(temp, 0, temp2, 0, 64);
//
//             compCopy.add(temp2);
//         }

        //  Collections.copy(compCopy, comp.data);
        // System.arraycopy(comp.data,0,compCopy,0, comp.data.size());



        // ***************************************************************************
        long startTime = System.currentTimeMillis();
        int totalBlocks = comp.data.size();
        short current1DJpegBlock[] = null;
        int blockIndex = 0;
        int databits[] = null;
        int datalength = 0;
        GetHistogram fr2 = new GetHistogram(decoder.width, decoder.height, "");
        fr2.setPoints(comp);
        int posHist[] = fr2.getPosHist();
        int negHist[] = fr2.getNegHist();
        TOTALZeros[0] = fr2.getZeros();
        TOTALBlocks[0] = totalBlocks;
        int COEFF_LIMIT = DUAL_HISTOGRAM_COEFF_LIMIT;
        //int numberOfStopPairs = COEFF_LIMIT / 2;
        int nonzeros1 = TOTALBlocks[0] * 64 - TOTALZeros[0];
        int datareducedlimit = (int) (nonzeros1 * BPNZ);
        //datareducedlimit=2000;
        double temp = Math.ceil(datareducedlimit / 8.0);
        datareducedlimit = (int) (temp * 8);
        //datareducedlimit += (datareducedlimit % 8);
        byte[] buffer = new byte[datareducedlimit / 8];
        Random r = new Random();
        r.nextBytes(buffer);
        datalength = buffer.length;
        databits = Utility.ConvertData2Bits(buffer, buffer.length);

        setSeed();
        rand = new Random(seed);
        decoder.printStream.println("*******  Input Image is = " + decoder.inputImageFile);
        decoder.printStream.println("*******  Output image is = " + outimageFilename);
        decoder.printStream.println("*******  Password is = " + password);
        decoder.printStream.println("*******  Input Data length is = " + datalength * 8 + "  bits");


        //starts at 2... 0 1 not included
        posBinremain = fr2.getPosDualBin();
        negBinremain = fr2.getNegDualBin();
        int initialPos[][]=new int[posBinremain.length][posBinremain[0].length];
        int initialNeg[][]=new int[negBinremain.length][negBinremain[0].length];
        for (int i = 0; i < posBinremain.length; i += 1) {
            for (int j = 0; j < posBinremain[0].length; j += 1) {
            initialPos[i][j]=posBinremain[i][j];
            initialNeg[i][j]=negBinremain[i][j];
            }
            }

//        int initialZeros=TOTALZeros[0];
//        int initialPosOnes= fr2.totalPosOnes;
//        int initialNegOnes= fr2.totalNegOnes;
//        int zeroRemain = initialZeros;
//        posOneRemain = initialPosOnes;
//        negOneRemain = initialNegOnes;

         int initialZeros=TOTALZeros[0];
        int initialPosOnes= fr2.totalPosOnes;
        int initialNegOnes= fr2.totalNegOnes;
        int zeroRemain = fr2.zeroUnderSize;
        posOneRemain = posHist[0];
        negOneRemain = negHist[0];

        //   int[][] posBin1 = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT];
        //  int[][] negBin1 = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT];
        int m0 = 0;
        int m1 = 0;
        for (int i = 0; i < databits.length; i++) {
            if (databits[i] == 0) {
                m0++;
            } else {
                m1++;
            }

        }
        double p0 = (double) m0 / databits.length;
        double p1 = (double) m1 / databits.length;
        double cap1 = 0;
        double cap2 = 0;
        long estimated = 0;

        int totalMsgBits= datareducedlimit;
        int k=0;
        for(k=1;k<20;k++){
            double sum=0;
             int n=  (int) (Math.pow(2, k) - 1);
        for (int i = 0; i < posBinremain.length; i += 1) {
            for (int j = 0; j < posBinremain[0].length; j += 2) {
               int even= (posBinremain[i][j]);
                int odd= posBinremain[i][j + 1];
                double top=0;
                double bottom=0;
                //for positive values
                if(even<odd){
                  top= (n+1)*even*(odd+even);
                 bottom= odd+n*even;
                if(bottom!=0)
                    sum+= top/bottom;
                }else {
                 top= (n+1)*odd*(odd+even);
                 bottom= even+n*odd;
                if(bottom!=0)
                    sum+= top/bottom;
                }
                //for negative values
                 even= negBinremain[i][j];
                 odd= negBinremain[i][j + 1];
                if(even<odd){
                  top= (n+1)*even*(odd+even);
                 bottom= odd+n*even;
                 if(bottom!=0)
                sum+= top/bottom;
                }else {
                 top= (n+1)*odd*(odd+even);
                 bottom= even+n*odd;
                 if(bottom!=0)
                sum+= top/bottom;
                }

            } //end 1st for


        }
          sum+=(posOneRemain+negOneRemain+zeroRemain);
            double val1= sum/totalMsgBits;
            double val2= n/k;
            if(val1<val2)
                break;



        }//end fo ladst for

        k=k-1;
        System.out.println("value of k is "+(k) );


       int n=  (int) (Math.pow(2, k) - 1);
for (int i = 0; i < posBinremain.length; i += 1) {
            for (int j = 0; j < posBinremain[0].length; j += 2) {
               int even= (posBinremain[i][j]);
                int odd= posBinremain[i][j + 1];
                double top=0;
                double bottom=0;
                double poscap=0;
                 double negcap=0;
                //for positive values
                if(even<odd){
                  top= (n+1)*even*(odd+even);
                 bottom= odd+n*even;
                if(bottom!=0)
                    poscap= top/bottom;
                }else {
                 top= (n+1)*odd*(odd+even);
                 bottom= even+n*odd;
                if(bottom!=0)
                    poscap= top/bottom;
                }
                //for negative values
                 even= negBinremain[i][j];
                 odd= negBinremain[i][j + 1];
                if(even<odd){
                  top= (n+1)*even*(odd+even);
                 bottom= odd+n*even;
                 if(bottom!=0)
                negcap= top/bottom;
                }else {
                 top= (n+1)*odd*(odd+even);
                 bottom= even+n*odd;
                 if(bottom!=0)
                negcap= top/bottom;
                }

                posEstimatedCap[i][j / 2] = (int) Math.floor(poscap);
                negEstimatedCap[i][j / 2] = (int) Math.floor(negcap);

            } //end 1st for


        }









       ////// old estimation

//        for (int i = 0; i < posBinremain.length; i += 1) {
//            for (int j = 0; j < posBinremain[0].length; j += 2) {
//                cap1 += (posBinremain[i][j] + negBinremain[i][j]);
//                cap2 += (posBinremain[i][j + 1] + negBinremain[i][j + 1]);
//                //  posBin1[i][j] = posBinremain[i][j];
//                //  negBin1[i][j] = negBinremain[i][j];
//                //  posBin1[i][j + 1] = posBinremain[i][j + 1];
//                // negBin1[i][j + 1] = negBinremain[i][j + 1];
//                double c1 = (posBinremain[i][j] / p1 < posBinremain[i][j + 1] / p0) ? posBinremain[i][j] / p1 : posBinremain[i][j + 1] / p0;
//                double c2 = (negBinremain[i][j] / p1 < negBinremain[i][j + 1] / p0) ? negBinremain[i][j] / p1 : negBinremain[i][j + 1] / p0;
//
//                posEstimatedCap[i][j / 2] = (int) Math.floor(c1 * 0.9);
//                negEstimatedCap[i][j / 2] = (int) Math.floor(c2 * 0.9);
//                estimated += (posEstimatedCap[i][j / 2] + negEstimatedCap[i][j / 2]);
//            }
//
//        }

        posOneEstimated = posOneRemain;
        negOneEstimated = negOneRemain;
        //estimated += (posOneEstimated + negOneEstimated + zeroRemain);






        double limit = cap1 + cap2;
        cap1 /= p0;
        cap2 /= p1;
        if (cap1 > limit) {
            TotalEstimatedCapacity[0] = (int) cap2 / 8;
        } else if (cap2 >= limit) {
            TotalEstimatedCapacity[0] = (int) cap1 / 8;
        }

        int code_k = k;

//                if(databits.length < datareducedlimit){

 //        int templength = datareducedlimit + totalHeaderLength + 30;
        int templength = datareducedlimit;
//        double factor = (templength * 100.0) / (double) estimated;
//        System.out.println("factor=" + factor);
//        if (factor > 66.67) {
//            code_k = 1;
//        } else if (factor > 42.86) {
//            code_k = 2;
//        } else if (factor > 26.67) {
//            code_k = 3;
//        } else if (factor > 16.13) {
//            code_k = 4;
//        } else if (factor > 9.52) {
//            code_k = 5;
//        } else if (factor > 5.51) {
//            code_k = 6;
//        } else if (factor > 3.14) {
//            code_k = 7;
//        } else if (factor > 1.76) {
//            code_k = 8;
//        } else {
//            code_k = 9;
//        }

       //  code_k=k;
        int codelength = (int) Math.pow(2, code_k) - 1;


        System.out.println("codelength=" + codelength + "     code_k=" + code_k);

        int indices[] = new int[codelength];
        int values[] = new int[codelength];

        decoder.printStream.println("******************************************");

        decoder.printStream.println("Total no. of blocks available:" + totalBlocks);
        decoder.printStream.println("number of bits to encode:" + templength);
        //int bitsAvail = databits.length;
        int dataIndex = 0;
        coeffchanged = 0;
        int nextNumber = 0;
        byte uniqueBitmap[] = new byte[totalBlocks * 64];
        uniqueBitmap[0] = NOT_USUABLE_BIT;
        int coeffIndex = 0;
        int cc = 0;
//        int negativeStops[] = new int[numberOfStopPairs];
//        int positiveStops[] = new int[numberOfStopPairs];
//        // int oneStops = 0;
//
//        negativeStopsRelative = new int[numberOfStopPairs];
//        positiveStopsRelative = new int[numberOfStopPairs];
//        oneStopsRelative = 0;


        //******************************** date May 19, 2009 *********
        // FIX databits major bug with continue inside while loop.....
        //**********************************************88
        //int stopCounter = COEFF_LIMIT;
        int actualBytesOfDataEncoded = datalength;
        int zerocounter = 0;
        datareducedlimit = datareducedlimit - (datareducedlimit % code_k);
        //while (dataIndex < databits.length) {
        while (dataIndex < datareducedlimit) {
            //if (stopCounter == 0 && dataIndex < databits.length) {
            //stopCounter = COEFF_LIMIT;
            boolean flag = false;
            for (int i = 0; i < posBooleanStop.length; i++) {
                for (int j = 0; j < posBooleanStop[0].length; j++) {

                    if (posRunningCap[i][j] + 1 > posEstimatedCap[i][j] && !posBooleanStop[i][j]) {
                        posBooleanStop[i][j] = true;
                    }


                    if (negRunningCap[i][j] + 1 > negEstimatedCap[i][j] && !negBooleanStop[i][j]) {
                        negBooleanStop[i][j] = true;
                    }
                    if (negOnesRunningCap + 1 > negOneEstimated && !negOneStop) {
                        negOneStop = true;
                    }

                    if (posOnesRunningCap + 1 > posOneEstimated && !posOneStop) {
                        posOneStop = true;
                    }

                    if (!posBooleanStop[i][j] || !negBooleanStop[i][j] || !posOneStop || !negOneStop) {
                        flag = true;
                    }

                }
            }

            if (!flag) {
                decoder.printStream.println("Whole data cannot be encoded....Encoding whatever can be encoded");
                actualBytesOfDataEncoded = dataIndex / 8;
                return -5;
                // break;
            }

//            if (stopCounter == 0 && dataIndex < datareducedlimit) {
//
//                decoder.printStream.println("Whole data cannot be encoded....Encoding whatever can be encoded");
//                actualBytesOfDataEncoded = dataIndex / 8;
//                return -5;
//            }
            //check for enough remaminng before getting the next set of indices..be passimistic in approach


            indices = getNextIndices(rand, totalBlocks, uniqueBitmap, comp, COEFF_LIMIT, codelength);
            int dataBlock[] = new int[code_k];

            for (int i = 0; i < code_k; i++) {
                dataBlock[i] = databits[dataIndex++];
            }
            int indexToChange = getIndexTochange(indices, dataBlock, codelength, code_k, comp);
            if (indexToChange < 0) {
                continue;
            }
            coeffIndex = indexToChange % 64; // row number of the block
            blockIndex = indexToChange / 64; // block number
            current1DJpegBlock = (short[]) comp.data.get(blockIndex);
            int number = (int) current1DJpegBlock[coeffIndex];
            if (number < -1) {
                int number1 = -number;
                negBinchanged[coeffIndex - 1][number1 - 2] += 1;
                if (number1 % 2 == 0) {
                    current1DJpegBlock[coeffIndex] -= 1;
                } else {
                    current1DJpegBlock[coeffIndex] += 1;
                }
            } else if (number > 1) {
                posBinchanged[coeffIndex - 1][number - 2] += 1;
                if (number % 2 == 0) {
                    current1DJpegBlock[coeffIndex] += 1;
                } else {
                    current1DJpegBlock[coeffIndex] -= 1;
                }

            } else if (number == 0) {
                int posBal = zero2PosOne - posOne2Zero;
                int negBal = zero2NegOne - negOne2Zero;
                //if(posBal>=0 && negBal>=0){
                if (posBal < negBal) {

                    current1DJpegBlock[coeffIndex] += 1;
                    zero2PosOne++;
                } else {
                    current1DJpegBlock[coeffIndex] -= 1;
                    zero2NegOne++;
                }
                //  }else

//                    if(posOne2Zero > negOne2Zero){
//                        current1DJpegBlock[coeffIndex] += 1;
//                        zero2PosOne++;
//                    }else {
//                        current1DJpegBlock[coeffIndex] -= 1;
//                        zero2NegOne++;
//                    }
                // zerocounter++;


            } else if (number == 1) {
                posOne2Zero++;
                current1DJpegBlock[coeffIndex] -= 1;
            } else if (number == -1) {
                negOne2Zero++;
                current1DJpegBlock[coeffIndex] += 1;
            }



            /////////////////////////////////////////////////////////////////////////////////////////////////////
            //end of for loop
            //            if (stopCounter == 0 && dataIndex < databits.length) {
            //                decoder.printStream.println("Whole data cannot be encoded....Encoding whatever can be encoded");
            //                actualBytesOfDataEncoded = dataIndex / 8;
            //                break;
            //            }

            // decoder.printStream.println("data encoded = "+dataIndex);
        } // end of while loop

//        if (stopCounter != 0) {
//            actualBytesOfDataEncoded = dataIndex / 8;
//        }


        ///////////////////////////////////////////////////////////////////////////////////////////

        //total number of changes
        int coefChanged = 0;
        for (int i = 0; i < posBinchanged.length; i++) {
            for (int j = 0; j < posBinchanged[0].length; j++) {
                coefChanged += posBinchanged[i][j];
                coefChanged += negBinchanged[i][j];
            }
        }

//        if (postablechanged != null && postablechanged.length > 0) {
//            tableChanged[2] = posBinChanged[coeffIndex-1][0];
//        }
//        if (postablechanged.length > 1) {
//            tableChanged[3] = posBinChanged[coeffIndex-1][1];
//        }
//        tableRemain[0] = negOneRemain;
//        tableRemain[1] = posOneRemain;
//        if (postableremain != null && postableremain.length > 0) {
//            tableRemain[2] = posBinremain[coeffIndex-1][0];
//        }
//        if (postableremain.length > 1) {
//            tableRemain[3] = posBinremain[coeffIndex-1][1];
//        }

        decoder.printStream.println("*******  Compensating for Histogram imbalance");
        int count = 0;
        //calculate the absolute number of changes in each pair

        for (int row = 0; row < posBinchanged.length; row += 1) {
            for (int col = 0; col < posBinchanged[0].length; col += 2) {

                if (posBinchanged[row][col] >= posBinchanged[row][col + 1]) {
                    posBinchanged[row][col + 1] = posBinchanged[row][col] - posBinchanged[row][col + 1];
                    posBinchanged[row][col] = 0;
                } else {
                    posBinchanged[row][col] = posBinchanged[row][col + 1] - posBinchanged[row][col];
                    posBinchanged[row][col + 1] = 0;
                }

                if (negBinchanged[row][col] >= negBinchanged[row][col + 1]) {
                    negBinchanged[row][col + 1] = negBinchanged[row][col] - negBinchanged[row][col + 1];
                    negBinchanged[row][col] = 0;
                } else {
                    negBinchanged[row][col] = negBinchanged[row][col + 1] - negBinchanged[row][col];
                    negBinchanged[row][col + 1] = 0;
                }

                count += (posBinchanged[row][col] + posBinchanged[row][col + 1] + negBinchanged[row][col] + negBinchanged[row][col + 1]);
            }
        }

        ///compensate for each colum of histograms

//        GetHistogram fr4 = new GetHistogram(decoder.width, decoder.height, "");
//        fr4.setPoints(comp);
//        int test1[][]=fr4.getPosDualBin();
//        int test2[][]=fr4.getNegDualBin();
//        for(int i=0;i<posBinremain.length;i++){
//    for(int j=0;j<posBinremain[0].length;j++){
//        System.out.print(test1[i][j]+"   ");
//    }
//  System.out.println("");
//for(int j=0;j<negBinremain[0].length;j++){
//        System.out.print(test2[i][j]+"   ");
//    }
//     System.out.println("\n*******");
//
//}


        int posCheckBalance[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT - 1];
        int negCheckBalance[][] = new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT - 1];
        for (int i = 0; i < totalBlocks * 64; i++) {
            if (uniqueBitmap[i] == UNUSED_BIT || uniqueBitmap[i] == REUSUABLE_BIT) {

                coeffIndex = i % 64; // row number of the block
                blockIndex = i / 64; // block number
                if (coeffIndex > DUAL_HISTOGRAM_SIZE || coeffIndex == 0) {
                    continue;
                }
                current1DJpegBlock = (short[]) comp.data.get(blockIndex);

                int number = (int) current1DJpegBlock[coeffIndex];
                if (number == 0 || number == -1 || number == 1) {
                    continue;
                }
                if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {

                    if (number > 0) {
                        posCheckBalance[coeffIndex - 1][number - 2]++;
                    } else if (number < 0) {
                        negCheckBalance[coeffIndex - 1][(number * -1) - 2]++;
                    }
                }

            }
        }
        int imbalance = 0;

        for (int i = 0; i < posBinchanged.length; i++) {
            for (int j = 0; j < posBinchanged[0].length; j++) {
                if (posCheckBalance[i][j] < posBinchanged[i][j]) {
                    decoder.printStream.println(" Warning !! Warning !! Warning !! Warning");
                    decoder.printStream.println("Will not be able to balance histogram for component " + 0);
                    decoder.printStream.println("Histogram imbalance will be in " + (i + 1) + "th  coefficient by " + (posBinchanged[i][j] - posCheckBalance[i][j]));
                    imbalance += (posBinchanged[i][j] - posCheckBalance[i][j]);
                    return -5;

                }
                if (negCheckBalance[i][j] < negBinchanged[i][j]) {
                    decoder.printStream.println(" Warning !! Warning !! Warning !! Warning");
                    decoder.printStream.println("Will not be able to balance histogram for component " + 0);
                    decoder.printStream.println("Histogram imbalance will be in -" + (i + 1) + "th  coefficient by " + (negBinchanged[i][j] - negCheckBalance[i][j]));
                    imbalance += (negBinchanged[i][j] - negCheckBalance[i][j]);
                    return -5;

                }
            }
        }
        coefChanged += count;
        coefChanged+=(posOne2Zero+negOne2Zero+zero2NegOne+zero2PosOne);

        System.out.println("Total number of coeff changes=" + coefChanged);
        int bitencoded = (actualBytesOfDataEncoded) * 8;
        efficiency = bitencoded / (double) coefChanged;



        while (count > imbalance) {
            // decoder.printStream.println(count);
            nextNumber = getNextNumberForBalance(rand, totalBlocks,
                    uniqueBitmap, comp);
            uniqueBitmap[nextNumber] = PENDING_BIT;
            coeffIndex = nextNumber % 64; // row number of the block
            blockIndex = nextNumber / 64; // block number

            if (coeffIndex > DUAL_HISTOGRAM_SIZE || coeffIndex == 0) {
                uniqueBitmap[nextNumber] = NOT_USUABLE_BIT;
                continue;
            }
            current1DJpegBlock = (short[]) comp.data.get(blockIndex);

            int number = (int) current1DJpegBlock[coeffIndex];

            if (number == 0 || number == 1 || number == -1) {
                uniqueBitmap[nextNumber] = NOT_USUABLE_BIT;
                continue;
            }
            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
                if (number < 0) {
                    int number1 = number * -1;
                    if (number1 % 2 == 0 && negBinchanged[coeffIndex - 1][number1 - 2] > 0) {
                        current1DJpegBlock[coeffIndex] -= 1;
                        negBinchanged[coeffIndex - 1][number1 - 2] -= 1;
                        uniqueBitmap[nextNumber] = USED_BIT_FOR_BALANCE;

                        count--;
                    } else if (number1 % 2 == 1 && negBinchanged[coeffIndex - 1][number1 - 2] > 0) {
                        current1DJpegBlock[coeffIndex] += 1;
                        negBinchanged[coeffIndex - 1][number1 - 2] -= 1;
                        uniqueBitmap[nextNumber] = USED_BIT_FOR_BALANCE;
                        count--;
                    }
                } else if (number > 0) {
                    if (number % 2 == 0 && posBinchanged[coeffIndex - 1][number - 2] > 0) {
                        current1DJpegBlock[coeffIndex] += 1;
                        posBinchanged[coeffIndex - 1][number - 2] -= 1;
                        uniqueBitmap[nextNumber] = USED_BIT_FOR_BALANCE;
                        count--;

                    } else if (number % 2 == 1 && posBinchanged[coeffIndex - 1][number - 2] > 0) {
                        current1DJpegBlock[coeffIndex] -= 1;
                        posBinchanged[coeffIndex - 1][number - 2] -= 1;
                        uniqueBitmap[nextNumber] = USED_BIT_FOR_BALANCE;
                        count--;
                    }
                }

            }
        }// end of while


        GetHistogram fr5 = new GetHistogram(decoder.width, decoder.height, "");
        fr5.setPoints(comp);
        int finalPos[][] = fr5.getPosDualBin();
        int finalNeg[][] = fr5.getNegDualBin();

//
       for(int i=0;i<posBinremain.length;i++){
    for(int j=0;j<posBinremain[0].length;j++){
      if(initialPos[i][j]!=finalPos[i][j]){
          System.out.println(initialPos[i][j]+"    "+finalPos[i][j]);
          return -5;
      }
      if(initialNeg[i][j]!=finalNeg[i][j]){
          System.out.println(initialPos[i][j]+"    "+finalPos[i][j]);
          return -5;
      }
    }
    }

       FileWriter fw1 = new FileWriter("efficiency.txt", true);
            fw1.write(decoder.inputImageFile + "," +BPNZ + "," +code_k +"," +initialPosOnes +","
                    +fr5.totalPosOnes +"," +initialNegOnes +"," +fr5.totalNegOnes +","
                    +initialZeros+"," +fr5.getZeros() +","+efficiency+
                    "," +posOne2Zero +"," +zero2PosOne +"," +negOne2Zero +"," +zero2NegOne +"\n");
            fw1.close();

//
//        System.out.print(test3[i][j]+"   ");
//    }
//    System.out.println("");
//for(int j=0;j<negBinremain[0].length;j++){
//        System.out.print(test4[i][j]+"   ");
//    }
//     System.out.println("\n*******");
//
//}



//        for(int i=0;i<posBinremain.length;i++){
//    for(int j=0;j<posBinremain[0].length;j++){
//         int diff= Math.abs(test3[i][j]- posBin1[i][j]);
//        if(diff > 0 )
//        System.out.print(diff+"   ");
//    }
//    System.out.println("\n**************************************************");
//        }
//       for(int i=0;i<comp.data.size();i++){
//     short[] a1 = (short[]) comp.data.get(i);
//     short[] b1= (short[]) compCopy.get(i);
//    // a1[0]=200;
//      boolean b = java.util.Arrays.equals(a1, b1);
//
//        if(!b){
//        System.out.print("here   ");
//     for(int i1=0;i1<64;i1++){
//         System.out.print((a1[i1]-b1[i1])+"   ");
//     }
//
//      System.out.println("");
//           }
//    }
//
//
//    System.out.println("\n**************************************************");


        //for(int j=0;j<negBinremain[0].length;j++){
//        System.out.print(test4[i][j]+"   ");
//    }
        // System.out.println("\n*******");


        // + round((endTime - startTime), 2));
        File f = new File(decoder.inputImageFile);
        decoder.printStream.println("****** Avergare number of Bits embedded per coeff=" + (double) actualBytesOfDataEncoded / (totalBlocks * 8));
        decoder.printStream.println("****** data to file size ratio=" + (double) actualBytesOfDataEncoded / f.length());
        decoder.printStream.println("****** actual bytes encoded=" + (double) actualBytesOfDataEncoded);
        decoder.printStream.println("****** data bytes requested=" + (double) datareducedlimit / 8);
        decoder.printStream.println("****** zero changed to +1=" + zero2PosOne);
        decoder.printStream.println("****** zero changed to -1=" + zero2NegOne);
        decoder.printStream.println("****** +1 changed to 0=" + posOne2Zero);
        decoder.printStream.println("****** -1 changed to 0=" + negOne2Zero);

        int posbal = posOne2Zero - zero2PosOne;
        int negbal = negOne2Zero - zero2NegOne;
        int bal = Math.abs(posbal - negbal);

        if (bal > 200) {
            FileWriter fw = new FileWriter("baldiff.txt", true);
            fw.write(embedinfilename + "," + bal + "\n");
            fw.close();
        }
        setBytesEmbedded(actualBytesOfDataEncoded);
        setTotalBlocks(totalBlocks);
        decoder.printStream.println("$$$$$$$$$$$$$$$$$$$ EMBEDDING PROCESS COMPLETE for component number " + 0);
        TOTALBytesEmbedded[0] = actualBytesOfDataEncoded;
        // probability estimation
        m0 = 0;
        m1 = 0;
        for (int i = 0; i < databits.length; i++) {
            if (databits[i] == 0) {
                m0++;
            } else {
                m1++;
            }

        }
        p0 = (double) m0 / databits.length;
        p1 = (double) m1 / databits.length;
        cap1 = 0;
        cap2 = 0;
        if (posHist.length > 1) {
            for (int i = 1; i <= COEFF_LIMIT; i += 2) {
                cap1 += (posHist[i] + negHist[i]);
                cap2 += (posHist[i + 1] + negHist[i + 1]);

            }
        }
        cap1 += negHist[0];
        cap2 += posHist[0];
        limit = cap1 + cap2;
        //  estimatesstops = new int[numberOfStopPairs];
        // actualstop = new int[numberOfStopPairs];
        //for (-1,1) pair
        double pc0 = negHist[0] / (double) (limit);
        double pc1 = posHist[0] / (double) (limit);

//        int temp1 = (int) (posHist[0] / (p1 * (pc0 + pc1)));
//        int temp2 = (int) (negHist[0] / (p0 * (pc0 + pc1)));
//        if (temp1 < temp2) {
//            estimatesstops[0] = temp1;
//        } else {
//            estimatesstops[0] = temp2;
//        }
//
//        actualstop[0] = oneStopsRelative;
//        for (int i = 1; i < numberOfStopPairs; i++) {
//            pc0 = posHist[(i) * 2 - 1] / (double) (limit);
//            pc1 = posHist[(i) * 2] / (double) (limit);
//
//            temp1 = (int) (posHist[(i) * 2] / (p1 * (pc0 + pc1)));
//            temp2 = (int) (posHist[(i) * 2 - 1] / (p0 * (pc0 + pc1)));
//            if (temp1 < temp2) {
//                estimatesstops[i] = temp1;
//            } else {
//                estimatesstops[i] = temp2;
//            }
//
//            actualstop[i] = positiveStopsRelative[i - 1];
//        }

        cap1 /= p0;
        cap2 /= p1;
        if (cap1 > limit) {
            TotalEstimatedCapacity[0] = (int) cap2 / 8;
        } else if (cap2 >= limit) {
            TotalEstimatedCapacity[0] = (int) cap1 / 8;
        }

        return actualBytesOfDataEncoded;
    }

    void setSeed() {
        if (decoder.verbose) {
            decoder.printStream.println("Password is: " + password);
        }
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();
            if (b[0] < 0) {
                seed = (256 + b[0]);
            } else {
                seed = b[0];
            }
            if (b[1] < 0) {
                seed1 = (256 + b[1]);
            } else {
                seed1 = b[1];
            }

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void encodeJPEG(JPEGFrame frame) {
        try {
            decoder.printStream.println("*******  Starting JPEG Encoder");
            decoder.je.main(decoder.inputImageFile, outimageFilename, frame);
            decoder.printStream.println("*******  File Encoded as  " + outimageFilename);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private int getIndexTochange(int[] indices, int[] dataBlock, int codelength, int code_n, JPEGComponent comp) {
        int values[] = new int[codelength];
        for (int i = 0; i < codelength; i++) {
            int coeffindex = indices[i] % 64;
            int blockIndex = indices[i] / 64; // block number
            short[] currentBlock = (short[]) comp.data.get(blockIndex);
            int number = (int) currentBlock[coeffindex];
            if (number == -1) {
                values[i] = 0;
            } else if (number == 1) {
                values[i] = 1;
            } else if (number % 2 == 0) {
                values[i] = 0;
            } else {
                values[i] = 1;
            }


        }
        //get the XOR of the numbers in the given code

        int cols = (int) Math.pow(2, code_n - 1);
        int rows = code_n;
//        int code[]=generateCode(0, cols);
//         code=generateCode(1, cols);
//         code=generateCode(2, cols);
//         code=generateCode(73, cols);
//         code=generateCode(4, cols);

        int temp[] = new int[code_n];
        for (int i = 0; i < rows; i++) {
            int dynamicHammingCode[] = generateCode(i, cols);
            for (int j = 0; j < cols; j++) {
                temp[i] += values[dynamicHammingCode[j] - 1];
                //temp[i]+=values[CODES[i][j]-1];
            }
            if (temp[i] % 2 != 0) {
                temp[i] = 1;
            } else {
                temp[i] = 1;
            }
        }
        //XOR the result with the data code to find the position to change

        for (int i = 0; i < code_n; i++) {
            int a = dataBlock[i] + temp[i];
            if (a == 1) {
                temp[i] = 1;
            } else {
                temp[i] = 0;
            }
        }
        int pos = Utility.changeBitsToValue(temp, code_n);
        if (pos == 0) {
            return -1;
        } else {
            return indices[pos - 1];
        }
    }

    private int getNextNumberForBalance(Random r1, int totalBlocks,
            byte uniqueBit[], JPEGComponent comp) {
        // int i = 0;
        //
        // for (i = 0; i < uniqueBit.length; i++) {
        // if (uniqueBit[i] == 0) {
        // break;
        // }
        //
        // }
        // if (i == uniqueBit.length){
        // printStream.println("ERROR******************************");
        // System.exit(0);
        // }
        while (true) {
            int a = r1.nextInt(totalBlocks * 64);
            if (uniqueBit[a] == UNUSED_BIT || uniqueBit[a] == REUSUABLE_BIT) {
                int coeffindex = a % 64;
                if (coeffindex == 0) {
                    uniqueBit[a] = NOT_USUABLE_BIT;
                    continue;
                }
                return a;

            }
        }
    }

    private int[] getNextIndices(Random r1, int totalBlocks, byte uniqueBit[],
            JPEGComponent comp, int coeffLimit, int codelength) {
        int length = 0;
        int indices[] = new int[codelength];
        while (true) {
            //System.out.println("here");
            int a = r1.nextInt(totalBlocks * 64);
            if (uniqueBit[a] == UNUSED_BIT) {
                //System.out.println(databits);
                int coeffindex = a % 64;
                int blockIndex = a / 64; // block number
                 short[] currentBlock = (short[]) comp.data.get(blockIndex);
                int number = (int) currentBlock[coeffindex];
                if (number == 1 && coeffindex != 0) {
                    if (posOneStop) {
                        uniqueBit[a] = REUSUABLE_BIT;
                        continue;
                    } else {
                        indices[length++] = a;
                        uniqueBit[a] = USED_BIT_FOR_DATA;
                        posOneRemain--;
                        posOnesRunningCap++;
                    }
                } else if (number == -1 && coeffindex != 0) {
                    if (negOneStop) {
                        uniqueBit[a] = REUSUABLE_BIT;
                        continue;
                    } else {
                        indices[length++] = a;
                        uniqueBit[a] = USED_BIT_FOR_DATA;
                        negOneRemain--;
                        negOnesRunningCap++;
                    }
                } else if (number == 0 && coeffindex != 0) {
                    indices[length++] = a;
                    uniqueBit[a] = USED_BIT_FOR_DATA;
                }
                if (length == codelength) {
                    return indices;
                }

                if (coeffindex == 0 || coeffindex > DUAL_HISTOGRAM_SIZE) {
                    uniqueBit[a] = NOT_USUABLE_BIT;
                    continue;
                }
               
//                if (number == 0) {
//                    uniqueBit[a] = ZERO_VALUE_BIT;
//                    continue;
//                } else
//
                if (number < -coeffLimit || number > coeffLimit) {
                    uniqueBit[a] = NOT_USUABLE_BIT;
                    continue;
                } else if (number >= -coeffLimit && number <= -2) {
                    int number1 = -number;
//                    if (number1 % 2 != 0 && negativeStops[coeffindex - 1][number1 / 2]) {
//                        uniqueBit[a] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (number1 % 2 == 0 && negativeStops[coeffindex - 1][number1 / 2 - 1]) {
//                        uniqueBit[a] = REUSUABLE_BIT;
//                        continue;
//                    }
                    if (negBooleanStop[coeffindex - 1][number1 / 2 - 1]) {
                        uniqueBit[a] = REUSUABLE_BIT;
                        continue;
                    } else {
                        indices[length++] = a;
                        uniqueBit[a] = USED_BIT_FOR_DATA;
                        negBinremain[coeffindex - 1][number1 - 2]--;
                        negRunningCap[coeffindex - 1][number1 / 2 - 1]++;
                    }

                } else if (number <= coeffLimit && number >= 2) {
//                    if (number % 2 != 0 && positiveStops[coeffindex - 1][number / 2]) {
//                        // this is the culprit because the bitmap bit was set
//                        // even
//                        // if unused by the program, so we have to undo the bit
//                        // set
//                        // on the bitmap if not used
//                        uniqueBit[number] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (number % 2 == 0 && positiveStops[coeffindex - 1][number / 2 - 1]) {
//                        uniqueBit[a] = REUSUABLE_BIT;
//                        continue;
//                    }

                    if (posBooleanStop[coeffindex - 1][number / 2 - 1]) {
                        uniqueBit[a] = REUSUABLE_BIT;
                        continue;
                    } else {
                        indices[length++] = a;
                        uniqueBit[a] = USED_BIT_FOR_DATA;
                        posBinremain[coeffindex - 1][number - 2]--;
                        posRunningCap[coeffindex - 1][number / 2 - 1]++;
                    }

                } 




                //indices[length++] = a;
                //uniqueBit[a] = USED_BIT_FOR_DATA;
                if (length == codelength) {
                    return indices;
                }




            }
        }
    }

//    private int getNextIndex(Random r1, int totalBlocks, byte uniqueBit[],
//            JPEGComponent comp, int coeffLimit) {
//        while (true) {
//            //System.out.println("here");
//            int a = r1.nextInt(totalBlocks * 64);
//            if (uniqueBit[a] == UNUSED_BIT) {
//                //System.out.println(databits);
//                int coeffindex = a % 64;
//                int blockIndex = a / 64; // block number
//
//                if (coeffindex == 0) {
//                    uniqueBit[a] = NOT_USUABLE_BIT;
//                    continue;
//                }
//                short[] currentBlock = (short[]) comp.data.get(blockIndex);
//                int number = (int) currentBlock[coeffindex];
//                if (number == 0) {
//                    uniqueBit[a] = ZERO_VALUE_BIT;
//                    continue;
//                }
//
//                if (number < -coeffLimit || number > coeffLimit) {
//                    uniqueBit[a] = NOT_USUABLE_BIT;
//                    continue;
//                }
//
//
//
//                return a;
//
//            }
//        }
//    }
//    private int[] getNextIndicesForHeader(Random r1, int totalBlocks, byte uniqueBit[],
//            JPEGComponent comp, int coeffLimit, int codelength) {
////accept just 1 and -1 values
//        int length = 0;
//        int indices[] = new int[codelength];
//        while (true) {
//            //System.out.println("here");
//            int a = r1.nextInt(totalBlocks * 64);
//            if (uniqueBit[a] == UNUSED_BIT) {
//                //System.out.println(databits);
//                int coeffindex = a % 64;
//                int blockIndex = a / 64; // block number
//
//                if (coeffindex == 0) {
//                    uniqueBit[a] = NOT_USUABLE_BIT;
//                    continue;
//                }
//                short[] currentBlock = (short[]) comp.data.get(blockIndex);
//                int number = (int) currentBlock[coeffindex];
//                if (number == 0) {
//                    uniqueBit[a] = ZERO_VALUE_BIT;
//                    continue;
//                } else if (number < -coeffLimit || number > coeffLimit) {
//                    uniqueBit[a] = NOT_USUABLE_BIT;
//                    continue;
//                }
//                if (number == -1 || number == 1) {
//                    indices[length++] = a;
//                    uniqueBit[a] = USED_BIT_FOR_HEADER;
//                }
//                if (length == codelength) {
//                    return indices;
//                }
//
//
//
//
//            }
//        }
//    }
//    private int getNextNumberForHeader(Random r1, int totalBlocks,
//            byte uniqueBit[], JPEGComponent comp) {
//        int i = 0;
//        while (true) {
//            int a = r1.nextInt(totalBlocks * 64);
//            if (uniqueBit[a] == USED_BIT_FOR_HEADER) {
//                return a;
//            }
//
//        }
//    }
    private int[] generateCode(int row, int col) {
        int startCode = (int) Math.pow(2, row);
        int res[] = new int[col];
        int k = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < startCode; j++) {
                res[i] = startCode + k;
                i++;
                k++;
            }
            i--;
            for (int j = 0; j < startCode; j++) {
                k++;

            }


        }
        return res;
    }

//    private int getNextNumber1(Random r1, int totalBlocks, int uniqueBit[]) {
//        // System.out.println("hello1");
//        int i = 0;
//        for (i = 0; i < uniqueBit.length; i++) {
//            if (uniqueBit[i] == 0) {
//                break;
//            }
//
//        }
//        if (i >= uniqueBit.length) {
//            return -1;
//        }
//        int a = r1.nextInt(totalBlocks);
//        while (uniqueBit[a] != 0) {
//            a = r1.nextInt(totalBlocks);
//        }
//        uniqueBit[a] = 2;
//        return a;
//    }
    void setBytesEmbedded(int x) {
        this.bytesEmbedded = x;

    }

    void setTotalBlocks(int x) {
        this.totalblocks = x;

    }

    void setBlocksDiscarded(int x) {
        this.blocksDiscarded = x;
    }

    void setHistChangesBefore(int x) {
        this.histChangeBefore = x;
    }

    void setHistChangesAfter(int x) {
        this.histChangeAfter = x;
    }

    void setZeros(int x) {
        this.noOfZeros = x;
    }
}
