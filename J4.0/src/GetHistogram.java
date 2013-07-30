


public class GetHistogram implements Constants{
	boolean bitmap[][];
	int width=0;
	int height=0;
	int zeros=0;
        int zeroUnderSize=0;
        public int totalPosOnes=0;
        public int totalNegOnes=0;
	JPEGComponent comp=null;
        //dont include 1, -1 and 0
	public int posBins[][]=new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT-1];
        public int negBins[][]=new int[DUAL_HISTOGRAM_SIZE][DUAL_HISTOGRAM_COEFF_LIMIT-1];


	int posHistarray[]=new int[ HISTOGRAM_UPPER_LIMIT ];
	int negHistarray[]=new int[ HISTOGRAM_UPPER_LIMIT ];
	public GetHistogram(){
		
	}

	public GetHistogram(int width, int height,String title){
		this.width=width;
		this.height=height;
	}
	
public int[] getPosHist(){
	 return posHistarray;
	
}
public int getZeros(){
	 return zeros;
	
}

public int[][] getPosDualBin(){
	 return posBins;

}
public int[][] getNegDualBin(){
	 return negBins;

}

public int[] getNegHist(){
	 return negHistarray;
	
}


public void setPoints(JPEGComponent comp) {
	this.comp=comp;
	double h=comp.factorH/(double)comp.maxH;
	double v=comp.factorV/(double)comp.maxV;
	int cols=(int) (Math.ceil(width/8.0)*h);
	int rows=(int) (Math.ceil(height/8.0)*v);

	int z=0;
	int count=0;

	for (int r = 0; r < comp.data.size(); r++) {
		//for (int c = 0; c < cols; c++) {
		short block[] = (short[]) comp.data.get(r);
		for (int j = 1; j < 64; j++) {
				//if(j==0) continue;
//				if((int)block[j]<=HISTOGRAM_UPPER_LIMIT && (int)block[j]>0)
//					posHistarray[(int)block[j]-1]++;
//				else if((int)block[j]>=-HISTOGRAM_UPPER_LIMIT && (int)block[j]<0)
//					negHistarray[-(int)block[j]-1]++;
//				else if((int)block[j]==0)
//					zeros++;
				 if((int)block[j]==0){
                                    zeros++;
                                    
                                 }
                                if((int)block[j]==1){
                                    totalPosOnes++;

                                 }
                                 if((int)block[j]==-1){
                                    totalNegOnes++;

                                 }
                                if(j<=DUAL_HISTOGRAM_SIZE){

                                    if((int)block[j]<=DUAL_HISTOGRAM_COEFF_LIMIT && (int)block[j]>0)
					posHistarray[(int)block[j]-1]++;
				else if((int)block[j]>=-DUAL_HISTOGRAM_COEFF_LIMIT && (int)block[j]<0)
					negHistarray[-(int)block[j]-1]++;
                                else  if((int)block[j]==0){
                                    zeroUnderSize++;
                                }
					

                                    if((int)block[j]<=DUAL_HISTOGRAM_COEFF_LIMIT && (int)block[j]>1)
					posBins[j-1][(int)block[j]-2]++;
				else if((int)block[j]>=-DUAL_HISTOGRAM_COEFF_LIMIT && (int)block[j]<-1)
					negBins[j-1][-(int)block[j]-2]++;
                                }
                                    


                
		}
	}


		
//		for (int j = 0; j < 1; j++) {
//			for (int k = 1; k < 7; k++) {
//			
//				
//				if(block[j][k]!=0.0){
//					
//					
//					
//					if(		block[j][k+1]==0.0 && 
//							block[j][k-1]==0.0&&
//							block[j+1][k]==0.0&&
//							block[j+1][k+1]==0.0&&
//							block[j+1][k-1]==0.0
//							){
//						bitmap[r*8+j][c*8+k]=true;
//
//					}
//					}
//				}
//		}
//		for (int j = 7; j < 8; j++) {
//			for (int k = 1; k < 7; k++) {
//				if(block[j][k]!=0.0){
//					
//					
//					
//					if(		block[j][k+1]==0.0 && 
//							block[j][k-1]==0.0&&
//							block[j-1][k]==0.0&&
//							block[j-1][k+1]==0.0&&
//							block[j-1][k-1]==0.0
//							){
//						bitmap[r*8+j][c*8+k]=true;
//
//					}
//					}
//				}
//		}
//
//		for (int j = 1; j < 7; j++) {
//			for (int k = 0; k < 1; k++) {
//
//				if(block[j][k]!=0.0){
//					
//					
//					if(		block[j][k+1]==0.0 && 
//							block[j+1][k]==0.0&&
//							block[j-1][k]==0.0 &&
//							block[j+1][k+1]==0.0&&
//							block[j-1][k+1]==0.0
//							){
//						bitmap[r*8+j][c*8+k]=true;
//
//					}
//					}
//				}
//		}
//
//		for (int j = 1; j < 7; j++) {
//			for (int k = 7; k < 8; k++) {
//
//				if(block[j][k]!=0.0){
//					
//					
//					if(		block[j][k-1]==0.0 && 
//							block[j+1][k]==0.0&&
//							block[j-1][k]==0.0 &&
//							block[j+1][k-1]==0.0&&
//							block[j-1][k-1]==0.0
//							){
//						bitmap[r*8+j][c*8+k]=true;
//
//					}
//					}
//				}
//		}
//		
//				if(block[0][7]!=0.0){
//					int j=0;
//					int k=7;
//
//					if(		block[0][6]==0.0 && 
//							block[1][7]==0.0&&
//							block[1][6]==0.0
//							){
//						bitmap[r*8+0][c*8+7]=true;
//
//					}
//					}
//		
//				if(block[7][0]!=0.0){
//					int j=7;
//					int k=0;
//
//					
//					if(		block[7][1]==0.0 && 
//							block[6][1]==0.0&&
//							block[6][0]==0.0
//							){
//						bitmap[r*8+7][c*8+0]=true;
//
//					}
//					}
//				if(block[7][7]!=0.0){
//					int j=7;
//					int k=7;
//
//					
//					if(		block[6][7]==0.0 && 
//							block[7][6]==0.0&&
//							block[6][6]==0.0
//							){
//						bitmap[r*8+7][c*8+7]=true;
//
//					}
//					}
//
//				
//				
//				
//		for (int j = 1; j < 7; j++) {
//			for (int k = 1; k < 7; k++) {
//
//				if(block[j][k]!=0.0){
//					
//					
//					
//					if(		block[j][k+1]==0.0 && 
//							block[j][k-1]==0.0&&
//							block[j+1][k]==0.0&&
//							block[j-1][k]==0.0 &&
//							block[j+1][k+1]==0.0&&
//							block[j-1][k-1]==0.0&&
//							block[j+1][k-1]==0.0&&
//							block[j-1][k+1]==0.0
//							){
//						
////						x[z]=c*8+k;
////						y[z]=r*8+j;
////						++z;
//						//if(title.indexOf("cropped")>0 && (k%4==0 || j%4==0 ||k%5==0 || j%5==0) )
//						//continue;
//							bitmap[r*8+j][c*8+k]=true;
//						
//					}
//					}//outer iff
//
//				
//				
//				}
//				
//			}
//			}	
//}

}

//public void reconstruct(String file, int nh,int nw) {
//	boolean bitmap1[][]=new boolean[nh][nw];
//	for(int i=4;i<height+4;i++){
//		for(int j=4;j<width+4;j++){
//		if(bitmap[i-4][j-4])
//			g1.drawString("*", j, i);
//		//g.fillOval(x[i], y[i], 1, 1);
//	}
//	}
//    g1.dispose();
//	FileOutputStream fos;
//	
//	// TODO Auto-generated method stub
//	
//}
}