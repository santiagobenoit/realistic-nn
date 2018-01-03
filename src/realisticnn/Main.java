package realisticnn;

import java.util.Arrays;

public class Main {

    public static final String VERSION = "0.0.1";

    public static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().maxMemory() / 1048576d);
        System.out.println("Creating neurons...");
        NeuralNetwork network = new NeuralNetwork(2048, 10000, 4);
        System.out.println("Connecting neurons...");
        //network.randomizeConnections(1000);
        network.fullyConnect();
        System.out.println("Randomizing weights...");
        network.randomizeWeights();
        System.out.println("Done. Starting network...");
        network.start(true);
        new Thread(() -> {
            while (network.isRunning()) {
                System.out.println("Done");
                //network.input(Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d));

//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }
}
