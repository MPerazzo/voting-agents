package ar.edu.itba.utils;

public abstract class Random {

    protected Random() {

    }

    protected double generateDouble(final double min, final double max, final java.util.Random r) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    protected double generateDoubleSigned(final double min, final double max, final java.util.Random r) {
        return (r.nextBoolean() ? 1 : -1) * generateDouble(min, max, r);
    }

    protected double generateDouble(final java.util.Random r) {
        return r.nextDouble();
    }

    protected int generateInt(int min, int max, final java.util.Random r) {

        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return r.nextInt((max - min) + 1) + min;
    }

    public abstract double generateDouble(final double min, final double max);

    public abstract double generateDoubleSigned(final double min, final double max);

    public abstract double generateDouble();

    public abstract int generateInt(int min, int max);
}
