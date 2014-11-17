/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleneuralnetwork;

/**
 *
 * @author philippe
 */
public class OutputNeuron extends Neuron {

    private double target;

    public void setTarget(double target) {
        this.target = target;
    }

    @Override
    protected double calculateError() {
        double _error = target - value;
        //System.out.println("error: " + _error);
        return _error * value * (1 - value);
    }

    public double getError() {
        return calculateError();
    }
}
