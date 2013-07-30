
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mahendra Kumar
 */
public class Utility implements Constants{
    
    public static byte[] getDESKey(String pass) {
        try {
            MessageDigest md1 = MessageDigest.getInstance("MD5");
            md1.update(pass.getBytes());
            byte b[] = md1.digest();
            byte temp[] = new byte[8];
            System.arraycopy(b, 0, temp, 0, temp.length);
            return temp;

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
        }
        return null;

    }
    public static byte changeBits2Byte(int[] data) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {

            int a = (int) (Math.pow(2, i) * data[i]);
            sum += a;
        }
        sum = sum - 128;
        // printStream.println("bits decoded "+sum);
        return (byte) sum;
    }

public static int[] changetoBits(long number, int length) {
        int bit[] = new int[length];
        for (int i = 0; i < length; i++) {
            bit[i] = (int) (number % 2);
            number = number / 2;

        }
        return bit;
    }

    public static int changeBitsToValue(int bits[], int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            int a = (int) (Math.pow(2, i) * bits[i]);
            value += a;

        }
        return value;
    }
public static int getBitsRequired(int totalBlocks) {
        int count = (int) Math.pow(2, BLOCK_NUMBER_LENGTH);
        for (int i = 0; i < count; i++) {
            if ((int) Math.pow(2, i) >= totalBlocks - 1) {
                return i;
            }

        }
        return -1;
    }
  public static int[] ConvertFile2Bits(String datafile, int offset)
            throws IOException {
        int k = 0;
        int b = 0;
        File f3 = new File(datafile);
        int datalength = (int) f3.length();
        FileInputStream fin = new FileInputStream(datafile);
        int templen = datalength * 8 - offset * 8;
        int[] res = new int[templen];
        fin.skip(offset);
        for (int fc = 0; fc < templen / 8; fc++) {
            b = fin.read();
            int c[] = getBitsfromByte((byte) b);
            for (int i = 0; i < 8; i++) {
                res[k++] = c[i];
            }
        }
        fin.close();
        return res;
    }
  public static int[] getBitsfromByte(byte data) {

        // big endian format
        int bit[] = new int[8];
        int data1 = 0;
        data1 = 128 + data;
        for (int i = 0; i < 8; i++) {
            if (data1 == 1) {
                bit[i] = 1;
                break;
            }

            bit[i] = data1 % 2;
            data1 = data1 / 2;

        }

        return bit;
    }

    public static int[] get3BitsfromNumber(int data) {

        // big endian format
        int bit[] = new int[3];
        for (int i = 0; i < 3; i++) {
            if (data == 1) {
                bit[i] = 1;
                break;
            }

            bit[i] = data % 2;
            data = data / 2;

        }

        return bit;
    }
    public static int[] ConvertData2Bits(byte data[], int datalen) throws IOException {
        int[] res = new int[datalen * 8];
        int k = 0;
         
        for (int fc = 0; fc < datalen; fc++) {
            int c[] = getBitsfromByte(data[fc]);
            for (int i = 0; i < 8; i++) {
                res[k++] = c[i];
            }
        }

        return res;
    }
}
