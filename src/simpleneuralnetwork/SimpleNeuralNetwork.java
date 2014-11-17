/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleneuralnetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author philippe
 */
public class SimpleNeuralNetwork {

    public static final Random rand = new Random();
    private final int inputNb, outputNb;
    List<Neuron> inputLayer, outputLayer;
    List<List<Neuron>> hiddenLayers;
    public static DecimalFormat df;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int iter = 50;
        int[] trainSizes = new int[]{1000, 2000, 5000, 7000, 9000};
        double[] results = new double[trainSizes.length];
        double result;
        for (int i = 0; i < trainSizes.length; i++) {
            result = 0;
            for (int j = 0; j < iter; j++) {
                result += subMain(trainSizes[i]);
            }
            results[i] = result / iter;
        }

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
        System.out.println("Train set / Percent good");
        for (int i = 0; i < trainSizes.length; i++) {
            System.out.println("\t" + trainSizes[i] + "\t" + df.format(results[i] * 100));
        }
    }

    private static double subMain(int trainNumber) {
        SimpleNeuralNetwork net = new SimpleNeuralNetwork(9, 1);
        net.build();
        int testNumber = 1000;
        for (int i = 0; i < trainNumber; i++) {
            double[] data = new double[9];
            double target = generateData(data);
            net.train(data, target);
        }

        int wellClassified = 0;
        for (int i = 0; i < testNumber; i++) {
            double[] data = new double[9];
            double target = generateData(data);
            if (net.test(data) == target) {
                wellClassified++;
            }
        }

        //DecimalFormat df = new DecimalFormat("#");
        //df.setMaximumFractionDigits(3);
        System.out.println("Trained on " + trainNumber + ". Percent good: " + df.format(100 * (float) wellClassified / (float) testNumber) + "%");
        //print the network
        //net.printWeights();
        return (float) wellClassified / (float) testNumber;
    }

    private SimpleNeuralNetwork(int inputNb, int outputNb) {
        this.inputNb = inputNb;
        this.outputNb = outputNb;
        df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
    }

    private void build() {
        // Create the neurons
        inputLayer = new ArrayList<Neuron>();
        for (int i = 0; i < inputNb; i++) {
            inputLayer.add(new InputNeuron());
        }

        hiddenLayers = new ArrayList<List<Neuron>>();
        List<Neuron> hidden1 = new ArrayList<Neuron>();
        for (int i = 0; i < inputNb; i++) {
            hidden1.add(new Neuron());
        }
        hiddenLayers.add(hidden1);

        outputLayer = new ArrayList<Neuron>();
        for (int i = 0; i < outputNb; i++) {
            outputLayer.add(new OutputNeuron());
        }

        // Connect them
        for (Neuron neuron : inputLayer) {
            neuron.setConnections(null, hidden1);
        }
        for (Neuron neuron : hidden1) {
            neuron.setConnections(inputLayer, outputLayer);
        }
        for (Neuron neuron : outputLayer) {
            neuron.setConnections(hidden1, null);
        }
    }

    private void train(double[] inputData, double target) {
        test(inputData);

        // set output target
        for (Neuron neuron : outputLayer) {
            OutputNeuron outputNeuron = (OutputNeuron) neuron;
            outputNeuron.setTarget(target);
        }

        // back propagate
        for (Neuron neuron : outputLayer) {
            neuron.backPropagate();
        }
        for (List<Neuron> neurons : hiddenLayers) {
            for (Neuron neuron : neurons) {
                neuron.backPropagate();
            }
        }
        for (Neuron neuron : inputLayer) {
            neuron.backPropagate();
        }
    }

    private double test(double[] inputData) {
        double result = 0;
        for (int i = 0; i < inputNb; i++) {
            InputNeuron inputNeuron = (InputNeuron) inputLayer.get(i);
            inputNeuron.setInput(inputData[i]);
        }

        // propagate
        for (Neuron neuron : inputLayer) {
            neuron.propagate();
        }
        for (List<Neuron> neurons : hiddenLayers) {
            for (Neuron neuron : neurons) {
                neuron.propagate();
            }
        }
        for (Neuron neuron : outputLayer) {
            neuron.propagate();
            //System.out.println("output: " + df.format(neuron.getValue())
            //        + ", error: " + df.format(((OutputNeuron) neuron).getError()));
            result = neuron.getValue();
        }
        return result >= 0.5 ? 1 : 0;
    }

    private static double generateData(double[] data) {
        for (int i = 0; i < 9; i++) {
            data[i] = rand.nextInt(2);
        }

        boolean row1, row2, row3;
        if (data[0] == data[1] && data[1] == data[2]) {
            row1 = true;
        } else {
            row1 = false;
        }

        if (data[3] == data[4] && data[4] == data[5]) {
            row2 = true;
        } else {
            row2 = false;
        }

        if (data[6] == data[7] && data[7] == data[8]) {
            row3 = true;
        } else {
            row3 = false;
        }
        if (row1 || row2 || row3) {
            return 1;
        } else {
            return 0;
        }
    }

    private static String dataToString(double[] data) {
        String str = "";
        for (int i = 0; i < 9; i++) {
            str += data[i] + " ";
        }
        return str;
    }

    private void printWeights() {
        for (List<Neuron> neurons : hiddenLayers) {
            for (Neuron neuron : neurons) {
                neuron.printWeights();
            }
        }
        for (Neuron neuron : outputLayer) {
            neuron.printWeights();
        }
    }
    
    
    // THIS FUNCTION IS OUTDATED
    private void build2() {
        NetworkStructure structure = new NetworkStructure();
        structure.createInputLayer(9);
        structure.createOutputLayer(1);
        structure.createSubLayer("hidden1.1", 3);
        structure.createSubLayer("hidden1.2", 3);
        structure.createSubLayer("hidden1.3", 3);
        structure.connectLayers("input", "hidden1.1", NetworkStructure.ConnexionType.FULL);
        structure.connectLayers("input", "hidden1.2", NetworkStructure.ConnexionType.FULL);
        structure.connectLayers("input", "hidden1.3", NetworkStructure.ConnexionType.FULL);
        structure.attachSubLayerToLayer("hidden1.1", "hidden1");
        structure.attachSubLayerToLayer("hidden1.2", "hidden1");
        structure.attachSubLayerToLayer("hidden1.3", "hidden1");
        structure.connectLayers("hidden1", "output", NetworkStructure.ConnexionType.FULL);
        List<List<Neuron>> allLayers = structure.getLayers();
        inputLayer = allLayers.get(0);
        outputLayer = allLayers.get(allLayers.size() - 1);
        hiddenLayers = allLayers.subList(1, allLayers.size() - 1);
    }
}
