package trainig_pca;




import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.Point;




public class Detector {
	    private Mat matSRC, matDST, matDST2;
	  //  Mat matDST2 = new Mat();
	    private static final String    TAG                 = "OCVSample::Activity";
	    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);   
	    private Rect[] facesArray = null, eyesArray = null;
	    private Rect[]  noseArray = null, mouthArray = null;   
	    private int[] index;	    
	    private static final Scalar    EYE_RECT_COLOR     = new Scalar(0, 255, 255, 255);  
	   	private static final Scalar    MOUTH_RECT_COLOR     = new Scalar(255, 255, 255, 255);   
	 	private static final Scalar    NOSE_RECT_COLOR     = new Scalar(0, 255, 0, 255);    
	 	private Point faceCenter = new Point();
	 	private Point faceTopLeft = new Point();
	 	private Point noseCenter = new Point();
	 	private Point mouthCenter = new Point();
	 	private Point leftMouth = new Point();
	 	private Point rightMouth = new Point();
	 	private Point leftEye = new Point();
	 	private Point rightEye = new Point();
    	
	 	
	 	public boolean r = false;
	 	
	//Ham sap xep:
	public void quickSort(Rect[] a1, int[] index1, int l, int r) {

	    int t;
	    double key = a1[index1[(l+r)/2]].y;
	    int i = l, j = r;
	 
	    while(i <= j)
	    {
	        while(a1[index1[i]].y < key) i++;      
	        while(a1[index1[j]].y > key) j--;       
	        if(i <= j)
	        {
	            if (i < j) 
	            {
	              t = index1[i];
	              index1[i] = index1[j];
	              index1[j] = t;
	            }
	            i++;
	            j--;
	        }
	    }
	   
	    if (l < j) quickSort(a1, index1, l, j);   
	    if (i < r) quickSort(a1, index1, i, r);
	}   
	
	
	//-------------------------------------
	//Ham tao khong tham so:
			public Detector() {
				matSRC = new Mat();			
				matDST = new Mat();
				matDST2 = new Mat();
			}
	
	//Ham tao co tham so:
		public Detector(Mat mt) {
			matSRC = mt;			
			matDST = matSRC.clone();
			matDST2 = matSRC.clone();
		}
		
	//------------------
		
		
		
	public Mat getMatDST () {
		return matDST;
	}
	//----------------------------------------------
	
		public Mat getMatSRC() {
		return matSRC;
	}


	public void setMatSRC(Mat matSRC) {
		this.matSRC = matSRC;
	}


	public void setMatDST(Mat matDST) {
		this.matDST = matDST;
	}


	public void setMatDST2(Mat matDST2) {
		this.matDST2 = matDST2;
	}


		public Mat getMatDST2 () {
			return matDST2;
		}
	//----------------------------------------------
		
		
		public Point getFaceCenter() {
			return faceCenter;
		}


		public void setFaceCenter(Point faceCenter) {
			this.faceCenter = faceCenter;
		}


		public Point getFaceTopLeft() {
			return faceTopLeft;
		}


		public void setFaceTopLeft(Point faceTopLeft) {
			this.faceTopLeft = faceTopLeft;
		}


		public Point getNoseCenter() {
			return noseCenter;
		}


		public void setNoseCenter(Point noseCenter) {
			this.noseCenter = noseCenter;
		}


		public Point getMouthCenter() {
			return mouthCenter;
		}


		public void setMouthCenter(Point mouthCenter) {
			this.mouthCenter = mouthCenter;
		}
		
		
		//--------------------------------------------
	
	
	public Point getLeftMouth() {
			return leftMouth;
		}




		public void setLeftMouth(Point leftMouth) {
			this.leftMouth = leftMouth;
		}




		public Point getRightMouth() {
			return rightMouth;
		}




		public void setRightMouth(Point rightMouth) {
			this.rightMouth = rightMouth;
		}




		public Point getLeftEye() {
			return leftEye;
		}




		public void setLeftEye(Point leftEye) {
			this.leftEye = leftEye;
		}




		public Point getRightEye() {
			return rightEye;
		}




		public void setRightEye(Point rightEye) {
			this.rightEye = rightEye;
		}


