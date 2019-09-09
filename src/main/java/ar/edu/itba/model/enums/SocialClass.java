package ar.edu.itba.model.enums;

public enum SocialClass {
    LOW(0, 1),
    MID(1, 2),
    HIGH(2, 3);

    private final double start;
    private final double end;

    SocialClass(final double start, final double end) {
        this.start = start;
        this.end = end;
    }

    public static SocialClass getSocialClass(final double wellness) throws Exception {
        for (final SocialClass s : SocialClass.values()) {
            if (wellness >= s.start && wellness < s.end)
                return s;
        }
        throw new Exception("Wellness is out of bound [" + LOW.start + "-" + HIGH.end + ")");
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }
}
