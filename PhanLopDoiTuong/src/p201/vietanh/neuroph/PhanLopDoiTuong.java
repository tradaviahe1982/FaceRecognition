package p201.vietanh.neuroph;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TrainingSetImport;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.core.learning.SupervisedLearning;
public class PhanLopDoiTuong 
{
	//
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		String trainingSetFileName = "D:\\PCAFile\\dir82\\train_37_5.txt";
        int inputsCount = 38;
        int outputsCount = 7;
        // create training set
        DataSet trainingSet = null;
        try 
        {
            trainingSet = TrainingSetImport.importFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
        } 
        catch (FileNotFoundException ex) 
        {
            System.out.println("File không tìm thấy!");
        } 
        catch (IOException | NumberFormatException ex) 
        {
            System.out.println(" Lỗi định dạng file hoặc định dạng số!");
        }
        
        System.out.println(" Tạo mạng neuron!");
        NeuralNetwork<BackPropagation> neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 38, 76, 7);
        //
        int maxIterations = 999999;
		double learningRate = 0.1;
		double maxError = 0.0001;
		SupervisedLearning learningRule = neuralNet.getLearningRule();
		learningRule.setMaxError(maxError);
		learningRule.setLearningRate(learningRate);
		learningRule.setMaxIterations(maxIterations);
		learningRule.addListener(new LearningEventListener() 
		{
			public void handleLearningEvent(LearningEvent learningEvent) 
			{
				SupervisedLearning rule = (SupervisedLearning) learningEvent.getSource();
				System.out.println("Network error for interation "
						+ rule.getCurrentIteration() + ": "
						+ rule.getTotalNetworkError());
			}
		});
        System.out.println(" Đang huấn luyện mạng neural...");
        neuralNet.learn(trainingSet);
        testFaceRecognition(neuralNet, trainingSet);
        System.out.println("Kết thúc!");
        System.out.println("Hiển thị đầu ra mạng Neural ...");
        neuralNet.save("D:\\PCAFile\\dir82\\face_pm3_reg7_35.nnet");
		/*NeuralNetwork nn = NeuralNetwork.createFromFile("D:\\PCAFile\\dir45\\face_reg10.nnet");
		//
		
        //
        System.out.println("Ket qua Test!");
        //
        double[] vietanh = {3.8085675965531446E-4, 0.9670314816639497, 0.4625472754532319, 0.8875874061137455, 0.9996191432403447, 0.7695855525277308, 0.7377497525351422, 0.8414878856771287, 0.7635388721316082, 0.9693201651212795, 1.0};
        nn.setInput(vietanh);
        nn.calculate();
        double[] ketqua_vietanh = nn.getOutput();
        System.out.println(Arrays.toString(ketqua_vietanh));*/
        //
	}
	public static void testFaceRecognition(NeuralNetwork<BackPropagation> nnet, DataSet dset) 
	{
        for (DataSetRow trainingElement : dset.getRows()) 
        {
            nnet.setInput(trainingElement.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }
}
