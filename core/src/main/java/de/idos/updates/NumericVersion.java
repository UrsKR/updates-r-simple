package de.idos.updates;

public class NumericVersion implements Version, Comparable<NumericVersion> {
    private Integer major;
    private Integer minor;
    private Integer micro;

    public NumericVersion(int major, int minor, int micro) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
    }

    @Override
    public int compareTo(NumericVersion other) {
        int majorComparison = major.compareTo(other.major);
        if (majorComparison != 0) {
            return majorComparison;
        }
        int minorComparison = minor.compareTo(other.minor);
        if (minorComparison != 0) {
            return minorComparison;
        }
        return micro.compareTo(other.micro);
    }

    @Override
    public String toString() {
        return "Version " + asString();
    }

    @Override
    public boolean isGreaterThan(Version version) {
        if (version instanceof NullVersion) {
            return true;
        }
        return compareTo((NumericVersion) version) > 0;
    }

    @Override
    public boolean isEqualTo(Version version) {
        if (version instanceof NullVersion) {
            return false;
        }
        return compareTo((NumericVersion) version) == 0;
    }

    @Override
    public String asString() {
        return major + "." + minor + "." + micro;
    }
}