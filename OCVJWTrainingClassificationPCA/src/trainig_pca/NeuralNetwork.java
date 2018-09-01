package trainig_pca;



public class NeuralNetwork {
	private int hideNeuralNumber;
	private Neural[] neural;
	
	
	
	//Ham tao :
	public NeuralNetwork(int hideNeuralNumber, int inputNumber) {
		this.hideNeuralNumber =  hideNeuralNumber;
		this.neural = new Neural[this.hideNeuralNumber + 1];
		for(int i = 0; i < this.hideNeuralNumber; i++) {
			this.neural[i] = new Neural(inputNumber);
			
		}
		this.neural[this.hideNeuralNumber] = new Neural(this.hideNeuralNumber + 1);
		//----------------------
		/*
		for(int i = 0; i < this.hideNeuralNumber; i++) {
			System.out.println(this.neural[this.hideNeuralNumber].getW()[i]);
		}
		*/
	}

	public int getHideNeuralNumber() {
		return hideNeuralNumber;
	}

	public void setHideNeuralNumber(int hideNeuralNumber) {
		this.hideNeuralNumber = hideNeuralNumber;
	}

	public Neural[] getNeural() {
		return neural;
	}

	public void setNeural(Neural[] neural) {
		this.neural = neural;
	}
	
	

}
