package ar.edu.itba.utils;

public class ConfigRandom extends Random {

    final private static ConfigRandom instance = new ConfigRandom();

    final private java.util.Random r = new java.util.Random();

    private ConfigRandom() {

    }

    @Override
    public double generateDouble(double min, double max) {
        return super.generateDouble(min, max, r);
    }

    @Override
    public double generateDoubleSigned(double min, double max) {
        return super.generateDoubleSigned(min, max, r);
    }

    @Override
    public double generateDouble() {
        return super.generateDouble(r);
    }

    @Override
    public int generateInt(int min, int max) {
        return super.generateInt(min, max, r);
    }

    public static ConfigRandom getInstance() {
        return instance;
    }
}

