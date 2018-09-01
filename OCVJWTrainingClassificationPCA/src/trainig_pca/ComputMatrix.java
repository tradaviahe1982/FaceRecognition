package trainig_pca;


import org.opencv.core.Mat;


public class ComputMatrix {
	//Phuong thuc tinh do dai vevtor:
	public  double modul(Mat m1) {
		double d = 0;
		for(int j = 0; j < m1.cols(); j++) {
			d += Math.pow(m1.get(0, j)[0], 2);
		}
		d = Math.sqrt(d);
		return d;
	}
	
	//Phuong thuc tinh muc xam trung binh cua  anh:
	public double pixcelMean(Mat m) {
		double avg = 0;
		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.cols(); j++) {
				avg += m.get(i, j)[0];
			}
		}
		avg = avg / (m.rows() * m.cols());
		return avg;
	}
	
	//Phuong thuc tinh vector cua anh trong khong gian moi:
	public double[] resultVector(Mat img, Mat eigenVectors, Mat avg) {
		int oldCoorNumber = eigenVectors.cols();
		int newCoorNumber = eigenVectors.rows();
		double[] feature = new double[newCoorNumber];
		double[] imgVector = new double[oldCoorNumber];
		double[] den = new double[oldCoorNumber];
		int count;
		
		count = 0;
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				imgVector[count] = img.get(i, j)[0];
				count++;
			}
		}
		
		for (int i = 0; i < oldCoorNumber; i++) {
			den[i] = imgVector[i] - avg.get(0, i)[0];
			
		}
		
		
		for (int i = 0; i < newCoorNumber; i++) {
			feature[i] = 0;
		}
		
		for (int i = 0; i < newCoorNumber; i++) {
			for (int j = 0; j < oldCoorNumber; j++) {
				feature[i] += eigenVectors.get(i, j)[0] * den[j]; 
			}
		}
		
		return feature;
		
	}
	
	//Phuong thuc tinh vector cua anh trong khong gian moi:
		public double[] resultVector1(Mat img, double[][] eigenVectors, Mat avg) 
		{
			int oldCoorNumber = eigenVectors[0].length;
			int newCoorNumber = eigenVectors.length;
			double[] feature = new double[newCoorNumber];
			double[] imgVector = new double[oldCoorNumber];
			double[] den = new double[oldCoorNumber];
			int count;
			
			count = 0;
			for (int i = 0; i < img.rows(); i++) 
			{
				for (int j = 0; j < img.cols(); j++) 
				{
					imgVector[count] = img.get(i, j)[0];
					count++;
				}
			}
			
			for (int i = 0; i < oldCoorNumber; i++) 
			{
				den[i] = imgVector[i] - avg.get(0, i)[0];
				
			}
			
			
			for (int i = 0; i < newCoorNumber; i++) {
				feature[i] = 0;
			}
			
			for (int i = 0; i < newCoorNumber; i++) 
			{
				for (int j = 0; j < oldCoorNumber; j++) 
				{
					feature[i] += eigenVectors[i][j] * den[j]; 
				}
			}
			
			return feature;
			
		}
	//Phuong thuc tao lai anh tu anh trung binh, cac vector rieng va vector ket qua cua anh 
    //trong khong gian moi:
	public Mat computeImageMatrix(int rows, int cols, Mat mean,
			                  Mat eigenVectors, Mat featureVector) 
	{
		
		//Mat img = new Mat(1, mean.cols(), 0);
		Mat image = new Mat(rows, cols, 0);
		double[] vector = new double[mean.cols()];
		int count;
		
		for (int i = 0; i < vector.length; i++) {
			vector[i] = 0;
		}
		
		//Tinh vecto hieu:
		for (int j = 0; j < eigenVectors.cols(); j++) {
			//System.out.printf("\n j = %d", j);
			for (int i = 0; i < eigenVectors.rows(); i++) {
				//System.out.printf("\n i = %d", i);
				vector[j] = vector[j] + featureVector.get(0, i)[0] * eigenVectors.get(i, j)[0];
			}
			vector[j] += mean.get(0, j)[0];
		}
		
		count = 0;
		for (int i = 0; i < image.rows(); i++) {
			for (int j = 0; j < image.cols(); j++) {
				image.put(i, j, vector[count]);
				//image.put(i, j, mean.get(0, count)[0]);
				count++;
				
			}
		}
		
		return image;
	}
	
	//Phuong thuc tinh anh guong cua anh dua vao:
	public Mat[] mirrorImage(Mat[] srcFaces) {
		int srcNumber = srcFaces.length;
		Mat[] dstFaces = new Mat[srcNumber];
		
		for (int i = 0; i < srcFaces.length; i++) {
			dstFaces[i] = new Mat(srcFaces[i].rows(), srcFaces[i].cols(), 0);
			for (int j = 0; j < srcFaces[i].rows(); j++) {
				for (int k = 0; k < srcFaces[i].cols(); k++) {
				    dstFaces[i].put(j, k, srcFaces[i].get(j, srcFaces[i].cols() - 1 - k));
				}
			}
		}
		return dstFaces;
	}
	
	//Phuong thuc chuan hoa vector dau vao sao cho tri tuyet doi cua toa do
	//cua no xap xi 2:
	public double[] stnInputVector(double[] inp) {
		int lth = inp.length;
		double avg = 0;
		double dev = 0;
		double var = 0;
		
		// tinh gia tri trung binh cua cac toa do cua mot vector:
		for (int i = 0; i < lth; i++) {
			avg += inp[i];
		}
		avg = avg / lth;
		
		//Tinh phuong sai cua cac toa do:
		for (int i = 0; i < lth; i++) {
			inp[i] = inp[i] - avg; //Toa do da hieu chinh buoc dau.
			var += Math.pow(inp[i], 2);
		}
		var = var / lth;
		
		//Tinh do lech chuan cua cac toa do:
		dev = Math.sqrt(var);
		
		//Chuan hoa vector input:
		for (int i = 0; i < lth; i++) {
			inp[i] = inp[i] / dev;
		}
		inp[lth - 1] = 1;
		return inp;
	}
	
	//Phuong thuc chuan hoa vector dau vao ve mien gia tri [0, 1]:
			public void standardizeVector(double[] feature) 
			{
				double min, max;
				
				min = feature[0];
				max = feature[0];
				for (int i = 1; i < feature.length - 1; i++) 
				{
					if (min > feature[i]) 
					{
						min = feature[i];
					}
					if (max < feature[i]) 
					{
						max = feature[i];
					}
				}
				
				double ran = max - min + 2;
				
				for (int i = 0; i < feature.length - 1; i++) 
				{
					feature[i] = (feature[i] - min + 1) / ran;
				}
				
				return;
			}
	
	//Phuong thuc tron hai mang anh:
	public Mat[] mergerImages(Mat[] m1, Mat[] m2) {
		int lth1 = m1.length;
		int lth2 = m2.length;
		int lth = lth1 + lth2;
		Mat[] m = new Mat[lth];
		
		for (int i = 0; i < lth1; i++) {
			m[i] = m1[i];
		}
		for (int i = lth1; i < lth; i++) {
			m[i] = m2[i-lth1];
		}
		return m;
	}

}