//-----------------------------------------------------------------------------
		//Phuong thuc cat anh tu anh truyen vao:
		public Mat cropImage(Mat src, Point tl, Point br) {
			Mat crp;
			int x1, y1, x2, y2;
			
		//	System.out.print("\n" + tl.toString());
		//	System.out.print("\n" + br.toString());
			
			x1 = (int) tl.x;
			x2 = (int) br.x;
			y1 = (int) (tl.y - (br.y - tl.y) / 9.2);
			if (y1 < 0) {
				y1 = 0;
			}
			y2 = (int) (br.y + (br.y - tl.y) / 9.2);
			if (y2 > src.rows() - 1) {
				y2 = src.rows() - 1;
			}
			crp = new Mat(y2 - y1 + 1, x2 - x1 + 1, 0);
			Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);
			for (int i = y1; i <= y2; i++) {
				for (int j = x1; j <= x2; j++) {
					crp.put(i - y1, j - x1, src.get(i, j)[0]);
				}
			}
			return crp;
		}
		
		

	//Faces detection with square
	public Mat[] facesDetection(CascadeClassifier Cas) {
		Mat[] mt;
	    float  mRelativeFaceSize   = 0.05f;
		int mAbsoluteFaceSize   = 0;
		//double c = 1.5;
		
		if (matDST != null) {
            int height = matDST.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }

        }                

        MatOfRect faces = new MatOfRect();
        
        if (Cas != null)
        	Cas.detectMultiScale(matDST, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                      new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());               
     
        facesArray =  faces.toArray();
        //-----------------------------------
        if (facesArray.length > 0) {
	        faceCenter.x = ((facesArray[0].tl().x + facesArray[0].br().x) / 2);
	        faceCenter.y = ((facesArray[0].tl().y + facesArray[0].br().y) / 2);
	        
	        faceTopLeft = facesArray[0].tl();
	        
	        
	        mt = new Mat[facesArray.length]; 
	        for(int i = 0; i < facesArray.length; i++) {
	        	mt[i] = matDST.submat(facesArray[i]);
	        	Imgproc.rectangle(matDST, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
	        }
	        
	        return mt;
        }
            
        return null;
        
	}
	
	//Faces detection with rectangle
		public Mat facesDetection1(CascadeClassifier Cas) {
			Mat[] mt;
		    float  mRelativeFaceSize   = 0.05f;
			int mAbsoluteFaceSize   = 0;
			Point tl, br;
			//double c = 1.5;
			
			if (matDST != null) {
	            int height = matDST.rows();
	            if (Math.round(height * mRelativeFaceSize) > 0) {
	                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
	            }

	        }                

	        MatOfRect faces = new MatOfRect();
	        
	        if (Cas != null)
	        	Cas.detectMultiScale(matDST, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
	                      new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());               
	     
	        facesArray =  faces.toArray();
	        tl = facesArray[0].tl();
	        br = facesArray[0].br();
	        //-----------------------------------
	        if (facesArray.length > 0) {
		        Mat face = cropImage(matDST, tl, br);
		        return face;
	        }
	            
	        return null;
	        
		}
		


		//-------------Faces detection------------------------------------
		public Mat[] facDet(CascadeClassifier Cas) {
			Mat[] mt;
		    float  mRelativeFaceSize   = 0.05f;
			int mAbsoluteFaceSize   = 0;
			
			if (matDST != null) {
	            int height = matDST.rows();
	            if (Math.round(height * mRelativeFaceSize) > 0) {
	                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
	            }

	        }                

	        MatOfRect faces = new MatOfRect();
	        
	        if (Cas != null)
	        	Cas.detectMultiScale(matDST, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
	                      new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());               
	     
	        facesArray =  faces.toArray();
	        //-----------------------------------
	        if (facesArray.length > 0) {
		        
		        
		        mt = new Mat[facesArray.length]; 
		        for(int i = 0; i < facesArray.length; i++) {
		        	mt[i] = matDST.submat(facesArray[i]);
		        	//Core.rectangle(matDST, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
		        }
		        
		        return mt;
	        }
	            
	        return null;
	        
		}
	
		//----------------------Eye Detect-----------------------------	
		public Mat[] eyesDetection(CascadeClassifier Cas, Mat face) {
			Mat[] mt;
		    float  mRelativeEyeSize   = 0.0005f;
			int mAbsoluteEyeSize   = 0;
			Point pnt;
			
	    	if (face != null) {
	            int height = face.rows();
	            if (Math.round(height * mRelativeEyeSize) > 0) {
	                mAbsoluteEyeSize = Math.round(height * mRelativeEyeSize);
	            }

	        }
	      
	        if (mAbsoluteEyeSize == 0) {
	            int height = face.rows();
	            if (Math.round(height * mRelativeEyeSize) > 0) {
	                mAbsoluteEyeSize = Math.round(height * mRelativeEyeSize);
	            }

	        }
	        

	        MatOfRect eyes = new MatOfRect();

	        
	        if (Cas != null) {
	        	Cas.detectMultiScale(face, eyes, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
	                    new Size(mAbsoluteEyeSize, mAbsoluteEyeSize), new Size());
	       // 	Cas.detectMultiScale(face, eyes);
	        }          
	     
	        eyesArray = eyes.toArray();  
	        //--------------------------------------------
	        
	       
	        index = new int[eyesArray.length];
	    	for(int i = 0; i < eyesArray.length; i++) {			
				index[i] = i;
			}
	    	
	    	mt = new Mat[2];
	    	
	        if (eyesArray.length > 2) {       	
	    		
	        	quickSort(eyesArray, index, 0, eyesArray.length - 1);
	        	/*
	        	leftEye.x = (int) ((eyesArray[index[0]].tl().x + eyesArray[index[0]].br().x) / 2);
				leftEye.y = (int) ((eyesArray[index[0]].tl().y + eyesArray[index[0]].br().y) / 2);
				rightEye.x = (int) ((eyesArray[index[1]].tl().x + eyesArray[index[1]].br().x) / 2);
				rightEye.y = (int) ((eyesArray[index[1]].tl().y + eyesArray[index[1]].br().y) / 2);
				
				if (leftEye.x > rightEye.x) {
					pnt = leftEye;
					leftEye = rightEye;
					rightEye = pnt;
				}
				Core.circle(face,  leftEye,2,EYE_RECT_COLOR,1);
				Core.circle(face,  rightEye,2,EYE_RECT_COLOR,1);
	        	*/
	        	for (int i = 0; i < 2; i++) {      		 
	                
	                              
	        		
	        		if (eyesArray[index[1]].y -  eyesArray[index[0]].y >= 15){
	        			mt[0] = matDST.submat(eyesArray[index[0]]);  
	        			Imgproc.rectangle(face, eyesArray[index[0]].tl(), eyesArray[index[0]].br(), EYE_RECT_COLOR, 1);
	        			return null;
	        		} else {
	        			mt[i] = matDST.submat(eyesArray[index[i]]); 
	        			
	        			leftEye.x = ((eyesArray[index[0]].tl().x + eyesArray[index[0]].br().x) / 2);
	    				leftEye.y = ((eyesArray[index[0]].tl().y + eyesArray[index[0]].br().y) / 2);
	    				rightEye.x = ((eyesArray[index[1]].tl().x + eyesArray[index[1]].br().x) / 2);
	    				rightEye.y = ((eyesArray[index[1]].tl().y + eyesArray[index[1]].br().y) / 2);
	    				
	    				if (leftEye.x > rightEye.x) {
	    					pnt = leftEye;
	    					leftEye = rightEye;
	    					rightEye = pnt;
	    				}
	    				Imgproc.circle(face,  leftEye,2,EYE_RECT_COLOR,1);
	    				Imgproc.circle(face,  rightEye,2,EYE_RECT_COLOR,1);
	    				Imgproc.rectangle(face, eyesArray[index[i]].tl(), eyesArray[index[i]].br(), EYE_RECT_COLOR, 1);
	        			//-------------------------------
	        			
	        			
	        			
	        		}
	        	}
	        	return mt;
	        } 
	        for(int i = 0; i < eyesArray.length; i++) {			
				index[i] = i;
			}
	        if (eyesArray.length == 2) {
	        	quickSort(eyesArray, index, 0, eyesArray.length - 1); 
	        	if (eyesArray[index[1]].y -  eyesArray[index[0]].y >= 15){        		
	        		mt[0] = matDST.submat(eyesArray[index[0]]);  
	        		Imgproc.rectangle(face, eyesArray[index[0]].tl(), eyesArray[index[0]].br(), EYE_RECT_COLOR, 1);
	    			return null;
	        	} else {
	        		leftEye.x = ((eyesArray[index[0]].tl().x + eyesArray[index[0]].br().x) / 2);
	    			leftEye.y = ((eyesArray[index[0]].tl().y + eyesArray[index[0]].br().y) / 2);
	    			rightEye.x = ((eyesArray[index[1]].tl().x + eyesArray[index[1]].br().x) / 2);
	    			rightEye.y = ((eyesArray[index[1]].tl().y + eyesArray[index[1]].br().y) / 2);
	    			if (leftEye.x > rightEye.x) {
    					pnt = leftEye;
    					leftEye = rightEye;
    					rightEye = pnt;
    				}
	    			Imgproc.circle(face,  leftEye,2,EYE_RECT_COLOR,1);
	    			Imgproc.circle(face,  rightEye,2,EYE_RECT_COLOR,1);        	
		            for (int i = 0; i < 2; i++) {    		 
		                mt[i] = matDST.submat(eyesArray[index[i]]);
		              //--------------------------------------------
		                Imgproc.rectangle(face, eyesArray[index[i]].tl(), eyesArray[index[i]].br(), EYE_RECT_COLOR, 1);
		                
		            
		            }
		            return mt;
	        	}
	        }
	        return null;
			
		}
		
		//----------------------Eye Detect2-----------------------------	
			public Rect[] eyeDet(CascadeClassifier Cas, Mat face) {
				//Mat[] mt;
			    float  mRelativeEyeSize   = 0.0005f;
				int mAbsoluteEyeSize   = 0;
				
		    	if (face != null) {
		            int height = face.rows();
		            if (Math.round(height * mRelativeEyeSize) > 0) {
		                mAbsoluteEyeSize = Math.round(height * mRelativeEyeSize);
		            }

		        }
		      
		        if (mAbsoluteEyeSize == 0) {
		            int height = face.rows();
		            if (Math.round(height * mRelativeEyeSize) > 0) {
		                mAbsoluteEyeSize = Math.round(height * mRelativeEyeSize);
		            }

		        }
		        

		        MatOfRect eyes = new MatOfRect();

		        
		        if (Cas != null) {
		        	Cas.detectMultiScale(face, eyes, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
		                    new Size(mAbsoluteEyeSize, mAbsoluteEyeSize), new Size());
		        }          
		     
		        eyesArray = eyes.toArray();            
		       
		        index = new int[eyesArray.length];
		    	for(int i = 0; i < eyesArray.length; i++) {			
					index[i] = i;
				}
		    	
		    
		        if (eyesArray.length > 2) {       	
		    		
		        	quickSort(eyesArray, index, 0, eyesArray.length - 1);
		        	
		        	for (int i = 0; i < 2; i++) {      		 
		                
		            
		        		if (eyesArray[index[1]].y -  eyesArray[index[0]].y >= 15){
		        	
		        			return null;
		        		} 
		        	}
		        	return eyesArray;
		        }
		        
		        for(int i = 0; i < eyesArray.length; i++) {			
					index[i] = i;
				}
		        
		        if (eyesArray.length == 2) {
		        	quickSort(eyesArray, index, 0, eyesArray.length - 1); 
		        	if (eyesArray[index[1]].y -  eyesArray[index[0]].y >= 15){        		
		        	
		    			return null;
		        	} else {
		        	
			            
			            return eyesArray;
		        	}
		        }
		        return null;
				
			}
			
		
		
		

	
	//------------Mouth detection------------------------------------------
	public Mat mouthDetection(CascadeClassifier Cas, Mat face) {
		Mat mt;
	    float  mRelativeMouthSize   = 0.001f;
		int mAbsoluteMouthSize   = 0;
		Point center = new Point();
		
		if (face != null) {
            int height = face.rows();
            if (Math.round(height * mRelativeMouthSize) > 0) {
                mAbsoluteMouthSize = Math.round(height * mRelativeMouthSize);
            }

        }         

        MatOfRect mouth = new MatOfRect();

        
        if (Cas != null) {
        	Cas.detectMultiScale(face, mouth, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteMouthSize, mAbsoluteMouthSize), new Size());
        }           
       
        mouthArray = mouth.toArray();        
       
       
  //      Point p = new Point();            
        index = new int[mouthArray.length];
        
    	for(int i = 0; i < mouthArray.length; i++) {			
			index[i] = i;
		}
    	
        if (mouthArray.length > 1) {            	
    		
        	quickSort(mouthArray, index, 0, mouthArray.length - 1);            	
        	Imgproc.rectangle(face, mouthArray[index[mouthArray.length - 1]].tl(), 
        			mouthArray[index[mouthArray.length - 1]].br(), MOUTH_RECT_COLOR, 1);
        	mt = matDST.submat(mouthArray[index[mouthArray.length - 1]]);
        	center.x = ((mouthArray[index[mouthArray.length - 1]].tl().x + 
        			           mouthArray[index[mouthArray.length - 1]].br().x) / 2);
        	center.y = ((mouthArray[index[mouthArray.length - 1]].tl().y + 
        			           mouthArray[index[mouthArray.length - 1]].br().y) / 2);
        	
        	mouthCenter = center;
        	
        	leftMouth.x = mouthArray[index[mouthArray.length - 1]].tl().x;
        	leftMouth.y = center.y;
        	
        	rightMouth.x = mouthArray[index[mouthArray.length - 1]].br().x;
        	rightMouth.y = center.y;
        	
        	
        	Imgproc.circle(face,  center,2, MOUTH_RECT_COLOR,1);
        	return mt;
       
        		
        } 
       
        if (mouthArray.length == 1) {            		
        	Imgproc.rectangle(face, mouthArray[index[0]].tl(), mouthArray[index[0]].br(), MOUTH_RECT_COLOR, 1);
        	mt = matDST.submat(mouthArray[0]);
        	center.x = ((mouthArray[0].tl().x + 
        			          mouthArray[0].br().x) / 2);
        	center.y = ((mouthArray[0].tl().y + 
			                  mouthArray[0].br().y) / 2);
        	mouthCenter = center;
        	
        	leftMouth.x = mouthArray[0].tl().x;
        	leftMouth.y = center.y;
        	
        	rightMouth.x = mouthArray[0].br().x;
        	rightMouth.y = center.y;
        	
        	
        	Imgproc.circle(face,  center,2, MOUTH_RECT_COLOR,1);
        	return mt;
        
        }
        return null;
	}
	
	//-------------------Nose Detect--------------------
    
	public Mat noseDetection(CascadeClassifier Cas, Mat face) {
		Mat mt;
	    float  mRelativeNoseSize   = 0.001f;
		int mAbsoluteNoseSize   = 0;
		Point center = new Point();
		
		
       	if (face != null) {
               int height = face.rows();
               if (Math.round(height * mRelativeNoseSize) > 0) {
                   mAbsoluteNoseSize = Math.round(height * mRelativeNoseSize);
               }

           }          

           MatOfRect nose = new MatOfRect();
           
           if (Cas != null) {
        	   Cas.detectMultiScale(face, nose, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                       new Size(mAbsoluteNoseSize, mAbsoluteNoseSize), new Size());
        	   
        	   
           }           
          
           noseArray = nose.toArray(); 
           if (noseArray.length > 0) {
	           center.x = (int) ((noseArray[0].tl().x + noseArray[0].br().x) / 2);
	           center.y = (int) ((noseArray[0].tl().y + noseArray[0].br().y) / 2);
	           noseCenter = center;	           
	           Imgproc.rectangle(face, noseArray[0].tl(), noseArray[0].br(), NOSE_RECT_COLOR, 1);
	           Imgproc.circle(face,  center,2, NOSE_RECT_COLOR,1);
	           mt = matDST.submat(noseArray[0]);
	       	   return mt;
           }
           return null;
       
    
	}
	//--------------Rotation Image--------------------------
	
			public boolean rotationImage(Mat src, Mat dst, Rect[] eyes) {
				//int x, y;
				double arg = 0, tan = 0;				
				Point leftEye = new Point();
				Point rightEye = new Point();
				Point pnt;
				
				if(eyes == null) {
					r = false;
					return false;
				}
				if(eyes.length < 2) {
					r = false;
					return false;
				}
				
				for(int i = 0; i < eyes.length; i++) {
					index[i] = i;
				}
				quickSort(eyes, index, 0, eyes.length - 1);
				//-----------------------------------------------------
				
				leftEye.x = ((eyes[index[0]].tl().x + eyes[index[0]].br().x) / 2);
    			leftEye.y = ((eyes[index[0]].tl().y + eyes[index[0]].br().y) / 2);
    			rightEye.x = ((eyes[index[1]].tl().x + eyes[index[1]].br().x) / 2);
    			rightEye.y = ((eyes[index[1]].tl().y + eyes[index[1]].br().y) / 2);
    			if (leftEye.x > rightEye.x) {
					pnt = leftEye;
					leftEye = rightEye;
					rightEye = pnt;
				}				
				
		        arg = (double) Math.atan((double) (rightEye.y - leftEye.y) / (rightEye.x - leftEye.x));
		        arg = arg * 180 / 3.14;
		        if (arg < 0) {
		        	arg = 360 + arg;
		        }							
								
				Mat rotImage = Imgproc.getRotationMatrix2D(leftEye, arg, 1.0);						
				
			    Imgproc.warpAffine(src, dst, rotImage, src.size());
			    matDST2 = matDST.clone();
			    r = true;
			    return true;		
				
				
			}
			
	

}
