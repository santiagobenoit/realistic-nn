package realisticnn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Neuron implements Serializable {

    private volatile List<Neuron> inputs;
    private volatile List<Neuron> outputs;
    private volatile List<Double> weights;
    //private volatile List<Neuron> presynaptic;
    private volatile double potential;
    //private volatile double firePotential;
    private volatile boolean readyToFire;
    private final NeuralNetwork neuralNetwork;
    private static final double DEFAULT_WEIGHT = 0.0;
    private static final double POTENTIAL_THRESHOLD = 1.0;
    private static final double MIN_THRESHOLD = 0.1;
    private static final double WEIGHT_LIMIT = 1.0;


    public Neuron(NeuralNetwork parent) {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        weights = new ArrayList<>();
        //presynaptic = new ArrayList<>();
        potential = 0;
        //firePotential = 0;
        readyToFire = false;
        neuralNetwork = parent;
    }

    public static void connect(Neuron sender, Neuron receiver) {
        sender.addOutput(receiver);
        receiver.addInput(sender);
    }

    public static void connect(Neuron sender, Neuron receiver, double weight) {
        sender.addOutput(receiver);
        receiver.addInput(sender, weight);
    }

    public static void disconnect(Neuron sender, Neuron receiver) {
        sender.removeOutput(receiver);
        receiver.removeInput(sender);
    }

    public void prepareToFire() {
        //firePotential = potential;
        potential = 0;
        readyToFire = true;
    }

    public void fire(boolean train, double lr) {
        for (Neuron output : outputs) {
            output.activate(this, POTENTIAL_THRESHOLD, train, lr);
        }
        //presynaptic.clear();
        readyToFire = false;
    }

    private synchronized void activate(Neuron sender, double signal, boolean train, double lr) {

        //double delta = signal * (2 * POTENTIAL_THRESHOLD * Util.sigmoid(weights.get(inputs.indexOf(sender))) - POTENTIAL_THRESHOLD);
        double delta = signal * weights.get(inputs.indexOf(sender));
        potential += delta;
        if (train) {
            if (potential >= POTENTIAL_THRESHOLD) {
                //System.out.println("+");
                //presynaptic.add(sender);
                changeWeight(sender, lr);
            } else if (readyToFire) {
                //System.out.println("-");
                changeWeight(sender, -lr);
            }
        }
    }

    public void setInputs(ArrayList<Neuron> newInputs) {
        inputs = newInputs;
    }

    public void setOutputs(ArrayList<Neuron> newOutputs) {
        outputs = newOutputs;
    }

    public void setWeights(ArrayList<Double> newWeights) {
        weights = newWeights;
    }

    public void setWeight(Neuron input, double weight) {
        weights.set(inputs.indexOf(input), weight);
    }

    public void setWeight(int index, double weight) {
        weights.set(index, weight);
    }

    public void changeWeight(Neuron input, double delta) {
        double newWeight = weights.get(inputs.indexOf(input)) + delta;
        if (newWeight > WEIGHT_LIMIT) {
            newWeight = WEIGHT_LIMIT;
        } else if (newWeight < -WEIGHT_LIMIT) {
            newWeight = -WEIGHT_LIMIT;
        }
        setWeight(input, newWeight);
    }

    public void changeWeight(int index, double delta) {
        double newWeight = weights.get(index) + delta;
        setWeight(index, newWeight);
    }

    public void setStoredPotential(double newPotential) {
        potential = newPotential;
    }

    public void addInput(Neuron input) {
        inputs.add(input);
        weights.add(DEFAULT_WEIGHT);
    }

    public void addInput(Neuron input, double weight) {
        inputs.add(input);
        weights.add(weight);
    }

    public void addOutput(Neuron output) {
        outputs.add(output);
    }

    public synchronized void addPotential(double delta) {
        potential += delta;
    }

    public void removeInput(Neuron input) {
        weights.remove(inputs.indexOf(input));
        inputs.remove(input);
    }

    public void removeInput(int index) {
        inputs.remove(index);
        weights.remove(index);
    }

    public void removeOutput(Neuron output) {
        outputs.remove(output);
    }

    public void removeOutput(int index) {
        outputs.remove(index);
    }

//    public void clearPresynapticNeurons() {
//        presynaptic.clear();
//    }

    public List<Neuron> getInputs() {
        return inputs;
    }

    public List<Neuron> getOutputs() {
        return outputs;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getWeight(Neuron input) {
        return weights.get(inputs.indexOf(input));
    }

    public double getWeight(int index) {
        return weights.get(index);
    }

    public double getStoredPotential() {
        return potential;
    }

//    public double getLastFiredPotential() {
//        return firePotential;
//    }

    public boolean isReadyToFire() {
        return readyToFire;
    }

    public boolean potentialExceedsThreshold() {
        return potential >= POTENTIAL_THRESHOLD;
    }

//    public double output() {
//        potential = 0;
//        return POTENTIAL_THRESHOLD;
//    }

//    public List<Neuron> getPresynapticNeurons() {
//        return presynaptic;
//    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }
}
