import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class test1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
  FileInputStream fin=new FileInputStream("test6.jpg");
  int a=0; int i=0;
  while((a=fin.read())!=-1){
	  System.out.println(++i+"  "+a+"  ");
  }
		
		
	}

}
