package realisticnn;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static int randInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static double randDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static double sigmoid(double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
