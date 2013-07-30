import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;



public class DataGenerator {

	int seed =0;
	Random r=null;
	public DataGenerator(int seed){
		this.seed=seed;
		r= new Random(seed);
	}
	
	void generateDataFile(int size, String name){
		File f=new File(name);
		if(f.exists()){
			f.delete();
			
		}
		byte tempbyte[]=new byte[size];
			r.nextBytes(tempbyte);
			try {
				FileOutputStream fout= new FileOutputStream(name);
				fout.write(tempbyte);
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
	}
	
	
}


