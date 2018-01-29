package realisticnn.neuralnetwork;

import realisticnn.environment.Environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class NeuralNetwork implements Serializable {

    private List<Neuron> input;
    private List<Neuron> hidden;
    private List<Neuron> output;
    private Environment environment;
    //private volatile double reward;
    private volatile boolean running;
    private static final double POTENTIAL_THRESHOLD = 1.0;
    private static final double DEFAULT_LR = 0.01;

    public NeuralNetwork() {
        input = new ArrayList<>();
        hidden = new ArrayList<>();
        output = new ArrayList<>();
        environment = null;
        //reward = 0;
        running = false;
    }

    public NeuralNetwork(int inputs, int hiddens, int outputs) {
        input = new ArrayList<>();
        hidden = new ArrayList<>();
        output = new ArrayList<>();
        for (int i = 0; i < inputs; i++) {
            input.add(new Neuron(this));
        }
        for (int i = 0; i < hiddens; i++) {
            hidden.add(new Neuron(this));
        }
        for (int i = 0; i < outputs; i++) {
            output.add(new Neuron(this));
        }
        //reward = 0;
        running = false;
    }

    public void start(boolean train) {
        start(train, DEFAULT_LR);
    }

    public void start(boolean train, double lr) {
        running = true;
        new Thread(() -> {
            while (running) {
                for (Neuron neuron : input) {
                    if (neuron.isReadyToFire()) {
                        neuron.fire(train, lr);
                        //System.out.println("Input");
                    }
                }
                for (Neuron neuron : hidden) {
                    if (neuron.isReadyToFire()) {
                        neuron.fire(train, lr);
                        //System.out.println("Hidden");
                    }
                }
                for (Neuron neuron : output) {
                    if (neuron.isReadyToFire()) {
                        neuron.fire(train, lr);
                        if (environment != null) {
                            environment.interact(output.indexOf(neuron));
                        }
                        System.out.println(output.indexOf(neuron));
                    }
                }
                for (Neuron neuron : input) {
                    if (neuron.potentialExceedsThreshold()) {
                        neuron.prepareToFire();
                    }
                }
                for (Neuron neuron : hidden) {
                    if (neuron.potentialExceedsThreshold()) {
                        neuron.prepareToFire();
                    }
                }
                for (Neuron neuron : output) {
                    if (neuron.potentialExceedsThreshold()) {
                        neuron.prepareToFire();
                    }
                }
//                for (Neuron hiddenNeuron : hidden) {
//                    if (hiddenNeuron.isReadyToFire()) {
//                        //for (Neuron neuron : presynaptic) {
//                            //System.out.println(hiddenNeuron.getWeight(neuron));
//                        //}
//                        //if (train) {
//                        //    for (Neuron neuron : hiddenNeuron.getPresynapticNeurons()) {
//                        //        hiddenNeuron.changeWeight(neuron, -lr);
//                        //    }
//                        //}
//                        //hiddenNeuron.clearPresynapticNeurons();
//                        hiddenNeuron.fire();
//                    }
//                }
//                for (Neuron hiddenNeuron : hidden) {
//                    if (hiddenNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
//                        hiddenNeuron.prepareToFire();
////                        ArrayList<Neuron> presynaptic = new ArrayList<>(hiddenNeuron.getPresynapticNeurons());
////                        hiddenNeuron.fire();
////                        hiddenNeuron.clearPresynapticNeurons();
////                        if (train) {
////                            if (reward != 0) {
////                                for (Neuron sender : presynaptic) {
////
////                                }
////                                reward = 0;
////                            }
////                            //hiddenNeuron.changeWeight(presynaptic.get(0), 0.01);
////                            //System.out.println(hiddenNeuron.getWeight(presynaptic.get(0)));
////                        }
//                    }
//                }
//                for (Neuron outputNeuron : output) {
//                    if (outputNeuron.getStoredPotential() >= POTENTIAL_THRESHOLD) {
//                        System.out.println(output.indexOf(outputNeuron) + ":" + outputNeuron.output());
//                    }
//                }
            }
        }).start();
//        if (environment != null) {
//            new Thread(() -> {
//                while (running) {
//                    input(environment.getStimuli());
//                }
//            }).start();
//        }
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

    public void input(double[] inputVector) {
        new Thread(() -> {
            for (Neuron inputNeuron : input) {
                inputNeuron.addPotential(inputVector[input.indexOf(inputNeuron)]);
            }
        }).start();
    }

//    public void reward(double reward) {
//        this.reward += reward;
//    }

//    public void randomizeConnections(int extraConnections) {
//        for (Neuron inputNeuron : input) {
//            Neuron.connect(inputNeuron, hidden.get(randInt(0, hidden.size())));
//        }
//        for (int i = 1; i < hidden.size(); i++) {
//            Neuron.connect(hidden.get(i - 1), hidden.get(i));
//        }
//        for (Neuron outputNeuron : output) {
//            Neuron.connect(hidden.get(randInt(0, hidden.size())), outputNeuron);
//        }
//        int i = 0;
//        while (i < extraConnections) {
//            Neuron sender = hidden.get(randInt(0, hidden.size()));
//            Neuron receiver = hidden.get(randInt(0, hidden.size()));
//            if (sender != receiver && !sender.getOutputs().contains(receiver) && !receiver.getOutputs().contains(sender)) {
//                Neuron.connect(sender, receiver);
//                i++;
//            }
//        }
//    }

    public void randomlyConnect(int connectionsPerNeuron) {
        //double i = 0;
        for (Neuron n1 : input) {
            //System.out.println(i / input.size());
            //i++;
            List<Neuron> sample = new ArrayList<>(hidden);
            Collections.shuffle(sample);
            sample = sample.subList(0, connectionsPerNeuron);
            for (Neuron n2 : sample) {
                Neuron.connect(n1, n2);
            }
        }
        //i = 0;
        for (Neuron n1 : hidden) {
            //System.out.println(i / hidden.size());
            //i++;
            List<Neuron> sample = new ArrayList<>(hidden).stream()
                    .filter(n -> n != n1 && !Neuron.connected(n, n1))
                    .collect(Collectors.toList());
            Collections.shuffle(sample);
            sample = sample.subList(0, connectionsPerNeuron);
            for (Neuron n2 : sample) {
                if (randInt(0, 2) == 0) {
                    Neuron.connect(n1, n2);
                } else {
                    Neuron.connect(n2, n1);
                }
            }
        }
        //i = 0;
        for (Neuron n1 : output) {
            //System.out.println(i / output.size());
            //i++;
            List<Neuron> sample = new ArrayList<>(hidden);
            Collections.shuffle(sample);
            sample = sample.subList(0, connectionsPerNeuron);
            for (Neuron n2 : sample) {
                Neuron.connect(n2, n1);
            }
        }
    }

    public void fullyConnect() {
        //double i = 0;
        //double s1 = input.size() * hidden.size();
        for (Neuron neuron1 : input) {
            //System.out.println(i / s1);
            for (Neuron neuron2 : hidden) {
                //i++;
                Neuron.connect(neuron1, neuron2);
            }
        }
        //i = 0;        //double s2 = hidden.size() * hidden.size();
        for (Neuron neuron1 : hidden) {
            //System.out.println(i / s2);
            for (Neuron neuron2 : hidden) {
                //i++;
                if (neuron1 != neuron2 && !Neuron.connected(neuron1, neuron2)) {
                    if (randInt(0, 2) == 0) {
                        Neuron.connect(neuron1, neuron2);
                    } else {
                        Neuron.connect(neuron2, neuron1);
                    }

                }
            }
        }
        //i = 0;
        //double s3 = hidden.size() * output.size();
        for (Neuron neuron1 : hidden) {
            //System.out.println(i / s3);
            for (Neuron neuron2  : output) {
                //i++;
                Neuron.connect(neuron1, neuron2);
            }
        }
    }

    public void randomizeWeights() {
        //int i = 0;
        for (Neuron hiddenNeuron : hidden) {
            //System.out.println(++i);
            //double range = Util.logit(0.5 * POTENTIAL_THRESHOLD / Math.sqrt(hiddenNeuron.getInputs().size()) + 0.5);
            double range = POTENTIAL_THRESHOLD / Math.sqrt(hiddenNeuron.getInputs().size());
            //System.out.println(range);
            for (Neuron sender : hiddenNeuron.getInputs()) {
                hiddenNeuron.setWeight(sender, randDouble(-range, range));
            }
        }
        for (Neuron outputNeuron : output) {
            //double range = POTENTIAL_THRESHOLD / Math.sqrt(outputNeuron.getInputs().size());
            double range = POTENTIAL_THRESHOLD / Math.sqrt(outputNeuron.getInputs().size());
            //System.out.println(range);
            for (Neuron sender : outputNeuron.getInputs()) {
                outputNeuron.setWeight(sender, randDouble(-range, range));
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

    public void setEnvironment(Environment environment) {
        if (running) {
            // TODO raise exception
        } else {
            this.environment = environment;
        }
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

    public Environment getEnvironment() {
        return environment;
    }

    //    public double getCurrentReward() {
//        return reward;
//    }

    public boolean isRunning() {
        return running;
    }

    private static int randInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    private static double randDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }
}
