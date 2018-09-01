package trainig_pca;

public class SelectElements {
	//Ham sap xep:
		public void quickSort(double[] a, int[] index, int l, int r) {

		    int t;
		    double key = a[index[(l+r)/2]];
		    int i = l, j = r;
		 
		    while(i <= j)
		    {
		        while(a[index[i]] < key) i++;      
		        while(a[index[j]] > key) j--;       
		        if(i <= j)
		        {
		            if (i < j) 
		            {
		              t = index[i];
		              index[i] = index[j];
		              index[j] = t;
		            }
		            i++;
		            j--;
		        }
		    }
		   
		    if (l < j) quickSort(a, index, l, j);   
		    if (i < r) quickSort(a, index, i, r);
		}   
		
		
		//-------------------------------------
	
	//ham sinh ra mot so luong xac dinh cac mang noron:
	public NeuralNetwork[] generateNeuralNetworks (int neuralNetworksNumber, 
			int hideNeuralsNumber, int inputNumber) {
		
		NeuralNetwork[] nns = new NeuralNetwork[neuralNetworksNumber];
		
		for (int i = 0; i < neuralNetworksNumber; i++) {
			nns[i] = new NeuralNetwork(hideNeuralsNumber, inputNumber);
		}
		
		return nns;
		
	}
	
	//Phuong thuc tinh do tot cua mot mang:
	public double goodLevel(NeuralNetwork nnk, double[][] inputArrays, 
			int[] labels, double errMax) {
		
		
		double[] inputNeuralOut;
		double glv = 0;
		int objNumber = inputArrays.length;	
		double out;
		
		int nrh = nnk.getHideNeuralNumber();
		
		inputNeuralOut = new double[nrh + 1];			
		
		for(int i = 0; i < nrh; i++) {
			inputNeuralOut[i] = 0.0;
		}
		inputNeuralOut[nrh] = 1;
		
	
		//---------Tinh toan voi dau vao la mang tin hieu inputArrays:
		//moi vong lap se cho ket qua tinh toan tuong ung voi 1 anh.
		for (int i = 0; i < objNumber; i++) {
			//Tinh gia tri dau ra cua cac noron an:
			for(int j = 0; j < nrh; j++) {
				
				inputNeuralOut[j] = nnk.getNeural()[j].computingOutput(inputArrays[i]);
				
			}
			//Tinh gia tri dau ra cua noron xuat:
			out = nnk.getNeural()[nrh].computingOutput(inputNeuralOut);	
		//	System.out.printf("\n Output voi anh thu %d: out = %f\n", i, out);
			
		    if (Math.abs(out - labels[i]) <= errMax) {
		    	glv += 1;
		    } 
		    glv +=  - Math.abs(out - labels[i]);

		}
		
		
		
		return glv;
		
	}
	
	//Ham chon loc cac mang noron cho ket qua nhan dang tot nhat tu cac mang tren:
	public NeuralNetwork[] selectNeuralNetworks(NeuralNetwork[] nnk, 
			int selectNumber,  double[][] inputArrays, 
			int[] labels, double errMax) {
		
		int lth = nnk.length;
		double[] goodLevelArray = new double[lth];
		NeuralNetwork[] newNNK = new NeuralNetwork[selectNumber];
		
		for (int i = 0; i < lth; i++) {
		//	System.out.printf("\n Mang noron thu %d:\n", i);
			goodLevelArray[i] = goodLevel(nnk[i], inputArrays, labels, errMax);
			
		}
		
		int[] index = new int[lth];
				
		for (int i = 0; i < lth; i++) {
			index[i] = i;
		}
		
		quickSort(goodLevelArray, index, 0, lth - 1);
		
		
		
		for (int i = lth - 1; i >= lth - selectNumber; i--) {
			newNNK[lth - i - 1] = nnk[index[i]];
		}
		
		
		
		if (selectNumber == 1) {
			System.out.printf("\n");
			/*
			for (int i = 0; i < lth; i++) {
				System.out.printf("index[%d] = %d ", i, index[i]);
			}
			*/
			
			for (int i = 0; i < lth; i++) {			
				
				System.out.printf("\n good Level Net[%d] = %f", i, goodLevelArray[i]);				
			}
			System.out.printf("\n Mang noron duoc chon la mang thu: %d \n", index[lth - 1]);
		}
		
		return newNNK;
	}
	

}
