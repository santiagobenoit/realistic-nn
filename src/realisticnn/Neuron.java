package realisticnn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Neuron {

    private volatile List<Neuron> inputs;
    private volatile List<Neuron> outputs;
    private volatile List<Double> weights;
    private volatile double potential;
    private volatile Neuron presynaptic;
    private final ReentrantLock lock;
    private static final double DEFAULT_WEIGHT = 0.0;
    private static final double POTENTIAL_THRESHOLD = 1.0;


    public Neuron() {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        weights = new ArrayList<>();
        potential = 0;
        presynaptic = null;
        lock = new ReentrantLock();
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

    public void fire() {
        lock.lock();
        try {
            for (Neuron output : outputs) {
                output.activate(this, potential);
            }
            potential = 0;
        } finally {
            lock.unlock();
        }
    }

    public void activate(Neuron sender, double signal) {
        lock.lock();
        try {
            presynaptic = sender;
            double delta = signal * (2 * POTENTIAL_THRESHOLD * Util.sigmoid(weights.get(inputs.indexOf(sender))) - POTENTIAL_THRESHOLD);
            addPotential(delta);
            if (potential >= POTENTIAL_THRESHOLD) {
                System.out.println(weights.get(inputs.indexOf(sender)));
            }
        } finally {
            lock.unlock();
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

    public void addPotential(double delta) {
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

    public double output() {
        double output = potential;
        potential = 0;
        return output;
    }

    public Neuron getPresynapticNeuron() {
        return presynaptic;
    }
}
