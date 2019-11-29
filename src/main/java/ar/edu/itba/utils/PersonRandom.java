package ar.edu.itba.utils;

public class PersonRandom extends Random {

    final private static PersonRandom instance = new PersonRandom();

    final private java.util.Random r = new java.util.Random();

    private PersonRandom() {

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

    public static PersonRandom getInstance() {
        return instance;
    }
}

