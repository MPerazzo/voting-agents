package ar.edu.itba.utils;

public class Random {

    final static java.util.Random r = new java.util.Random();

    public Random() {

    }

    public static double generateDouble(final double min, final double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    public static double generateDoubleSigned(final double min, final double max) {
        return (r.nextBoolean() ? 1 : -1) * generateDouble(min, max);
    }

    public static double generateDouble() {
        return r.nextDouble();
    }

    public static int generateInt(int min, int max) {

        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return r.nextInt((max - min) + 1) + min;
    }
}
