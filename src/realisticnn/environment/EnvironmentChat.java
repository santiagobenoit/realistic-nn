package realisticnn.environment;

import realisticnn.neuralnetwork.NeuralNetwork;

import java.util.*;

public class EnvironmentChat extends Environment {

    private volatile String view;
    private volatile String message;
    private volatile BitSet bits;
    private static final int INPUT_NEURONS = 2049;

    public EnvironmentChat() {
        super();
        view = "";
        message = "";
        bits = new BitSet(8);
    }

    @Override
    public void start() {
        if (running) {
            //TODO
        } else {
            running = true;
            for (NeuralNetwork network : neuralNetworks) {
                network.start(true);
                new Thread(() -> {
                    while (running) {
                        network.input(getStimuli());
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (running) {
                    String next;
                    while ((next = scanner.nextLine()).isEmpty());
                    view = "User: " + next;
                    System.out.println(view);
                }
            }).start();
        }
    }

    @Override
    public synchronized void interact(int action) {
        switch (action) {
            case 0: // cases 0-7: flip a bit
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                bits.flip(action);
                break;
            case 8: // next byte
                message += new String(bits.toByteArray());
                bits.clear();
                break;
            case 9: // submit message
                if (!message.isEmpty()) {
                    view = "Bot: " + message;
                    System.out.println(view);
                    message = "";
                }
                break;
            default: // do nothing
                break;
        }
    }

    @Override
    public synchronized double[] getStimuli() {
        double[] data = new double[INPUT_NEURONS];
        //System.out.println(Arrays.toString(view.getBytes()));
        //System.out.println(view);
        BitSet bs = BitSet.valueOf(view.getBytes());
        //System.out.println(bs.size());
        for (int i = 0; i < bs.size(); i++) {
            data[i] = bs.get(i) ? 1.0 : 0.0;
        }
        data[INPUT_NEURONS - 1] = 1.0;
        //System.out.println(Arrays.toString(data));
        return data;
    }

    public String getCurrentView() {
        return view;
    }

    public String getCurrentMessage() {
        return message;
    }

    public static void main(String[] args) {
        Environment e = new EnvironmentChat();
        e.interact(0);
        e.interact(2);
        e.interact(4);
        e.interact(6);
        e.interact(8);
        e.interact(1);
        e.interact(3);
        e.interact(5);
        e.interact(7);
        e.interact(8);
        e.interact(9);
        e.interact(10);
    }
}
