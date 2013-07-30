import javax.crypto.spec.PSource;


public class Freckle {

	
	
	
	

private int posfrek4=0;
private int negfrek4=0;
private int posfrek8=0;
private int negfrek8=0;
private int zeroneigh4=0;
private int zeroneigh8=0;
private int nonzeros;

public Freckle(){
	
}

public void calculateFreckles(JPEGComponent comp) {
	posfrek4=0;
	negfrek4=0;
	posfrek8=0;
	negfrek8=0;
	zeroneigh4=0;
	zeroneigh8=0;
	nonzeros=0;
	for(int i=0;i<comp.data.size();i++){
		double block[][]=(double[][])comp.data.get(i);
			for(int k=0;k<8;k++){
				
				if(block[0][k]!=0.0)
					nonzeros+=1;
				if(block[7][k]!=0.0)
					nonzeros+=1;
			}
			for(int k=1;k<7;k++){
				if(block[k][0]!=0.0)
					nonzeros+=1;
				if(block[k][7]!=0.0)
					nonzeros+=1;
			
			}
			//nonzeros-=1;
		for(int j=1;j<7;j++){
			for(int k=1;k<7;k++){
			if(block[j][k]!=0.0){
				nonzeros+=1;
				if(		block[j][k+1]==0.0 && 
						block[j][k-1]==0.0&&
						block[j+1][k]==0.0&&
						block[j-1][k]==0.0)
				{
				posfrek4+=1;		
				if(		block[j+1][k+1]==0.0&&
						block[j-1][k-1]==0.0&&
						block[j+1][k-1]==0.0&&
						block[j-1][k+1]==0.0){
					
					posfrek8+=1;
					
				}
				}//outer iff
				
			}else if(block[j][k]==0.0){
				if(		block[j][k+1]!=0.0 && 
						block[j][k-1]!=0.0&&
						block[j+1][k]!=0.0&&
						block[j-1][k]!=0.0){
						negfrek4+=1;
						if(block[j+1][k+1]!=0.0&&
						block[j-1][k-1]!=0.0&&
						block[j+1][k-1]!=0.0&&
						block[j-1][k+1]!=0.0){
					
					negfrek8+=1;
						}
				}
				
			}
				if(block[j][k]==0.0){
				if(		block[j][k+1]==0.0 && 
						block[j][k-1]==0.0&&
						block[j+1][k]==0.0&&
						block[j-1][k]==0.0){
						zeroneigh4+=1;
						if(block[j+1][k+1]==0.0&&
						block[j-1][k-1]==0.0&&
						block[j+1][k-1]==0.0&&
						block[j-1][k+1]==0.0){
					
					zeroneigh8+=1;
						}
				}
				
			}

				
			
		}//inner inner for
		
	}//inner for
	}//for
	
}

public int getPosFreckle4() {
	// TODO Auto-generated method stub
	return posfrek4;
}

public int getNegFreckle4() {
	// TODO Auto-generated method stub
	return negfrek4;
}
public int getPosFreckle8() {
	// TODO Auto-generated method stub
	return posfrek8;
}

public int getNegFreckle8() {
	// TODO Auto-generated method stub
	return negfrek8;
}
public int getzeroneighbours4() {
	// TODO Auto-generated method stub
	return zeroneigh4;
}

public int getzeroneighbours8() {
	// TODO Auto-generated method stub
	return zeroneigh8;
}
public int getnonzero() {
	// TODO Auto-generated method stub
	return nonzeros;
	//return posfrek8;

}

}