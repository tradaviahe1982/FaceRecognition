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
public class ExtractFeature 
{
	static 
	{
        // Load the OpenCV DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
	//
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
	//
	public static void main( String[] args ) throws IOException 
	{	
		
	    int trainNumber = 37; 
	    int vectorLength0 = 10304;
	    int inputNumber = trainNumber + 1;
	    //
	    String id = String.valueOf(82);
        String pathTrain0 = "D:\\PCAFile\\att_faces\\orl_faces\\s" + id + "\\";         
        String[] pathTrainArray = new String[trainNumber];
        int count;
        Mat[] trainFaces = new Mat[trainNumber];
        double[][] trainDouble = new double[trainNumber][vectorLength0];  
	    Mat eigenVectors = new Mat();
	    Mat mean = new Mat();		   
	    Mat rstCollectionTrain = new Mat();	   
	    Mat collectionTrain = new Mat(trainNumber, vectorLength0, 0); 
	    for (int i = 1; i <= trainNumber; i++) 
	    {
	    	pathTrainArray[i-1] = pathTrain0 + String.valueOf(i) + ".pgm";		    
	    }	
	    Traning training = new Traning();
	    trainFaces = training.loadImage(pathTrainArray);	
	    count = 0;
	    for (int i = 0; i < trainNumber; i++) 
	    {
	    	count = 0;
	    	for (int j = 0; j < trainFaces[i].rows(); j++) 
	    	{
	    		for (int k = 0; k < trainFaces[i].cols(); k++) 
	    		{
	    			trainDouble[i][count] = trainFaces[i].get(j, k)[0];
	    			count++;
	    		}
	    	}
	    }
	    for (int i = 0; i < trainNumber; i++) 
	    {
	    	for (int j = 0; j < trainDouble[i].length; j++) 
	    	{
	    		collectionTrain.put(i, j, trainDouble[i][j]);
	    	}
	    }
	    Core.PCACompute(collectionTrain, mean, eigenVectors);
	    System.out.printf("\n mean.rows = %d", mean.rows());
	    System.out.printf("\n mean.cols = %d", mean.cols());
	    System.out.printf("\n eigenVector.rows = %d", eigenVectors.rows());
	    System.out.printf("\n eigenVector.cols = %d", eigenVectors.cols());
		org.opencv.core.Core.PCAProject(collectionTrain, mean, eigenVectors, rstCollectionTrain);
	    System.out.printf("\n mean.rows = %d", mean.rows());
	    System.out.printf("\n mean.cols = %d", mean.cols());
	    System.out.printf("\n eigenVector.rows = %d", eigenVectors.rows());
	    System.out.printf("\n eigenVector.cols = %d", eigenVectors.cols());
	    System.out.printf("\n rstCollectionTrain.rows = %d", rstCollectionTrain.rows());
	    System.out.printf("\n rstCollectionTrain.cols = %d", rstCollectionTrain.cols());		
	    System.out.println("");
		double[][] trainVectors = new double[trainNumber][inputNumber];
		//Chuan hoa vector:
		ComputMatrix cm = new ComputMatrix();
		//
		for (int i = 0; i < trainNumber; i++) 
		{
			for (int j = 0; j < inputNumber - 1; j++) 
			{
				trainVectors[i][j] = rstCollectionTrain.get(i, j)[0];
			}
			trainVectors[i][inputNumber - 1] = 1;
		}
		//
		//Chuan hoa vetor:
		for (int i = 0; i < trainVectors.length; i++) 
		{
			cm.standardizeVector(trainVectors[i]);
		}
		for (int i=0; i < trainVectors.length; i++)
		{
			double[] rows = trainVectors[i];
			System.out.println(Arrays.toString(rows));
		}
		//
		System.out.println("");
		String directoryName = "D:/PCAFile/dir" + id + "/";
		String meanVectorFile = directoryName + "meanVectorFile.text";
		String eigenVectorsFile = directoryName + "eigenVectorsFile.text";
		training.writePixcelsMatrix(mean, meanVectorFile);
		training.writePixcelsMatrix(eigenVectors, eigenVectorsFile);
		//
		/*Classification cla = new Classification();
		Mat mean1 = cla.readVectorsFile("D:\\PCAFile\\dir88\\meanVectorFile.text");
		double[][] eigenVectors1 = cla.readVectorsFile1("D:\\PCAFile\\dir88\\eigenVectorsFile.text");
		//
	    Mat face = training.loadImage("D:\\PCAFile\\att_faces\\orl_faces\\s82\\12.pgm");	
		//
		double[] result1;
		result1 = cm.resultVector1(face, eigenVectors1, mean1);		
		double[] feature = new double[result1.length + 1];	
		for (int j = 0; j < result1.length; j++) 
		{			
			feature[j] = result1[j];								
		}
		feature[result1.length] = 1;		
		cm.standardizeVector(feature);
		//
		for (int i = 0; i < trainNumber; i++) {
	    	String s = String.valueOf(i);
	    	try {
	    		imshow(s, trainFaces[i]);
	    	} catch (Exception e) {
	    		continue;
	    	}
	    }
		//
		System.out.println(Arrays.toString(feature));*/
		//*/
	    return;
	}	
}
