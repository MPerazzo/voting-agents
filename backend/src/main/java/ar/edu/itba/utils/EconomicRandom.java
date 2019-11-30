package ar.edu.itba.utils;

public class EconomicRandom extends Random {

    final private static EconomicRandom instance = new EconomicRandom();

    final private java.util.Random r = new java.util.Random();

    private EconomicRandom() {

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

    public static EconomicRandom getInstance() {
        return instance;
    }
}
