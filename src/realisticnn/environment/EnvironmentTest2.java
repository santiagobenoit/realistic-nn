package realisticnn.environment;

import realisticnn.neuralnetwork.NeuralNetwork;

import java.util.Arrays;

public class EnvironmentTest2 extends Environment {

    private volatile double[][] map;
    private volatile int[] position;

    public EnvironmentTest2() {
        super();
        map = new double[][]{{1, 0.8, 0.6}, {0.8, 0.6, 0.4}, {0.6, 0.4, 0.2}};
        position = new int[]{0, 0};
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
            case 0: // +x
                if (position[0] < map[0].length - 1) {
                    position[0]++;
                    System.out.println("+x " + Arrays.toString(getStimuli()));
                }
                break;
            case 1: // -x
                if (position[0] > 0) {
                    position[0]--;
                    System.out.println("-x" + Arrays.toString(getStimuli()));
                }
                break;
            case 2: // +y
                if (position[1] < map.length - 1) {
                    position[1]++;
                    System.out.println("+y" + Arrays.toString(getStimuli()));
                }
                break;
            case 3: // -y
                if (position[1] > 0) {
                    position[1]--;
                    System.out.println("-y" + Arrays.toString(getStimuli()));
                }
                break;
//            case 4: // +value
//                map[position[1]][position[0]] += 0.01;
//                break;
//            case 5: // -value
//                map[position[1]][position[0]] -= 0.01;
//                break;
            default: // do nothing
                break;
        }
    }

    @Override
    public synchronized double[] getStimuli() {
        return new double[]{map[position[1]][position[0]], 0};
    }
}
