
import java.io.FileOutputStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mahendra Kumar
 */
public class Extract implements Constants {
// JPEGDecoder decoder=null;   
//    public Extract( JPEGDecoder decoder) {
//    this.decoder=decoder;
//    }
//    
//    private void extractData(JPEGFrame frame, int compNumber, boolean append)
//            throws Exception {
//        // get the Y component
//       
//        decoder.printStream.println("************  Extracting of Data Started  for component " + compNumber);
//
//        FileOutputStream fout = null;
//        JPEGComponent comp = (JPEGComponent) frame.components.get(compNumber);
//        FreckleAnalysis fr2 = new FreckleAnalysis(decoder.width, decoder.height, "");
//        fr2.setPoints(comp);
//        int posHist[] = fr2.getPosHist();
//        int negHist[] = fr2.getNegHist();
//        int noOfZeros = fr2.getZeros();
//        int COEFF_LIMIT = 0;
//        for (int i = 0; i < posHist.length; i++) {
//            if (posHist[i] >= COEFF_FREQ_LIMIT_FOR_NON_STOP_POINTS) {
//                COEFF_LIMIT++;
//            }
//        }
//        if (COEFF_LIMIT % 2 == 0) {
//            COEFF_LIMIT++;
//        }
//        int totalBlocks = comp.data.size();
//
//        setSeed();
//        rand = new Random(seed);
//        int blockIndex = 0;
//        short currentBlock[] = null;
//        int dataIndex = 0;
//        int data[] = new int[8];
//
//        byte uniqueBitmap[] = new byte[totalBlocks * 64];
//        int cc = 0;
//        // int postablechanged[] = new int[COEFF_LIMIT];
//        // int negtablechanged[] = new int[COEFF_LIMIT];
//        int posOneRemain = posHist[0];
//        int negOneRemain = negHist[0];
//        int postableremain[] = new int[COEFF_LIMIT - 1];
//        int negtableremain[] = new int[COEFF_LIMIT - 1];
//        for (int i = 1; i < COEFF_LIMIT; i++) {
//            postableremain[i - 1] = (int) posHist[i];
//            negtableremain[i - 1] = (int) negHist[i];
//
//        }
//        String outfile = "";
//        if (output2Console) {
//            extractoutfile = System.currentTimeMillis() + "temp";
//            decoder.printStream.println("*************** Creating Temp file =" + extractoutfile);
//        }
//        outfile = extractoutfile;
//        fout = new FileOutputStream(outfile, append);
//        decoder.printStream.println("*******  Input Image is = " + inputImageFile);
//        decoder.printStream.println("*******  Output data file is = " + outfile);
//        decoder.printStream.println("*******  Password is = " + password);
//        int coeffIndex = 0;
//        int nextNumber = 0;
//        // calculate file size in bytes
//        int filesizeInBits[] = new int[FILE_SIZE_LENGTH];
//        int headerCounter = 0;
//        while (headerCounter < FILE_SIZE_LENGTH) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp, COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                // decoder.printStream.println("*********Embedded coeff number" + nextNumber);
//                if (number == -1) {
//                    filesizeInBits[headerCounter] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    filesizeInBits[headerCounter] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    filesizeInBits[headerCounter] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    filesizeInBits[headerCounter] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//
//        // calculate number of bits to represent max coeff
//        int bitsNeededToRepresentMaxCoeffInBits[] = new int[BLOCK_NUMBER_LENGTH];
//        headerCounter = 0;
//        while (headerCounter < BLOCK_NUMBER_LENGTH) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                // decoder.printStream.println("*********Embedded coeff number" + nextNumber);
//                if (number == -1) {
//                    bitsNeededToRepresentMaxCoeffInBits[headerCounter] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    bitsNeededToRepresentMaxCoeffInBits[headerCounter] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    bitsNeededToRepresentMaxCoeffInBits[headerCounter] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    bitsNeededToRepresentMaxCoeffInBits[headerCounter] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//
//        // calculate the number of stop points pairs
//        int numberOfStopPairsInBits[] = new int[STOP_POINT_LENGTH];
//        headerCounter = 0;
//        while (headerCounter < STOP_POINT_LENGTH) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            // decoder.printStream.println("*********Embedded coeff number" + nextNumber);
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                if (number == -1) {
//                    numberOfStopPairsInBits[headerCounter] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    numberOfStopPairsInBits[headerCounter] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    numberOfStopPairsInBits[headerCounter] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    numberOfStopPairsInBits[headerCounter] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//
//        int filesize = changeBitsToValue(filesizeInBits, FILE_SIZE_LENGTH);
//        int coeff_bits = changeBitsToValue(bitsNeededToRepresentMaxCoeffInBits,
//                BLOCK_NUMBER_LENGTH);
//        int numberOfStopPairs = changeBitsToValue(numberOfStopPairsInBits,
//                STOP_POINT_LENGTH);
//        int stopcoeff = numberOfStopPairs * 2 - 1;
//        int dynamicHeaderLength = (numberOfStopPairs * 2 - 1) * (coeff_bits);
//        int totalHeaderLength = FILE_SIZE_LENGTH + BLOCK_NUMBER_LENGTH + STOP_POINT_LENGTH + dynamicHeaderLength;
//
//        // calculate the stop points
//        int oneStops = -1;
//        int negativeStops[] = new int[COEFF_LIMIT / 2];
//        int positiveStops[] = new int[COEFF_LIMIT / 2];
//        for (int i = 0; i < COEFF_LIMIT / 2; i++) {
//            negativeStops[i] = -1;
//            positiveStops[i] = -1;
//
//        }
//        headerCounter = 0;
//        int k = 0;
//
//        int tempstop[] = new int[coeff_bits];
//        while (headerCounter < coeff_bits) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                if (number == -1) {
//                    tempstop[headerCounter % coeff_bits] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    tempstop[headerCounter % coeff_bits] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                }
//                if ((headerCounter + 1) % coeff_bits == 0) {
//                    oneStops = changeBitsToValue(tempstop, coeff_bits);
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//
//        headerCounter = 0;
//        k = 0;
//        while (headerCounter < (dynamicHeaderLength - coeff_bits) / 2) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                if (number == -1) {
//                    tempstop[headerCounter % coeff_bits] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    tempstop[headerCounter % coeff_bits] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number == -1) {
//                    tempstop[headerCounter % coeff_bits] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    tempstop[headerCounter % coeff_bits] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                }
//                if ((headerCounter + 1) % coeff_bits == 0) {
//                    negativeStops[k++] = changeBitsToValue(tempstop, coeff_bits);
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//
//        headerCounter = 0;
//        k = 0;
//        while (headerCounter < (dynamicHeaderLength - coeff_bits) / 2) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                if (number == -1) {
//                    tempstop[headerCounter % coeff_bits] = 0;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number == 1) {
//                    tempstop[headerCounter % coeff_bits] = 1;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    negtableremain[number1 - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number1 % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                } else if (number > 0) {
//                    postableremain[number - 2] -= 1;
//                    tempstop[headerCounter % coeff_bits] = number % 2;
//                    uniqueBitmap[nextNumber] = USED_BIT_FOR_HEADER;
//
//                }
//                if ((headerCounter + 1) % coeff_bits == 0) {
//                    positiveStops[k++] = changeBitsToValue(tempstop, coeff_bits);
//                }
//                headerCounter++;
//
//            }
//        } // end of while loop
//        for (int i = 1; i < COEFF_LIMIT - 1; i += 2) {
//            if (i > stopcoeff - 1) {
//                continue;
//            }
//            int a1 = (int) (posHist[i] - postableremain[i - 1]);
//            int a2 = (int) (posHist[i + 1] - postableremain[i]);
//            postableremain[i - 1] = posHist[i] - (a1 + a2);
//            postableremain[i] = posHist[i + 1] - (a1 + a2);
//
//            int b1 = (int) (negHist[i] - negtableremain[i - 1]);
//            int b2 = (int) (negHist[i + 1] - negtableremain[i]);
//            negtableremain[i - 1] = negHist[i] - (b1 + b2);
//            negtableremain[i] = negHist[i + 1] - (b1 + b2);
//
//        }
//
//        decoder.printStream.println("********************Extracting actual data bits now for component " + compNumber);
//
//        int stopCounter = COEFF_LIMIT;
//        dataIndex = 0;
//        int tempDataByte[] = new int[8];
//        while (dataIndex < filesize * 8) {
//            nextNumber = getNextIndex(rand, totalBlocks, uniqueBitmap, comp,COEFF_LIMIT);
//            uniqueBitmap[nextNumber] = PENDING_BIT;
//            coeffIndex = nextNumber % 64; // row number of the block
//            blockIndex = nextNumber / 64; // block number
//            currentBlock = (short[]) comp.data.get(blockIndex);
//            int number = (int) currentBlock[coeffIndex];
//            if (number == 0) {
//                uniqueBitmap[nextNumber] = ZERO_VALUE_BIT;
//                continue;
//            }
//            if (number >= -COEFF_LIMIT && number <= COEFF_LIMIT) {
//                if (number == -1) {
//                    if (oneStops == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (oneStops == nextNumber) {
//                        oneStops = -2;
//                        stopCounter--;
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    } else {
//                        tempDataByte[dataIndex % 8] = 0;
//                        uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                        dataIndex++;
//                    }
//
//                } else if (number == 1) {
//                    if (oneStops == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (oneStops == nextNumber) {
//                        oneStops = -2;
//                        stopCounter--;
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    } else {
//                        tempDataByte[dataIndex % 8] = 1;
//                        uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                        dataIndex++;
//                    }
//
//                } else if (number < 0) {
//                    int number1 = number * -1;
//                    if (number1 % 2 != 0 && negativeStops[number1 / 2 - 1] == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    }
//
//                    if (number1 % 2 == 0 && negativeStops[number1 / 2 - 1] == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (number1 > stopcoeff) {// not in the stop point limit
//                        if (number1 % 2 != 0) {
//                            int temp = negHist[number1 - 2] + negHist[number1 - 1];
//                            int remain = negtableremain[number1 - 3] + negtableremain[number1 - 2];
//                            if (remain <= temp / 2) {
//                                negativeStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 1;
//                                negtableremain[number1 - 2] -= 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//
//                        } else if (number1 % 2 == 0) {
//                            int temp = negHist[number1 - 1] + negHist[number1];
//                            int remain = negtableremain[number1 - 1] + negtableremain[number1 - 2];
//                            if (remain <= temp / 2) {
//                                negativeStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 0;
//                                negtableremain[number1 - 2] -= 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        }
//                    } else if (number1 <= stopcoeff) {// in the stop point
//                        // limit
//                        if (number1 % 2 != 0) {
//                            if (negativeStops[number1 / 2 - 1] == nextNumber) {
//                                negativeStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        } else if (number1 % 2 == 0) {
//                            if (negativeStops[number1 / 2 - 1] == nextNumber) {
//                                negativeStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 0;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        }
//
//                    } // end of number of stops
//                } // end of less than zero
//                else if (number > 0) {
//                    int number1 = number * 1;
//                    if (number1 % 2 != 0 && positiveStops[number1 / 2 - 1] == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//
//                        continue;
//                    }
//                    if (number1 % 2 == 0 && positiveStops[number1 / 2 - 1] == -2) {
//                        uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                        continue;
//                    }
//                    if (number1 > stopcoeff) {// not in the stop point limit
//                        if (number1 % 2 != 0) {
//                            int temp = posHist[number1 - 1] + posHist[number1 - 2];
//                            int remain = postableremain[number1 - 3] + postableremain[number1 - 2];
//                            if (remain <= temp / 2) {
//                                positiveStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 1;
//                                postableremain[number1 - 2] -= 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//
//                        } else if (number1 % 2 == 0) {
//                            int temp = posHist[number1 - 1] + posHist[number1];
//                            int remain = postableremain[number1 - 1] + postableremain[number1 - 2];
//                            if (remain <= temp / 2) {
//                                positiveStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 0;
//                                postableremain[number1 - 2] -= 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        }
//                    } else if (number1 <= stopcoeff) {// in the stop point
//                        // limit
//                        if (number1 % 2 != 0) {
//                            if (positiveStops[number1 / 2 - 1] == nextNumber) {
//                                positiveStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 1;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        } else if (number1 % 2 == 0) {
//                            if (positiveStops[number1 / 2 - 1] == nextNumber) {
//                                positiveStops[number1 / 2 - 1] = -2;
//                                stopCounter--;
//                                uniqueBitmap[nextNumber] = REUSUABLE_BIT;
//                                continue;
//                            } else {
//                                tempDataByte[dataIndex % 8] = 0;
//                                uniqueBitmap[nextNumber] = USED_BIT_FOR_DATA;
//                                dataIndex++;
//                            }
//                        }
//
//                    } // end of number of stops
//                } // end of greater than zero
//                if ((dataIndex) % 8 == 0) {
//                    byte newData = changeBits2Byte(tempDataByte);
//                    fout.write(newData);
//                }
//
//            }// end of number range
//        } // end of while loop
//
//        fout.close();
//
//        if (output2Console) {
//            BufferedReader br = new BufferedReader(new FileReader(
//                    extractoutfile));
//            String str = "";
//            decoder.printStream.println("####################### OUTPUT DATA START #########################");
//
//            while ((str = br.readLine()) != null) {
//
//                if (isgui) {
//                    System.out.println(str + "\n");
//                }
//                decoder.printStream.println(str);
//            }
//            decoder.printStream.println("####################### OUTPUT DATA END #########################");
//
//            br.close();
//            decoder.printStream.println("*************** Deleting temp file =" + extractoutfile);
//            File f2 = new File(extractoutfile);
//            f2.delete();
//        }
//
//        decoder.printStream.println("*************** Extraction Process Complete for component " + compNumber);
//
//        return;
 //   }

}
