package ar.edu.itba.model.enums;

import ar.edu.itba.model.utils.Random;

public enum Subject {
    POLITICS,
    ECONOMICS,
    CULTURAL,
    SPORTS;

    public static Subject getRandomSubject() {
        return values()[Random.generateInt(0, values().length - 1)];
    }
}
