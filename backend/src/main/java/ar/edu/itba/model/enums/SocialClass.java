package ar.edu.itba.model.enums;

public enum SocialClass {
    LOW("low", 0, 1),
    MID("mid", 1, 2),
    HIGH("high",2, 3);

    private final String name;
    private final double start;
    private final double end;

    SocialClass(final String name, final double start, final double end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public static SocialClass getSocialClass(final double wellness) throws Exception {
        for (final SocialClass s : SocialClass.values()) {
            if (wellness >= s.start && wellness <= s.end)
                return s;
        }
        throw new Exception("Wellness is out of bound [" + LOW.start + "-" + HIGH.end + ")");
    }

    public static double economicThreshold(double economicWellness, double impact) {
        if (economicWellness + impact < LOW.start)
            return -economicWellness;
        else if (economicWellness + impact > HIGH.end)
            return HIGH.end - economicWellness;
        else
            return impact;
    }

    public String getName() {
        return name;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }
}
