package trainig_pca;

import java.io.*;
import java.util.*;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class Classification  {
	private  CascadeClassifier      mJavaDetector; 	 	
	private  CascadeClassifier      mJavaDetectorEye;
	private  CascadeClassifier      mJavaDetectorMouth; 
	private  CascadeClassifier      mJavaDetectorNose; 
	private double[][] weightArray;
	private String[] line;
	private int neuralNumber, nrh;
	private Mat mean, eigenVectors;
	private double err;
	
	

	public double getErr() {
		return err;
	}

	public void setErr(double err) {
		this.err = err;
	}

	//Phuong thuc load file phan lop
	public   Classification() {
		 	
		
		try {            
	
	       mJavaDetector = new CascadeClassifier("D:/Haar Feature/haarcascade_frontalface_alt.xml");
	       mJavaDetectorEye = new CascadeClassifier("D:/Haar Feature/haarcascade_eye.xml");
	       mJavaDetectorMouth = new CascadeClassifier("D:/Haar Feature/haarcascade_mcs_mouth.xml");
	       mJavaDetectorNose = new CascadeClassifier("D:/Haar Feature/haarcascade_mcs_nose.xml");
	       if (mJavaDetector.empty() || mJavaDetectorEye.empty()) {
	           System.out.println("Failed to load cascade classifier");
	           mJavaDetector = null;
	           mJavaDetectorEye = null;
	       }      
	          
	
	   } catch (Exception e) {
		   System.out.println("Failed to load cascade classifier");
	       e.printStackTrace();
	       
	   }
	}

	//ham doc file
	public double[][] readWeightFile(String path) throws IOException {
		int i=0;
		//So phan tu bang so noron cua mang.
	//	double[][] inputArray = new double[61][];
		FileInputStream f = new FileInputStream(path); //tao bien tep f
		Scanner input = new Scanner(f); //doc tu tep f su dung Scanner
        
		nrh = Integer.parseInt(input.nextLine());
		err = Double.parseDouble(input.nextLine());
		neuralNumber = nrh + 1;
		weightArray = new double[neuralNumber][];
		line = new String[nrh + 1];
		
		while(input.hasNextLine()) //trong khi chưa het file
		{
			line[i]= input.nextLine(); //doc 1 dong
			if(line[i].trim()!="") //neu dong khong phai rong
			{
				String item[] = line[i].split(","); //cat cac thong tin cua line bang dau phay
				weightArray[i] = new double[item.length];
				for(int j = 0; j < item.length; j ++) {
					weightArray[i][j] = Double.parseDouble(item[j]);
				}
				
				
			}
			i++;
		}
		input.close();
		return weightArray;
	}
	
	//ham doc file
		public  Mat readVectorsFile(String path) throws IOException {			
		
		
			FileInputStream f = new FileInputStream(path); //tao bien tep f
			Scanner input = new Scanner(f); //doc tu tep f su dung Scanner	        
			String line;
			Mat mat;
			int rows = 0, cols = 0;
			
			rows = Integer.parseInt(input.nextLine());
			cols = Integer.parseInt(input.nextLine());
			mat = new Mat(rows, cols, 0);
		//	mat = new Mat();
			
			
			for (int i = 0; i < rows; i++) {
				line = input.nextLine(); //doc 1 dong
				if(line.trim()!="") //neu dong khong phai rong
				{
					String item[] = line.split(","); //cat cac thong tin cua line bang dau phay
				
					for(int j = 0; j < item.length; j ++) {
						mat.put(i, j, Double.parseDouble(item[j]));
					//	System.out.printf("\n eigenVectors.get(%d, %d)[0] = %e",i, j, 
			    		//		mat.get(0, j)[0]);
						
					}
					
					
				}
				
			}
			input.close();
			return mat;
		}
		
		//ham doc file
				public  double[][] readVectorsFile1(String path) throws IOException {			
				
				
					FileInputStream f = new FileInputStream(path); //tao bien tep f
					Scanner input = new Scanner(f); //doc tu tep f su dung Scanner	        
					String line;
					double[][] mat;
					int rows = 0, cols = 0;
					
					rows = Integer.parseInt(input.nextLine());
					cols = Integer.parseInt(input.nextLine());
					mat = new double[rows][cols];
				
					
					for (int i = 0; i < rows; i++) {
						line = input.nextLine(); //doc 1 dong
						if(line.trim()!="") //neu dong khong phai rong
						{
							String item[] = line.split(","); //cat cac thong tin cua line bang dau phay
						
							for(int j = 0; j < item.length; j ++) {
								mat[i][j] =  Double.parseDouble(item[j]);
							//	System.out.printf("\n eigenVectors.get(%d, %d)[0] = %e",i, j, 
					    		//		mat.get(0, j)[0]);
								
							}
							
							
						}
						
					}
					input.close();
					return mat;
				}
				
	
	//Ham ghi file tu file da doc
	public void write(String path) throws IOException {
		FileOutputStream f = new FileOutputStream(path);
		PrintWriter output = new PrintWriter(f);
		
		for(int i = 0; i < weightArray.length; i++) {
			for(int j = 0; j < weightArray[i].length; j++) {
				output.println(weightArray[i][j]);
			}
		}
		
		output.close();
		
	}
	
	//Ham ghi file tu file da doc
		public void write1(String path, Mat eigenVectors) throws IOException {
			FileOutputStream f = new FileOutputStream(path);
			PrintWriter output = new PrintWriter(f);
			
			for(int i = 0; i < eigenVectors.rows(); i++) {
				for(int j = 0; j < eigenVectors.cols(); j++) {
					output.print(eigenVectors.get(i, j)[0]);
					output.print(",");
				}
				output.println();
			}
			
			output.close();
			
		}
	
		
		//--------------------------------------------------------------------------------
		public static Mat loadImageFromFile(String p) {
	    	
	        Mat rgbLoadedImage = null;
	        //String fileName ="";	        
	        

	        // this should be in BGR format according to the
	        // documentation.
	     
	        Mat image = Imgcodecs.imread(p);

	        if (image.width() > 0) {

	            rgbLoadedImage = new Mat(image.size(), image.type());

	            Imgproc.cvtColor(image, rgbLoadedImage, Imgproc.COLOR_BGR2RGB);
	            
	            image.release();
	            image = null;
	        }

	        return rgbLoadedImage;

	    }
		

		//Phuong thuc doc anh tu 1  duong dan cua anh
			
		public Mat loadImageAndCrop(String p) {
			Mat m = null;				
			Detector det = null;
			Mat faces = null;
	        Rect[] eyes = null;
	        Mat out = null;
						
			//m = Highgui.imread(p);
	        m =  loadImageFromFile(p);
			det = new Detector(m);
			try {
				faces = det.facDet(this.mJavaDetector)[0];
		        if(faces != null) {
		        	eyes = det.eyeDet(this.mJavaDetectorEye, faces);
		        	det.rotationImage(det.getMatSRC(), det.getMatDST(), eyes);
		        }
		       
		        out = det.facesDetection1(this.mJavaDetector); 
		     
		        System.out.printf("\n width = %d, height = %d", out.width(), out.height());
		        
		       // Imgproc.cvtColor(out, out, Imgproc.COLOR_RGB2GRAY);
		        Imgproc.equalizeHist(out, out);
		        Size sz = new Size(92,112);			 
				Imgproc.resize(out, out, sz);
				System.out.printf("\n Load image success");
		        
			} catch (Exception e) {
				System.out.printf("\n Load image failed");
			}
	        
	       
			
			return out;
			
		}
		
		//Phuong thuc doc anh tu 1  duong dan cua anh
		
		public Mat loadImage(String p) {
			Mat m = null;				
			Detector det = null;					
						
			//m = Highgui.imread(p);
			 m =  loadImageFromFile(p);
			
	        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
	        Imgproc.equalizeHist(m, m);
	        Size sz = new Size(92,112);			 
			Imgproc.resize(m, m, sz);
			System.out.printf("\n Load image success");
	  
	       
			
			return m;
			
		}
		
		//-----------------------------------------------------------
			public Mat[] loadImageAndCrop(String[] p) {
				Mat m = null;					
				int d = p.length;
				Mat[] out = new Mat[d];
				
				Detector det = new Detector();
				Mat faces = null;
		        Rect[] eyes;
		        boolean b = true;
		        
		       			            
				
				for(int i = 0; i < p.length; i++) {
					//m = Highgui.imread(p[i]);	
					 m =  loadImageFromFile(p[i]);
			
					det.setMatDST(m);
					det.setMatSRC(m);
					
					System.out.printf("\n Load image %d", i);
					try {
						if (det.facDet(this.mJavaDetector) == null ) {
							b = false;
					//		System.out.printf("\n i = %d, b = %b", i, b);
				        	continue;
				        	
				        }
					
						if (det.facDet(this.mJavaDetector).length == 0 ) {
				        	continue;
				        }
					
				        
					//	System.out.printf("\n ---length = " + det.facesDetection(this.mJavaDetector).length);
						if (det.facDet(this.mJavaDetector).length > 0 ) {
							faces = det.facDet(this.mJavaDetector)[0];
						}
				//		System.out.printf("\n ---i2 = %d", i);
				        if(faces != null) {
				        	eyes = det.eyeDet(this.mJavaDetectorEye, faces);
				        	det.rotationImage(det.getMatSRC(),det.getMatDST(), eyes);
				        }
			      
				        if (det.facDet(this.mJavaDetector) == null) {
				        	continue;
				        }
			       
			       
				        if (det.facDet(this.mJavaDetector).length == 0 ) {
				        	continue;
				        }
			       
			        	out[i] = det.facesDetection1(this.mJavaDetector); 
			        	
			       //  	Imgproc.cvtColor(out[i], out[i], Imgproc.COLOR_RGB2GRAY);
			        	Imgproc.equalizeHist(out[i], out[i]);
					    Size sz = new Size(92, 112);			 
					    Imgproc.resize(out[i], out[i], sz);
							
			        } catch (Exception e) {
			        	System.out.printf("\n Load image %d failed", i);
			        	continue;
			        }
			       
				//	System.out.printf("\n width = %d, height = %d", m.width(), m.height());
				}
				System.out.println("\n");
				
				return out;
				
			}
			//-----------------------------------------------
			
			public Mat[] loadImage(String[] p) {
				Mat m = null;					
				int d = p.length;
				Mat[] out = new Mat[d];
				
				
		       			            
				
				for(int i = 0; i < p.length; i++) {
					//out[i] = Highgui.imread(p[i]);
					out[i] = loadImageFromFile(p[i]);		
							
		         	Imgproc.cvtColor(out[i], out[i], Imgproc.COLOR_RGB2GRAY);
		        	Imgproc.equalizeHist(out[i], out[i]);
				    Size sz = new Size(92, 112);			 
				    Imgproc.resize(out[i], out[i], sz);
				}
				return out;
				
			}
	
	//-----------------------------------------------------------------------------------------		
			
		
			
			//Phuong thuc phan lop 1 anh:
			public boolean doClassification(double[] input, double[][] w,
					                     double err, int labelTest) 
			{
			
				int neuralHideNumber = this.neuralNumber - 1;
				boolean b = true;
				int d1, d2;
				double[] inputNeuralOut, arrayO1, arrayO2;
			//	double[][] weight;
				double out = -1;
				int inputNumber = input.length;
				NeuralNetwork nnk = new NeuralNetwork(neuralHideNumber, inputNumber);
				for(int i = 0; i < w.length; i++) 
				{
					nnk.getNeural()[i].setW(w[i]);
				}
				
				
				nrh = nnk.getHideNeuralNumber();
				
				inputNeuralOut = new double[nrh + 1];			
				
				for(int i = 0; i < nrh; i++) {
					inputNeuralOut[i] = 0.0;
				}
				inputNeuralOut[nrh] = 1;
				
			
				//---------Tinh toan voi dau vao la mang tin hieu m:
				
					//Tinh gia tri dau ra cua cac noron an:
					for(int j = 0; j < nrh; j++) {
						
						inputNeuralOut[j] = nnk.getNeural()[j].computingOutput(input);
						//					
						
					}
					//Tinh gia tri dau ra cua noron xuat:
					out = nnk.getNeural()[nrh].computingOutput(inputNeuralOut);		
				
		
				System.out.printf("\n Output = %e", out);
				System.out.printf("\n 1 - Out = %f", Math.abs(1 - out));
				//err = 0.052;
				
				System.out.printf("\n label:  %d \n", labelTest);
				
				if (labelTest == 1) {
					if (Math.abs(labelTest - out) <= err) {
						b = true;
					} else {
						b = false;
					}
				}				
				if (labelTest == 0) {
					if (Math.abs(1 - out) <= err) {
						b = false;
					} else {
						b = true;
					}
				}
				
				
				return b;
				
			}
			
		
			

}
