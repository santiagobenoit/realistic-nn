package realisticnn.environment;

import realisticnn.neuralnetwork.NeuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Environment implements Serializable {

    protected List<NeuralNetwork> neuralNetworks;
    protected volatile boolean running;

    public Environment() {
        neuralNetworks = new ArrayList<>();
        running = false;
    }

    public void start(long delay) {
        if (running) {
            //TODO
        } else {
            running = true;
            for (NeuralNetwork network : neuralNetworks) {
                network.start(true);
                new Thread(() -> {
                    while (running) {
                        if (delay > 0) {
                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        network.input(getStimuli());
                    }
                }).start();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public final void addNeuralNetwork(NeuralNetwork network) {
        neuralNetworks.add(network);
        network.setEnvironment(this);
    }

    public final void removeNeuralNetwork(NeuralNetwork network) {
        neuralNetworks.remove(network);
        network.setEnvironment(null);
    }

    public final List<NeuralNetwork> getNeuralNetworks() {
        return neuralNetworks;
    }

    public final boolean isRunning() {
        return running;
    }

    public abstract void interact(int action);

    public abstract double[] getStimuli();
}
