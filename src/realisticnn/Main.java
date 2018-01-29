package realisticnn;

import realisticnn.environment.Environment;
import realisticnn.environment.EnvironmentChat;
import realisticnn.environment.EnvironmentTest;
import realisticnn.environment.EnvironmentTest2;
import realisticnn.neuralnetwork.NeuralNetwork;

public class Main { // temporary test class

    public static final String VERSION = "0.0.1";

    public static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().maxMemory() / 1048576d);
        System.out.println("Creating neurons...");
        //NeuralNetwork network = new NeuralNetwork(2049, 10000, 13);
        NeuralNetwork network = new NeuralNetwork(2, 300, 4);
        System.out.println("Connecting neurons...");
        network.randomlyConnect(25);
        //network.fullyConnect();
        System.out.println("Initializing weights...");
        network.randomizeWeights();
        System.out.println("Initializing environment...");
        Environment environment = new EnvironmentTest2();
        environment.addNeuralNetwork(network);
        System.out.println("Done. Starting simulation...");
        environment.start(100);
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
