package realisticnn.environment;

import realisticnn.neuralnetwork.NeuralNetwork;

import java.util.*;

public class EnvironmentChat extends Environment {

    private volatile List<String> history;
    private volatile String message;
    private volatile BitSet bits;
    private volatile int index;
    private static final int INPUT_NEURONS = 2049;

    public EnvironmentChat() {
        super();
        history = new ArrayList<>();
        message = "";
        bits = new BitSet(8);
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
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (running) {
                    String next;
                    while ((next = scanner.nextLine()).isEmpty());
                    next = "User: " + next;
                    history.add(0, next);
                    System.out.println(next);
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
                    String next = "Bot: " + message;
                    history.add(0, next);
                    System.out.println(next);
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
        BitSet bs;
        if (index >= 0) {
            bs = BitSet.valueOf(history.get(index).getBytes());
        } else {
            bs = BitSet.valueOf(message.getBytes());
        }
        //System.out.println(bs.size());
        for (int i = 0; i < bs.size(); i++) {
            data[i] = bs.get(i) ? 1.0 : 0.0;
        }
        data[INPUT_NEURONS - 1] = 1.0;
        //System.out.println(Arrays.toString(data));
        return data;
    }

    public List<String> getMessageHistory() {
        return history;
    }

    public String getCurrentMessage() {
        return message;
    }

    public BitSet getCurrentBits() {
        return bits;
    }

//    public static void main(String[] args) {
//        Environment e = new EnvironmentChat();
//        e.interact(0);
//        e.interact(2);
//        e.interact(4);
//        e.interact(6);
//        e.interact(8);
//        e.interact(1);
//        e.interact(3);
//        e.interact(5);
//        e.interact(7);
//        e.interact(8);
//        e.interact(9);
//        e.interact(10);
//    }
}
