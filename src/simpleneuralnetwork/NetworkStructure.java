/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleneuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author philippe
 */
public class NetworkStructure {
    // THIS CLASS IS NOT USED.

    private Map<String, Layer> layers;
    private Map<String, Layer> subLayers;
    private List<String> layerOrder;

    public NetworkStructure() {
        layers = new HashMap<String, Layer>();
        subLayers = new HashMap<String, Layer>();
        layerOrder = new ArrayList<String>();
    }

    public void createInputLayer(int size) {
        Layer inputLayer = new Layer();
        for (int i = 0; i < size; i++) {
            inputLayer.addNeuron(new InputNeuron());
        }
        layers.put("input", inputLayer);
    }

    public void createOutputLayer(int size) {
        Layer outputLayer = new Layer();
        for (int i = 0; i < size; i++) {
            outputLayer.addNeuron(new OutputNeuron());
        }
        layers.put("output", outputLayer);
    }

    public void createSubLayer(String name, int size) {
        if (subLayers.containsKey(name)) {
            System.err.println("A sublayer with this name already exists");
            return;
        }
        if (layers.containsKey(name)) {
            System.err.println("A layer with this name already exists");
            return;
        }
        subLayers.put(name, new Layer(size));
    }

    public void createLayer(String name, int size) {
        if (subLayers.containsKey(name)) {
            System.err.println("A sublayer with this name already exists");
            return;
        }
        if (layers.containsKey(name)) {
            System.err.println("A layer with this name already exists");
            return;
        }
        layers.put(name, new Layer(size));
        layerOrder.add(name);
    }

    public void attachSubLayerToLayer(String subLayer, String layer) {
        if (!subLayers.containsKey(subLayer)) {
            System.err.println("A sublayer with the name " + subLayer + " doesn't exists");
            return;
        }
        Layer sub = subLayers.get(subLayer);
        if (!layers.containsKey(layer)) {
            layers.put(layer, sub);
            layerOrder.add(layer);
        } else {
            layers.get(layer).append(sub);
        }
        subLayers.remove(subLayer);
    }

    public void connectLayers(String srcName, String dstName, ConnexionType connexionType) {
        Layer src = null;
        if (subLayers.containsKey(srcName)) {
            src = layers.get(srcName);
        } else if (layers.containsKey(srcName)) {
            src = layers.get(srcName);
        } else {
            System.err.println("A sublayer/layer with the name " + srcName + " doesn't exists");
            return;
        }

        Layer dst = null;
        if (subLayers.containsKey(dstName)) {
            dst = layers.get(dstName);
        } else if (layers.containsKey(dstName)) {
            dst = layers.get(dstName);
        } else {
            System.err.println("A sublayer/layer with the name " + dstName + " doesn't exists");
            return;
        }
        src.connectTo(dst, connexionType);
    }

    public List<List<Neuron>> getLayers() {
        List<List<Neuron>> allLayers = new ArrayList<List<Neuron>>();
        allLayers.add(layers.get("input").getNeurons());
        for (String layerName : layerOrder) {
            allLayers.add(layers.get(layerName).getNeurons());
        }
        allLayers.add(layers.get("output").getNeurons());
        return allLayers;
    }

    private class Layer {

        private List<Neuron> neurons;

        public Layer() {
            neurons = new ArrayList<Neuron>();
        }

        public Layer(int size) {
            neurons = new ArrayList<Neuron>();
            for (int i = 0; i < size; i++) {
                neurons.add(new Neuron());
            }
        }

        private void addNeuron(Neuron neuron) {
            neurons.add(neuron);
        }

        private void connectTo(Layer dst, ConnexionType connexionType) {
            List<Neuron> dstNeurons = dst.getNeurons();
            for (Neuron neuron : neurons) {
                neuron.setOutgoingConnexions(dstNeurons);
            }
            for (Neuron neuron : dstNeurons) {
                neuron.setIncommingConnexions(neurons);
            }
        }

        private List<Neuron> getNeurons() {
            return neurons;
        }

        private void append(Layer layer) {
            neurons.addAll(layer.getNeurons());
        }
    }

    public enum ConnexionType {
        FULL, HALF
    };
}
