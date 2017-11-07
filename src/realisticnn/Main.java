package realisticnn;

import java.util.Arrays;

public class Main {

    public static final String VERSION = "0.0.1";

    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork(10, 100, 10);
        network.randomizeConnections(1000);
        network.randomizeWeights();
        System.out.println("Initialized");
        network.start(true);
        new Thread(() -> {
            while (true) {
                network.input(Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
