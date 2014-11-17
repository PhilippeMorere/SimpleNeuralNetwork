/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleneuralnetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author philippe
 */
public class Neuron {

    public double learningRate = 2;
    public static double minLearningRate = 0.001, maxLearningRate = 10, learningRateDecFactor = 1.3, learningRateInc = 0.3;
    public static Neuron biaisNeuron = new BiaisNeuron();
    protected List<Neuron> out;
    protected Map<Neuron, Double> in;
    protected double value;
    protected double error;

    protected Neuron() {
        error = 0;
    }

    public void setConnections(List<Neuron> in, List<Neuron> out) {
        setOutgoingConnexions(out);
        setIncommingConnexions(in);
    }

    public void setOutgoingConnexions(List<Neuron> out) {
        this.out = out;
    }

    public void setIncommingConnexions(List<Neuron> in) {
        this.in = new HashMap<Neuron, Double>();
        if (in != null) {
            for (Neuron neuron : in) {
                this.in.put(neuron, new Random().nextDouble() * 4 - 2);
            }
            this.in.put(biaisNeuron, new Random().nextDouble() * 4 - 2);
        }
    }

    public void propagate() {
        double tempValue = 0;
        for (Map.Entry<Neuron, Double> entry : in.entrySet()) {
            tempValue += entry.getValue() * entry.getKey().getValue();
        }
        value = sigmoidFunction(tempValue);
    }

    public void backPropagate() {
        double newError = calculateError();
        //changeLearningRate(newError);
        error = newError;
        for (Map.Entry<Neuron, Double> entry : in.entrySet()) {
            Neuron inNeuron = entry.getKey();
            in.put(inNeuron, entry.getValue() + learningRate * error * inNeuron.getValue());
        }
    }

    private void changeLearningRate(double newError) {
        if (Math.signum(error) == Math.signum(newError)) {
            learningRate += learningRateInc;
        } else {
            learningRate /= learningRateDecFactor;
        }
        if (learningRate >= maxLearningRate) {
            learningRate = maxLearningRate;
        } else if (learningRate < minLearningRate) {
            learningRate = minLearningRate;
        }
    }

    protected double getValue() {
        return value;
    }

    private double sigmoidFunction(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    protected double calculateError() {
        double _error = 0;
        for (Neuron neuron : out) {
            _error += neuron.getBackPropagatedError(this);
        }
        return _error * value * (1 - value);
    }

    protected double getBackPropagatedError(Neuron neuron) {
        if (in.containsKey(neuron)) {
            return in.get(neuron) * error;
        } else {
            return 0;
        }
    }

    void printWeights() {
        String weights = "";
        for (Map.Entry<Neuron, Double> entry : in.entrySet()) {
            weights += SimpleNeuralNetwork.df.format(entry.getValue()) + " ";
        }
        System.out.println(weights);
    }
}
