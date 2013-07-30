
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class RunSim implements Constants{
  //  String homeDir="/home/users/kumarmah/";
   // String homeDir="c:\\";
   // String indir = homeDir+"coverImagesNew/";
         //String outdir = "/home/users/kumarmah/d:\\j3_0.05bpnz\\";
        //String resultfile="j3_0.05bpnz.txt";
        //String outdir="";
                String outext = "txt";
         String datafile = "temp.txt";
        // double BPNZ=0.05;


   Embed1 embed;
   	
    public void runEmbed(double bpnz, String indir, String outdir, String resultfile) throws Exception {
         String pathSep= File.separator;

        if(!indir.endsWith(pathSep))
            indir+=pathSep;
          if(!outdir.endsWith(pathSep))
            outdir+=pathSep;

       // String resultfile=outf+".txt";
        FileWriter fw = null;
        try {
            File f1 = new File(indir + "Thumbs.db");
            f1.delete();
            File dir = new File(indir);
            String[] children = dir.list();
            int dirlength = children.length;
          // dirlength=1;
            String inputImage = "";
            String outputImage = "";
            double[] capacity0 = new double[dirlength];
            double[] capacity = new double[dirlength];
            double[] bitsPerCoeff0 = new double[dirlength];
            double[] bitsPerCoeff = new double[dirlength];
            double[] bitsPerPixel = new double[dirlength];
            double[] bitsPerPixel0 = new double[dirlength];
            double[] zeros = new double[dirlength];
            double[] nonzeros = new double[dirlength];
            double[] fileSize = new double[dirlength];
            double[] estimatedCapacity0 = new double[dirlength];
            double[] estimatedCapacity = new double[dirlength];
            double[] timetaken0 = new double[dirlength];
         //   double[] timetaken = new double[dirlength];
            int[][] estimatedstop = new int[dirlength][20];
            int[][] actualstop = new int[dirlength][20];
            int[][] TT = new int[dirlength][4];
            int[][] TR = new int[dirlength][4];
            int[][] TC = new int[dirlength][4];
            int[] height = new int[dirlength];
            int[] width = new int[dirlength];
            int k = 0;
            FileWriter fweff = null;
             fw = new FileWriter(resultfile,true);
            String temp = "\nFile Name, Height, Width, File Size(KB),Bits_Per_Pixel,Capacity,Estimated_Capacity(Bytes)," + 
                    "Bits_per_non-zero_Coefficient\n";
            fw.write(temp);
            fw.close();
            for (int i = 0; i < dirlength; i++) {

                try {
                    inputImage = indir + children[i];
                    outputImage = outdir + children[i];
                    String extension = "";
                    int whereDot = children[i].lastIndexOf('.');
                    if (0 < whereDot && whereDot <= children[i].length() - 2) {
                        extension = children[i].substring(whereDot + 1);
                    }
                    if (!extension.equals("jpg") && !extension.equals("JPG") && !extension.equals("jpeg") && !extension.equals("JPEG")) {
                        System.out.println("Invalid extension for file " + inputImage);
                        continue;
                    }
                   
                    System.out.println("\n\n*********************************************");
                    System.out.println("starting analysis of " + inputImage + "   File number = " + (i+1));
                    System.out.println("***********************************************");
                    for (int seed = 0; seed < SEEDS; seed++) {
                        double bytesembedded = 0;
                        
                        generateRandomFile(datafile, -1);
                        String password = generateRandomPassword(-1);
                        long time1 = System.currentTimeMillis();
                        
                        int res=embed(password, inputImage, outputImage, true, datafile, "", false, true, "log.txt", false,bpnz);
                         if(res==-5) continue;
                         bytesembedded += (embed.TOTALBytesEmbedded[0]);
                        int nonzero0 = embed.TOTALBlocks[0] * 64 - embed.TOTALZeros[0];
                        nonzeros[i] = nonzero0;
                        zeros[i] = embed.TOTALZeros[0];
                        int nonzero = 0;
                        for (int m = 0; m < 3; m++) {
                            nonzero += (embed.TOTALBlocks[m] * 64 - embed.TOTALZeros[m]);
                        }
                        File f3 = new File(inputImage);
//                                               fweff=new FileWriter("eff.txt",true);
//                        fweff.write(children[i]+","+f3.length()+","+embed.efficiency+"\n");
//                        fweff.close();

                       // capacity0[i] += (double) (embed.TOTALBytesEmbedded[0]);
                        capacity[i] += (double) (bytesembedded);
                       // estimatedCapacity0[i] = (embed.TotalEstimatedCapacity[0]);
                        estimatedCapacity[i] = (embed.TotalEstimatedCapacity[0]);
                       // bitsPerPixel0[i] += (double) (embed.TOTALBytesEmbedded[0] * 8) / (embed.decoder.height * embed.decoder.width);
                        bitsPerPixel[i] += (double) (bytesembedded * 8) / (embed.decoder.height * embed.decoder.width);
                        //bitsPerCoeff0[i] += (double) (embed.TOTALBytesEmbedded[0] * 8) / nonzero0;
                        bitsPerCoeff[i] += (double) (bytesembedded * 8) / nonzero;
//                        for (int m = 0; m < embed.estimatesstops.length; m++) {
//                            estimatedstop[i][m] += (embed.estimatesstops[m]);
//                            actualstop[i][m] += embed.actualstop[m];
//                        }
                        
                        fileSize[i] = f3.length();
                        height[i] = embed.decoder.height;
                        width[i] = embed.decoder.width;
                        System.out.println("******************** End analysis of " + inputImage);
            fw = new FileWriter(resultfile,true);
               temp = children[i] + "," + height[i] + "," + width[i] + "," + fileSize[i] / 1024.0 + ","  + bitsPerPixel[i] + "," + capacity[i] + "," +  estimatedCapacity[i] + ","  + bitsPerCoeff[i] + "\n";
                fw.write(temp);
                fw.close();
             System.out.println(temp);
           

                    }
//                    for (int m = 0; m < dec.estimatesstops.length; m++) {
//                        estimatedstop[i][m] /= SEEDS;
//                        actualstop[i][m] /= SEEDS;
//                    }
//                    for (int m = 0; m < dec.tableTotal.length; m++) {
//                        TT[i][m] /= SEEDS;
//                        TR[i][m] /= SEEDS;
//                        TC[i][m] /= SEEDS;
//                    }
//                    capacity0[i] /= SEEDS;
//                    capacity[i] /= SEEDS;
//                    bitsPerCoeff0[i] /= SEEDS;
//                    bitsPerCoeff[i] /= SEEDS;
//                    bitsPerPixel[i] /= SEEDS;
//                    bitsPerPixel0[i] /= SEEDS;
//                    timetaken0[i] /= SEEDS;
//                    timetaken[i] /= SEEDS;
                } catch (Throwable e) {
                   throw new Exception(e.fillInStackTrace());
                }
            }
//            fw = new FileWriter(resultfile,false);
//            String temp = "\nFile Name, Height, Width, File Size(KB), Bits_Per_Pixel_Lu,Bits_Per_Pixel,Capacity_Lu(Bytes), Capacity(Bytes),Estimated_Capacity_Lu(Bytes),Estimated_Capacity(Bytes)," + "Bits_per_non-zero_Coefficient_Lu,Bits_per_non-zero_Coefficient,Time_Taken_Lu(ms),Time_Taken(ms)\n";
//            fw.write(temp);
//            System.out.println(temp);
//            for (int i = 0; i < dirlength; i++) {
//                temp = children[i] + "," + height[i] + "," + width[i] + "," + fileSize[i] / 1024.0 + "," + bitsPerPixel0[i] + "," + bitsPerPixel[i] + "," + capacity0[i] + "," + capacity[i] + "," + estimatedCapacity0[i] + "," + estimatedCapacity[i] + "," + bitsPerCoeff0[i] + "," + bitsPerCoeff[i] + "," + timetaken0[i] + "," + timetaken[i]+"\n";
//                fw.write(temp);
//             System.out.println(temp);
//           }
//            temp = "\nFile Name, Height, Width, File Size(KB), Total(-1), Total(1),Total(2), Total(3), TR(-1), TR(1), TR(2), TR(3), TC(-1->1), TC (1->-1), TC(2->3), TC(3->2), SP(-1_1), SP(2_3), zeros, nonzeros\n";
//          //  fw.write(temp);
//            for (int i = 0; i < dirlength; i++) {
//                temp = children[i] + "," + height[i] + "," + width[i] + "," + fileSize[i] / 1024.0 + "," + TT[i][0] + "," + TT[i][1] + "," + TT[i][2] + "," + TT[i][3] + "," + TR[i][0] + "," + TR[i][1] + "," + TR[i][2] + "," + TR[i][3] + "," + TC[i][0] + "," + TC[i][1] + "," + TC[i][2] + "," + TC[i][3] + "," + actualstop[i][0] + "," + actualstop[i][1] + "," + zeros[i] + "," + nonzeros[i]+"\n";
//               // fw.write(temp);
//               //System.out.println(temp);
//         }
//            fw.close();
//        for (int i = 0; i < dirlength; i++) {
//        	System.out.println("\n\n\n\n**************************************************");
//        	System.out.println("File name =" +children[i]);
//        	System.out.println("Pair;Estimated Stop;Actual Stop");
//        	System.out.println("(-1,1);"+estimatedstop[i][0]+";"+actualstop[i][0]);
//        	for (int j = 1; j < estimatedstop[i].length; j++){
//            	System.out.println("("+(j*2)+","+(j*2+1)+");"+estimatedstop[i][j]+";"+actualstop[i][j]);
//
//
//        	}
//        	System.out.println("**************************************************");
//
//        }
//
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
       		 
        
//        for (int i = 0; i < dirlength; i++) {
//        	System.out.println("\n\n\n\n**************************************************");
//        	System.out.println("File name =" +children[i]);
//        	System.out.println("Pair;Estimated Stop;Actual Stop");
//        	System.out.println("(-1,1);"+estimatedstop[i][0]+";"+actualstop[i][0]);
//        	for (int j = 1; j < estimatedstop[i].length; j++){
//            	System.out.println("("+(j*2)+","+(j*2+1)+");"+estimatedstop[i][j]+";"+actualstop[i][j]);
//       		
//        		
//        	}
//        	System.out.println("**************************************************");
//       
//        }
//       

    }//end function

  
 public void generateRandomFile(String filename, int seed) throws Exception{
	Random r=null;
     File f= new File(filename);
	 if(f.exists())
		 f.delete();
	if(seed==-1)
            r=new Random();
        else
     r =new Random(seed);
	 byte barr[]= new byte[1000];
	 try {
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(filename));
		 for(int i=0;i<60;i++){
				 r.nextBytes(barr);
			bout.write(barr);
		 }
		 bout.close();
	} catch (Exception ex) {
		throw ex;
	} 
	 
 }
 public String generateRandomPassword(int seed){
	Random r=null;
     if(seed==-1)
            r =new Random();
        else
            r =new Random(seed);
	 byte barr[]= new byte[10];
	  r.nextBytes(barr);
		return barr.toString();	
	 
 }
    public static void main(String args[]) throws Exception {
        // java.awt.EventQueue.invokeLater(new Runnable() {
        FileWriter fw;
        try {
           System.out.println("Writing stats to file:efficiency.csv");
            fw = new FileWriter("efficiency.csv", true);
             fw.write("filename, bpnz, codek, initial +1s, final +1s, intitial -1s, final -1s," +
                    "initial zero, final zero,efficiency,+1 to 0, 0 to +1, -1 to 0,0 to -1\n");
            fw.close(); 
        } catch (IOException ex) {
          throw ex;
        }


        RunSim runSim = new RunSim();
        double bpnz=0.05;
        String outdir="i:\\testImagesOut";
        String indir="d:\\coverImagesNew";
        String resfile="result.csv";
        if(args.length<3){
         System.out.println("java -jar J4.4.jar <bpnz[0.05,0.1,0.2,0.3 or 0.4]> <input image dir> <out image dir>");
        System.out.println("example:\n java -jar J4.4.jar 0.1 c:\\testimages d:\\outimages");

                   System.exit(0);
        }else {
            indir=args[1];
            outdir=args[2];
           // resfile=args[3];
            bpnz=Double.parseDouble(args[0]);
        }
        
        System.out.println("Writing results  to file:result.csv");
        runSim.runEmbed(bpnz,indir,outdir,resfile);
    }

public static HashMap getHashMap(String filename) throws IOException{
    BufferedReader br=null;
    HashMap mp=null;
        try {
            mp = new HashMap();
            br = new BufferedReader(new FileReader(filename));
            String temp="";
            while((temp=br.readLine())!=null){
               StringTokenizer st=new StringTokenizer(temp,",");
               mp.put(st.nextToken(), st.nextToken());
            }
            return mp;
        } catch (IOException ex) {
           throw ex;
        } finally {
            try {
                br.close();
                return mp;
            } catch (IOException ex) {
                throw ex;
            }
        }

}
   int embed(String password, String inputImageFile,
            String outimageFilename, boolean inputFromFile,
            String embedinfilename, String inputData, boolean verbose,
            boolean system, String verfilename, boolean isgui, double BPNZ) throws Exception {
           JPEGDecoder dec = new JPEGDecoder(inputImageFile,verbose,system,verfilename);
        try {
            int a =0;
            dec.decode();
            embed=new Embed1(dec, password, embedinfilename, outimageFilename);
            a = embed.embedData(dec.frame,BPNZ);
              if(a!=-5)
                  embed.encodeJPEG(dec.frame);
            return a;
        } catch (Throwable ex) {
            Logger.getLogger(RunSim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
