
public interface Constants {
static final int BUFFER = 2048;
public final int COEFF_FREQ_LIMIT_FOR_NON_STOP_POINTS=2000;
public final int FILE_SIZE_LENGTH=16;
public final int BLOCK_NUMBER_LENGTH=5;
public final int STOP_POINT_LENGTH=5;
public final int COEFF_SIZE_FOR_STOP_POINTS=2000;
public final int UNUSED_BIT=0;
public final int NOT_USUABLE_BIT=2;
public final int USED_BIT_FOR_HEADER=3;
public final int USED_BIT_FOR_DATA=1;
public final int REUSUABLE_BIT=-1;
public final int PENDING_BIT=4;
public final int ZERO_VALUE_BIT=5;
public final int USED_BIT_FOR_BALANCE=6;
public final  int HISTOGRAM_UPPER_LIMIT=256;
public final  int DUAL_HISTOGRAM_COEFF_LIMIT=21; //always odd
public final  int DUAL_HISTOGRAM_SIZE= 30;


public final int SEEDS=1;
public final int HUFFMAN_CODE_N_BITS=3;
public final static int CODES[][] = {
            {1, 3, 5, 7, 9, 11, 13, 15, 17,19,21,23,25,27,29,31},
            {2, 3, 6, 7, 10, 11, 14, 15,18,19,22,23,26,27,30,31},
            {4, 5, 6, 7, 12, 13, 14, 15,20,21,22,23,28,29,30,31},
            {8, 9, 10, 11, 12, 13, 14, 15,24,25,26,27,28,29,30,31},
            {16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31},
            {32,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31},
            {64,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31},
            {128,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31},
            {256,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31}


};




}
