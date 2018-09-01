package trainig_pca;

import java.io.*;
import java.util.*;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Core;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Traning {
	
	private  CascadeClassifier      mJavaDetector; 	 	
	private  CascadeClassifier      mJavaDetectorEye;
	private  CascadeClassifier      mJavaDetectorMouth; 
	private  CascadeClassifier      mJavaDetectorNose;  
	
	 
	 private long count1 = 0;
	 private int count2 = 0;
	 private double[] netErrArray = new double[10];
	 private int nrh;
	 
     public Mat matimg;
	 public Mat[] faces;
	 private  double err = 0;
	 private  double errMax0 = 1, errMax1 = 0;
	 private int neuralNumber;
	 private double[][] weightArray;
	 private String[] line;
	 
	 //Ham tao khong tham so:
	 public Traning() {
		
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
		       e.printStackTrace();
		       System.out.println("Failed to load cascade classifier");
		       
		   }
	 }
	 
	
	
	public int getNrh() {
		return nrh;
	}
	
	

			
		public double[] getNetErrArray() {
		return netErrArray;
	}



	public void setNetErrArray(double[] netErrArray) {
		this.netErrArray = netErrArray;
	}
	
	
		public long getCount1() {
		return count1;
	}



	public void setCount1(long count1) {
		this.count1 = count1;
	}


	//--------------------------------------------------------------------------------
	public static Mat loadImageFromFile(String p) {
    	
       
     
        Mat image = Imgcodecs.imread(p);

        

        return image;

    }
	
	//Phuong thuc doc anh tu 1  duong dan cua anh
	
			public Mat loadImage(String p) {
				Mat m = null;				
				//Detector det = null;					
							
				//m = Highgui.imread(p);
				m = loadImageFromFile(p);
				
		        
		       
				
				return m;
				
			}
			
			//-----------------------------------------------
			public Mat[] loadImage(String[] p) {
				Mat m = null;					
				int d = p.length;
				Mat[] out = new Mat[d];
				
				
		       			            
				
				for(int i = 0; i < p.length; i++) {
					//out[i] = Highgui.imread(p[i]);
					out[i] = loadImageFromFile(p[i]);
							
		         	
				}
				return out;
				
			}
			
	//Phuong thuc doc anh tu 1  duong dan cua anh
		public Mat loadImageAndCrop(String p) {
			Mat m = null;				
			Detector det = null;
			Mat faces = null;
	        Rect[] eyes = null;
	        Mat out = null;
						
			//m = Highgui.imread(p);
	        m = loadImageFromFile(p);
			det = new Detector(m);
			try {
				faces = det.facDet(this.mJavaDetector)[0];
		        if(faces != null) {
		        	eyes = det.eyeDet(this.mJavaDetectorEye, faces);
		        	det.rotationImage(det.getMatSRC(), det.getMatDST(), eyes);
		        }
		       
		        out = det.facesDetection1(this.mJavaDetector); 
		     
		  //      System.out.printf("\n width = %d, height = %d", out.width(), out.height());
		        
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
					m = loadImageFromFile(p[i]);
			
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
			
	
	
		
		//****************Phuong thuc huan luyen 3 tham so mang noron:   ************************
				public double[][] trainingNeuralNetwork(double[][] input, int[] imgTrainLabels, 
						                                NeuralNetwork nnk, int loopsNumberLimit) {
					boolean b = true;					
					double[] inputNeuralOut, arrayO;
					double errAvg0 = 1;
					double errAvg1 = 0;
					this.nrh = 	nnk.getHideNeuralNumber();	
					int inputNumber = input[0].length;
					int objNumber = input.length;
					double[][] weight = new double[nrh + 1][];	
					
					
					arrayO = new double[objNumber];
								
					
					inputNeuralOut = new double[nrh + 1];
					
					for(int i = 0; i < nrh; i++) {
						inputNeuralOut[i] = 0.0;
					}
					inputNeuralOut[nrh] = 1;
					
					
					
					long count = 0;
					
					do {
					
						count++;
						b = true;
						errAvg1 = 0;
						
						//---------Tinh toan voi dau vao la mang cac mang tin hieu:
						for(int i = 0; i < objNumber; i++) {
							
							
							//Tinh gia tri dau ra cua cac noron an:
							for(int j = 0; j < nrh; j++) {
								
								inputNeuralOut[j] = nnk.getNeural()[j].computingOutput(input[i]);
								//					
								
							}
							//Tinh gia tri dau ra cua noron xuat:
							arrayO[i] = nnk.getNeural()[nrh].computingOutput(inputNeuralOut);		
							
							
							//Tinh gia tri denta cua noron xuat
							
							double out;
							
							
							out = nnk.getNeural()[nrh].dentaNeuralOutput(imgTrainLabels[i]);
							
							
							//System.out.printf("\n denta[%d] = %e", i, out);
							System.out.printf("\n 1 - out[%d] = %e", i, 1 - arrayO[i]);
						
						//	double out = nnk.getNeural()[nrh].dentaNeuralOutput(0.0);
							
							if (nnk.getNeural()[nrh].isAccept() == false) {
								b = false;
							}
							
							
							//Tinh gia tri denta cua cac noron an:
							double[] w = nnk.getNeural()[nrh].getW();
							for(int j = 0; j < nrh; j ++) {
								
								nnk.getNeural()[j].dentaNeuralHide(w[j], out);
							}
							
							//Cap nhat trong so cua cac noron cua mang noron:
							  //Cap nhat cac trong so cua cac noron an:
							for (int j = 0; j < nrh; j++) {
								nnk.getNeural()[j].updateTheWeight(input[i]);
							}
							  //Cap nhat trong so cua neuron xuat:
							nnk.getNeural()[nrh].updateTheWeight(inputNeuralOut);
							
						}
						//----------------------------------------------------------
										
						
						/*
						errMax1 = 1 - arrayO[0];						
						for(int i = 0; i <  objNumber; i++) {
							if (i != 3 && errMax1 < 1 - arrayO[i]) {
								errMax1 = 1 - arrayO[i];
								
							}
							
						}
						*/
						
											
						for(int i = 0; i <  objNumber; i++) {
							if (i != 3) {
								errAvg1 += 1 - arrayO[i];
								
							} else {
								errAvg1 += arrayO[i] - 0;
							}
						}
						errAvg1 /= objNumber;
						
						System.out.printf("\n errAvg = %e", errAvg1);
						
						if (errAvg1 > errAvg0) {
							errAvg0 = errAvg1;							
							break; //errMax1 > errMax0 thi se ngung huan luyen.
						} else {
							errAvg0 = errAvg1;
							
						}
						
					this.count1++;//dem so vong lap while
					
					System.out.print("\n Loop: " + String.valueOf(count));	
						
					} while (b == false && count < loopsNumberLimit);
					
					for(int i = 0; i < objNumber; i++) {
						
						System.out.printf("\n arrayO[%d] = %e", i, arrayO[i]);
					}
					System.out.printf("\n erMax = %e", errAvg1);
					this.netErrArray[this.count2] = errAvg1;
					
					//Lay mang cac mang trong so cua cac noron trong mang noron:
					for(int i = 0; i <= nrh; i++) {
						weight[i] = nnk.getNeural()[i].getW();	
					}
					
					this.count2++;//dem so lan goi phuong thuc nay.
					
					return weight;
					
					
				}
				
	//---------------------------------------------------------------------------------			
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
							
		
		
		
	
	//Phuong thuc ghi mang cac mang trong so vao file dang .txt:
	public void writeArrayWeight(double[][] aw, String fileName, double e) throws IOException {
		   int d1, d2;
		   
		   d1 = aw.length;		   
		
			FileOutputStream f = new FileOutputStream(fileName);
			PrintWriter output = new PrintWriter(f);
			
			output.println(this.nrh);
			output.println(e);
			for(int i = 0; i < d1; i++) {
				for (int j = 0; j < aw[i].length; j++) {
					output.print(aw[i][j]);
					output.print(",");
					
				}
				output.println();
				
			}
			
			output.close();
				
	}
	
	//Phuong thuc ghi mang cac mang trong so vao file dang .txt:
		public void writePixcelsMatrix(Mat mat, String fileName) throws IOException {
			   //int d1, d2;			   
			  
				FileOutputStream f = new FileOutputStream(fileName);
				PrintWriter output = new PrintWriter(f);
				
				output.println(mat.rows());
				output.println(mat.cols());
				for(int i = 0; i < mat.rows(); i++) 
				{
					for (int j = 0; j < mat.cols(); j++) 
					{
						output.print(mat.get(i, j)[0]);
						output.print(",");
						
					}
					output.println();
					
				}
				
				output.close();
					
		}
		
		
		
	
}
