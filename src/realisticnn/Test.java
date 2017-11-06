package realisticnn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    class Node {
        List<Node> inputs = new ArrayList<>();
        List<Node> outputs = new ArrayList<>();
        volatile double potential = 0;
        private final ReentrantLock lock = new ReentrantLock();

        // methods for adding input/output nodes...

        void send() {
            lock.lock();
            try {
                for (Node node : outputs) node.receive(potential);
                potential = 0;
            } finally {
                lock.unlock();
            }
        }

        void receive(double signal) {
            lock.lock();
            try {
                potential += signal;
                if (potential >= 1) System.out.println(signal);
                // this value will sometimes be negative, which would not be possible if my code was thread-safe.
            } finally {
                lock.unlock();
            }
        }
    }

    class Graph {
        List<Node> nodes = new ArrayList<>();

        // build the graph...

        void run() {
            for (Node node : nodes) {
                new Thread(() -> {
                    while (true) {
                        if (node.potential >= 1) node.send();
                    }
                }).start();
            }
        }

        void feedInput(List<Double> inputVector) {
            // ...
        }
    }
}
