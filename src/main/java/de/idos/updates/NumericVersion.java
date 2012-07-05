package de.idos.updates;

public class NumericVersion implements Version {
    private int major;
    private int minor;
    private int micro;

    public NumericVersion(int major, int minor, int micro) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
    }
}
