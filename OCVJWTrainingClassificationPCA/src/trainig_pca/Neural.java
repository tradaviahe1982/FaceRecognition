package trainig_pca;


public class Neural {
	private int wNumber;
	private double[] w;
	private double denta;
	private double output;
	private boolean accept = true;
	private double r = 0.1;//Ti le hoc
	private int inputNumber;
	private double e = 0.001;
 
	
	//Ham tao co tham so:
	public  Neural(int inb) {
		this.wNumber = inb;
	//	this.inputNumber = inb;
		this.w = new double[wNumber];
		for(int i = 0; i < wNumber; i++) {
			this.w[i] = Math.random() - 0.5;
		}
	}	
	

	//--------------------------------------------------
	public int getwNumber() {
		return wNumber;
	}
	

	public double getDenta() {
		return denta;
	}

	public void setDenta(double denta) {
		this.denta = denta;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public double[] getW() {
		return w;
	}

	public void setW(double[] w) {
		this.wNumber = w.length;
		this.w = new double[wNumber];
		for(int i = 0; i < this.wNumber; i++) {
			this.w[i] = w[i];
		}
	}
	
	//-------------Cac phuong thuc xu ly:------------------
	
	//Phuong thuc tinh Output:
	public double computingOutput(double[] input) {
		double total = 0.0;
		double lg = 0.0;
		
		for(int i = 0; i < w.length; i++) {
			total += w[i] * input[i];
		}
		
		lg = 1 / (1 + Math.exp(-total));
		output = lg;
		return lg;
	}
	
	
	
	//Phuong thuc tinh  denta  cua noron xuat:
		public  double dentaNeuralOutput(double valueExpect) {
			double dt = 0;
			
		
			if (Math.abs(valueExpect - output) < e) {
				this.accept = true;
			} else {
				this.accept = false;
			}
						
			dt = output * (1 - output) * (valueExpect - output);
			
			denta = dt;
			return dt;		
			
		}
	//Phuong thuc tinh denta cua noron lop an:
	public double dentaNeuralHide(double w, double dentaOut) {
		double dt = 0;		
		
		dt = output * (1 - output) * w * dentaOut;
		denta = dt;
		return dt;
		
	}
	
	//Phuong thuc cap nhat lai cac trong so cua noron:
	public void updateTheWeight(double[] input) {
		for (int i = 0; i < w.length; i++) {
			w[i] = w[i] + r * denta * input[i];	
			
		}
	}
	
		
	

}
