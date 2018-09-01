package trainig_pca;


import java.io.File;
import java.util.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.MatOfByte;

import java.io.IOException;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Main 
{
	
	static {
        // Load the OpenCV DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
	
	public static void imshow(String title, Mat img) {
	        
	        // Convert image Mat to a jpeg
	        MatOfByte imageBytes = new MatOfByte();
	        Imgcodecs.imencode(".jpg", img, imageBytes);
	        
	        try {
	            // Put the jpeg bytes into a JFrame window and show.
	            JFrame frame = new JFrame(title);
	            frame.getContentPane().add(new JLabel(new ImageIcon(ImageIO.read(
	            		new ByteArrayInputStream(imageBytes.toArray())))));
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.pack();
	            frame.setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	
	
	public static void main( String[] args ) throws IOException 
	{	
		
		    int trainNumber = 7; 
		    //So anh huan luyen cho 1 nguoi.
		    int vectorLength0 = 10304;
		    //   int vectorLength1 = 22500;
		    int inputNumber = trainNumber + 1;
		    //  int vectorLength1 = 22500;
		    int loopNumber = 0;
		    boolean b = false;
		    
		    int intID = -1;               //-------------
		    
		    Scanner input = new Scanner(System.in);
		    
		    do {
		    	try {
		    		System.out.println("Nhap vao gia tri the hien thu tu cua nguoi co anh can huan luyen: ");
		    		intID = input.nextInt();
		    		System.out.println("Nhap vao so vong lap huan luyen: \n (Nen nhap 200000)");
		    		loopNumber = input.nextInt();//Nen nhap 200000.
		    		b = true;
		    	} catch(Exception e) {
		    		b = false;
		    		System.out.println("\n Nhap sai kieu du lieu. Nhap lai: \n");
		    	}
		    } while (b == false);
		    input.close();

		    
		    String id = String.valueOf(intID);
            String pathTrain0 = "D:\\PCAFile\\att_faces\\orl_faces\\s" + id + "\\";         
            String pathTest = "D:\\PCAFile\\PCATestSelect\\" + id + "\\";
            String[] pathTrainArray = new String[trainNumber];
            int testNumber = 20;
            String[] pathTestArray = new String[testNumber];
            int[] imgTestLabels = new int[testNumber];
            int[] imgTrainLabels = new int[trainNumber];
            double errAccept = 0.055;
         
            int count;
            
            int imgRows0 = 112;
            int imgCols0 = 92;
          //  int imgRows1 = 133;
          //  int imgCols1 = 100;
            Mat[] trainFaces = new Mat[trainNumber];
            double[][] trainDouble = new double[trainNumber][vectorLength0];  
            Mat[] testFaces = new Mat[testNumber];
            double[][] testDouble = new double[testNumber][vectorLength0];  
        
		    Mat eigenVectors = new Mat();
		    Mat mean = new Mat();		   
		    Mat rstCollectionTrain = new Mat();	//Cac vector cua cac anh trong khong gian moi	   
		    Mat collectionTrain = new Mat(trainNumber, vectorLength0, 0); //cac vector cua cac anh huan luyen.
		    Mat rstCollectionTest = new Mat();	//Cac vector cua cac anh trong khong gian moi	   
		    Mat collectionTest = new Mat(testNumber, vectorLength0, 0); //cac vector cua cac anh test.
		 
		    Mat res0 = new Mat(imgRows0, imgCols0, 0);
		    Mat res1 = new Mat(imgRows0, imgCols0, 0);
		    Mat med = new Mat(imgRows0, imgCols0, 0);
		    
		   
		    String directoryName = "D:/PCAFile/dir" + id + "/"; 
		    
		    File directory_1 = new File(directoryName); // khai bao 1 folder ten la new trong project	
		    if (!directory_1.exists()) {
	        
		    	directory_1.mkdir(); 
		    }
		    
		    int nrh = trainNumber * 2;//So noron an
			Traning training = new Traning();
		    
		    
		  
		    for (int i = 1; i <= trainNumber; i++) {
		    //	pathArray[i-1] = path1 + String.valueOf(i) + ".jpg";
		    	pathTrainArray[i-1] = pathTrain0 + String.valueOf(i) + ".pgm";		    
		    	
		    }	
		    
		    for (int i = 1; i <= testNumber; i++) {
			   
			    	pathTestArray[i-1] = pathTest + String.valueOf(i) + ".pgm";		    
			    	
			    }	
		    
		  //Chon loadImage hoac loadImageAndCrop phu hop voi anh
		    trainFaces = training.loadImage(pathTrainArray);	//Khong cat anh
		//    matFaces = training.loadImageAndCrop(pathArray);         //Co cat anh
		    
		    for (int i = 0; i < trainNumber; i++) {
		    	String s = String.valueOf(i);
		    	try {
		    		imshow(s, trainFaces[i]);
		    	} catch (Exception e) {
		    		continue;
		    	}
		    }
		    
		    //Chon loadImage0 hoac loadImage1 phu hop voi anh
		    testFaces = training.loadImage(pathTestArray);	//Khong cat anh
		//    testFaces = training.loadImage(pathArray);         //Co cat anh
		    
		    for (int i = 0; i < testNumber; i++) {
		    	String s = String.valueOf(i);
		    	try {
		    		imshow(s, testFaces[i]);
		    	} catch (Exception e) {
		    		continue;
		    	}
		    }
		    
		    
		    
		    count = 0;
		    for (int i = 0; i < trainNumber; i++) {
		    	count = 0;
		    	for (int j = 0; j < trainFaces[i].rows(); j++) {
		    		for (int k = 0; k < trainFaces[i].cols(); k++) {
		    			trainDouble[i][count] = trainFaces[i].get(j, k)[0];
		    			count++;
		    		}
		    	}
		    }
		    
	//	    System.out.printf("\n");
		    for (int i = 0; i < trainNumber; i++) {
		    	for (int j = 0; j < trainDouble[i].length; j++) {
		    		collectionTrain.put(i, j, trainDouble[i][j]);
		    		
		    	//	System.out.printf("collection = %f ", collection.get(i, j)[0]);
		    	}
		    //	System.out.printf("\n");
		    }
		    
		
		    Core.PCACompute(collectionTrain, mean, eigenVectors);
		   
		    count = 0;
		    for (int i = 0; i < testNumber; i++) {
		    	count = 0;
		    	for (int j = 0; j < testFaces[i].rows(); j++) {
		    		for (int k = 0; k < testFaces[i].cols(); k++) {
		    			testDouble[i][count] = testFaces[i].get(j, k)[0];
		    			count++;
		    		}
		    	}
		    }
		    
	//	    System.out.printf("\n");
		    for (int i = 0; i < testNumber; i++) {
		    	for (int j = 0; j < testDouble[i].length; j++) {
		    		collectionTest.put(i, j, testDouble[i][j]);
		    		
		    	//	System.out.printf("collection = %f ", collection.get(i, j)[0]);
		    	}
		    //	System.out.printf("\n");
		    }
		    
		
		    System.out.printf("\n mean.rows = %d", mean.rows());
		    System.out.printf("\n mean.cols = %d", mean.cols());
		    System.out.printf("\n eigenVector.rows = %d", eigenVectors.rows());
		    System.out.printf("\n eigenVector.cols = %d", eigenVectors.cols());
		 
		    count = 0;
		    for (int i = 0; i < res0.rows(); i++) {
		    	for (int j = 0; j < res0.cols(); j++) {
		    		res0.put(i, j, 10 * (100 * eigenVectors.get(0, count)[0] + 10));
		    	
		    		count++;
		    	}
		    }
		    
		    count = 0;
		    for (int i = 0; i < res1.rows(); i++) {
		    	for (int j = 0; j < res1.cols(); j++) {		    		
		    		res1.put(i, j, 10 * (100 * eigenVectors.get(2, count)[0] + 10));		    	
		    		count++;
		    	}
		    }
		    
		  
		    count = 0;
		    for (int i = 0; i < med.rows(); i++) {
		    	for (int j = 0; j < med.cols(); j++) {
		    		med.put(i, j, mean.get(0, count)[0]);
		    		count++;
		    	}
		    }		 
		    
		    
		
			org.opencv.core.Core.PCAProject(collectionTrain, mean, eigenVectors, rstCollectionTrain);
			org.opencv.core.Core.PCAProject(collectionTest, mean, eigenVectors, rstCollectionTest);
			
			  
		    System.out.printf("\n mean.rows = %d", mean.rows());
		    System.out.printf("\n mean.cols = %d", mean.cols());
		    System.out.printf("\n eigenVector.rows = %d", eigenVectors.rows());
		    System.out.printf("\n eigenVector.cols = %d", eigenVectors.cols());
		    System.out.printf("\n rstCollectionTrain.rows = %d", rstCollectionTrain.rows());
		    System.out.printf("\n rstCollectionTrain.cols = %d", rstCollectionTrain.cols());		
		    System.out.printf("\n rstCollectionTest.rows = %d", rstCollectionTest.rows());
		    System.out.printf("\n rstCollectionTest.cols = %d", rstCollectionTest.cols());		
		
		    int selectNumber = 5;
			int selectNetNumber = 5;
			int finalNetNumber = 1;
			double[][] weight1Net = new double[nrh + 1][];
			double[][][] weightManyNet = new double[selectNetNumber][nrh + 1][];
			String weightFileName = directoryName + "weightFile.text";
			String meanVectorFile = directoryName + "meanVectorFile.text";
			String eigenVectorsFile = directoryName + "eigenVectorsFile.text";
			double[][] trainVectors = new double[trainNumber][inputNumber];//mang con co mot toa do bang 1
			double[][] testVectors = new double[testNumber][inputNumber];//mang con mot toa do bang 1
			
		
			SelectElements se = new SelectElements();
			int neuralNetworksNumber = 10000;
			
			NeuralNetwork[] srcNetsArray = new NeuralNetwork[neuralNetworksNumber];
			
			NeuralNetwork[] selectNetsArray = new NeuralNetwork[selectNumber];
			
			for (int i = 0; i < testNumber; i++) {
				if (i < testNumber - 3) {
					imgTestLabels[i] = 0; //Anh cua nhung nguoi khac
				} else {
					imgTestLabels[i] = 1;//Anh cua nguoi dang nhan dang
				}
			}
			
			for (int i = 0; i < testNumber; i++) {
				for (int j = 0; j < inputNumber - 1; j++) {
					testVectors[i][j] =  rstCollectionTest.get(i, j)[0];
				}
				testVectors[i][inputNumber - 1] = 1;
			}
			
			//Chuan hoa vector:
			ComputMatrix cm = new ComputMatrix();
			
			for (int i = 0; i < testVectors.length; i++) {
				cm.standardizeVector(testVectors[i]);
			}
			
			//Sinh 100 mang noron:
			srcNetsArray = se.generateNeuralNetworks(neuralNetworksNumber, nrh, inputNumber);
			//Chon 5 mang noron tot nhat tu cac mang tren:
			selectNetsArray = se.selectNeuralNetworks(srcNetsArray, selectNumber,
					                                  testVectors, imgTestLabels, errAccept);
			
			
		
			
			for (int i = 0; i < trainNumber; i++) {
				if (i == 3) 
				{
					imgTrainLabels[i] = 0; //Anh cua nhung nguoi khac
				} else {
					imgTrainLabels[i] = 1;//Anh cua nguoi dang nhan dang
				}
			}
			
			
			
			for (int i = 0; i < trainNumber; i++) {
				for (int j = 0; j < inputNumber - 1; j++) {
					trainVectors[i][j] = rstCollectionTrain.get(i, j)[0];
				}
				trainVectors[i][inputNumber - 1] = 1;
			}
			
			NeuralNetwork[] nnk = new NeuralNetwork[1];	
			
			//Chuan hoa vetor:
			for (int i = 0; i < trainVectors.length; i++) {
				cm.standardizeVector(trainVectors[i]);
			}
			
			//Huan luyen 5 mang noron vua chon:
			for (int i = 0; i < selectNetsArray.length; i++) 
			{				
				weightManyNet[i] = training.trainingNeuralNetwork(trainVectors, imgTrainLabels, 
						                                          selectNetsArray[i], loopNumber);
			}
			
			System.out.print("\n cac sai so max cua cac mang noron: \n");
			for (int i = 0; i < 5; i++) 
			{
				System.out.printf("\n Error cua mang %d = %f ", i, training.getNetErrArray()[i]);
			}
			
			System.out.printf("\n Tong so chu ki 5 mang da thuc hien = : %d \n", training.getCount1());
			
			//Chon 1 mang noron tot nhat tu nhung mang noron da duoc huan luyen tren:
			nnk = se.selectNeuralNetworks(selectNetsArray, finalNetNumber, 
					                                      testVectors, imgTestLabels, errAccept);
			
			for(int i = 0; i <= nrh; i++) 
			{
				// System.out.printf("\n i = %d", i);
				weight1Net[i] = nnk[0].getNeural()[i].getW();	
			}
			//Ghi cac doi tuong ra file:
			training.writeArrayWeight(weight1Net, weightFileName, errAccept);
			training.writePixcelsMatrix(mean, meanVectorFile);
			training.writePixcelsMatrix(eigenVectors, eigenVectorsFile);			
	
		    //imshow("src01", matFaces[0]);
		    imshow("eigenVectors", eigenVectors);		 
		    imshow("mean", mean);		
		    imshow("collection", collectionTrain);	
		    imshow("res0", res0);	
		    imshow("res1", res1);	
		    imshow("med", med);	
		    
		   
		    return;
		   
		   
		
	}

}


