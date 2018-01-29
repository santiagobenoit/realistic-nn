package realisticnn.environment;

import realisticnn.neuralnetwork.NeuralNetwork;

public class EnvironmentTest extends Environment {

    private volatile double value;

    public EnvironmentTest() {
        super();
        value = 0.0;
    }

    @Override
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

    @Override
    public synchronized void interact(int action) {
        switch (action) {
            case 0:
                if (value > 0) {
                    value -= 0.01;
                }
                break;
            case 1:
                if (value < 1) {
                    value += 0.01;
                }
            default: // do nothing
                break;
        }
    }

    @Override
    public synchronized double[] getStimuli() {
        return new double[]{value, 1};
    }

    public double getValue() {
        return value;
    }
}
