package realisticnn;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Arrays;
import java.util.concurrent.FutureTask;

public class Main {

    public static final String VERSION = "0.0.1";
    public static NeuralNetwork network;
    private static volatile int i;

    public static void main(String[] args) {
        network = new NeuralNetwork(10, 100, 10);
        network.randomizeConnections(1000);
        network.randomizeWeights();
        System.out.println("Initialized");
        network.start(true);
        new Thread(() -> {
            while (true) {
                i++;
                network.input(Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d));
                FutureTask<Void> task = new FutureTask(() -> {
                    Circle c = new Circle();
                    c.setRadius(10);
                    //c.setCenterX(i*10);
                    //c.setCenterY(i*10);
                    c.setFill(new Color(Util.randDouble(0, 1), Util.randDouble(0, 1), Util.randDouble(0, 1), 1));
                    //System.out.println(c.getCenterX());
                    Neuron n = new Neuron();
                    n.setStoredPotential(Util.randDouble(0, 1));
                    GUI.circles.put(n, c);
                    //System.out.println(GUI.circles.size());
                }, null);
                Platform.runLater(task);
                try {
                    task.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
