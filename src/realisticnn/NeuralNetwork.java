package realisticnn;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private volatile List<Neuron> input;
    private volatile List<Neuron> hidden;
    private volatile List<Neuron> output;
    private volatile double reward;
    private volatile boolean running;
    private static final double POTENTIAL_THRESHOLD = 1.0;

    public NeuralNetwork() {
        input = new ArrayList<>();
        hidden = new ArrayList<>();
        output = new ArrayList<>();
        reward = 0;
        running = false;
    }

    public NeuralNetwork(int inputs, int hiddens, int outputs) {
        input = new ArrayList<>();
        hidden = new ArrayList<>();
        output = new ArrayList<>();
        for (int i = 0; i < inputs; i++) {
            input.add(new Neuron());
        }
        for (int i = 0; i < hiddens; i++) {
            hidden.add(new Neuron());
        }
        for (int i = 0; i < outputs; i++) {
            output.add(new Neuron());
        }
    }

    public void start(boolean train) {
        running = true;
        new Thread(() -> {
            while (running) {
                for (Neuron hiddenNeuron : hidden) {
                    if (hiddenNeuron.isReadyToFire()) {
                        List<Neuron> presynaptic = hiddenNeuron.getPresynapticNeurons();
                        //for (Neuron neuron : presynaptic) {
                            //System.out.println(hiddenNeuron.getWeight(neuron));
                        //}
                        hiddenNeuron.fire();
                    }
                }
                for (Neuron hiddenNeuron : hidden) {
                    if (hiddenNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
                        hiddenNeuron.prepareToFire();
//                        ArrayList<Neuron> presynaptic = new ArrayList<>(hiddenNeuron.getPresynapticNeurons());
//                        hiddenNeuron.fire();
//                        hiddenNeuron.clearPresynapticNeurons();
//                        if (train) {
//                            if (reward != 0) {
//                                for (Neuron sender : presynaptic) {
//
//                                }
//                                reward = 0;
//                            }
//                            //hiddenNeuron.changeWeight(presynaptic.get(0), 0.01);
//                            //System.out.println(hiddenNeuron.getWeight(presynaptic.get(0)));
//                        }
                    }
                }
                for (Neuron outputNeuron : output) {
                    if (outputNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
                        System.out.println(output.indexOf(outputNeuron) + ":" + outputNeuron.output());
                    }
                }
            }
        }).start();
    }

//    public void start(boolean train) {
//        running = true;
//        for (Neuron hiddenNeuron : hidden) {
//            new Thread(() -> {
//                while (running) {
//                    if (hiddenNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
//                        Neuron presynaptic = hiddenNeuron.getPresynapticNeuron();
//                        hiddenNeuron.fire();
//                        if (train) {
//                            //hiddenNeuron.changeWeight(presynaptic, 0.01);
//                            //System.out.println(hiddenNeuron.getWeight(presynaptic));
//                        }
//                    }
//                }
//            }).start();
//        }
//        for (Neuron outputNeuron : output) {
//            new Thread(() -> {
//                while (running) {
//                    if (outputNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
//                        System.out.println(output.indexOf(outputNeuron) + ":" + outputNeuron.output());
//                    }
//                }
//            }).start();
//        }
//    }

    public void stop() {
        running = false;
    }

    public void input(List<Double> inputVector) {
        new Thread(() -> {
            for (Neuron inputNeuron : input) {
                inputNeuron.setStoredPotential(inputVector.get(input.indexOf(inputNeuron)));
                inputNeuron.prepareToFire();
                inputNeuron.fire();
            }
        }).start();
    }

    public void reward(double reward) {
        reward += reward;
    }

    public void randomizeConnections(int extraConnections) {
        for (Neuron inputNeuron : input) {
            Neuron.connect(inputNeuron, hidden.get(Util.randInt(0, hidden.size())));
        }
        for (int i = 1; i < hidden.size(); i++) {
            Neuron.connect(hidden.get(i - 1), hidden.get(i));
        }
        for (Neuron outputNeuron : output) {
            Neuron.connect(hidden.get(Util.randInt(0, hidden.size())), outputNeuron);
        }
        int i = 0;
        while (i < extraConnections) {
            Neuron sender = hidden.get(Util.randInt(0, hidden.size()));
            Neuron receiver = hidden.get(Util.randInt(0, hidden.size()));
            if (sender != receiver && !sender.getOutputs().contains(receiver) && !receiver.getOutputs().contains(sender)) {
                Neuron.connect(sender, receiver);
                i++;
            }
        }
    }

    public void randomizeWeights() {
        for (Neuron hiddenNeuron : hidden) {
            //double range = Util.logit(0.5 * POTENTIAL_THRESHOLD / Math.sqrt(hiddenNeuron.getInputs().size()) + 0.5);
            for (Neuron sender : hiddenNeuron.getInputs()) {
                hiddenNeuron.setWeight(sender, Util.randDouble(-1, 1));
            }
        }
        for (Neuron outputNeuron : output) {
            //double range = POTENTIAL_THRESHOLD / Math.sqrt(outputNeuron.getInputs().size());
            for (Neuron sender : outputNeuron.getInputs()) {
                outputNeuron.setWeight(sender, Util.randDouble(-1, 1));
            }
        }
    }

    public void setInputNeurons(ArrayList<Neuron> newInput) {
        input = newInput;
    }

    public void setHiddenNeurons(ArrayList<Neuron> newHidden) {
        hidden = newHidden;
    }

    public void setOutputNeurons(ArrayList<Neuron> newOutput) {
        output = newOutput;
    }

    public void addInputNeuron(Neuron neuron) {
        input.add(neuron);
    }

    public void addHiddenNeuron(Neuron neuron) {
        hidden.add(neuron);
    }

    public void addOutputNeuron(Neuron neuron) {
        output.add(neuron);
    }

    public void removeInputNeuron(Neuron neuron) {
        input.remove(neuron);
    }

    public void removeHiddenNeuron(Neuron neuron) {
        hidden.remove(neuron);
    }

    public void removeOutputNeuron(Neuron neuron) {
        output.remove(neuron);
    }

    public void removeInputNeuron(int index) {
        input.remove(index);
    }

    public void removeHiddenNeuron(int index) {
        hidden.remove(index);
    }

    public void removeOutputNeuron(int index) {
        output.remove(index);
    }

    public List<Neuron> getInputNeurons() {
        return input;
    }

    public List<Neuron> getHiddenNeurons() {
        return hidden;
    }

    public List<Neuron> getOutputNeurons() {
        return output;
    }

    public double getCurrentReward() {
        return reward;
    }

    public boolean isRunning() {
        return running;
    }
}
