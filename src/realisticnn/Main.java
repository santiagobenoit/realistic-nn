package realisticnn;

import realisticnn.environment.Environment;
import realisticnn.environment.EnvironmentChat;
import realisticnn.neuralnetwork.NeuralNetwork;

public class Main { // temporary test class

    public static final String VERSION = "0.0.1";

    public static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().maxMemory() / 1048576d);
        System.out.println("Creating neurons...");
        NeuralNetwork network = new NeuralNetwork(2049, 10000, 11);
        System.out.println("Connecting neurons...");
        //network.randomizeConnections(1000);
        network.fullyConnect();
        System.out.println("Initializing weights...");
        network.randomizeWeights();
        System.out.println("Initializing environment...");
        Environment environment = new EnvironmentChat();
        environment.addNeuralNetwork(network);
        System.out.println("Done. Starting simulation...");
        environment.start();
        //new Thread(() -> {
            //while (network.isRunning()) {
                //network.input(Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d));

//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            //}
        //}).start();
    }
}
