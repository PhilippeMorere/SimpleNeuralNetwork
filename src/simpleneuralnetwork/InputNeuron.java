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
public class InputNeuron extends Neuron {

    private double input;

    public void setInput(double input) {
        this.input = input;
    }

    @Override
    public void propagate() {
        value = input;
    }

    @Override
    public void backPropagate() {
    }
}
