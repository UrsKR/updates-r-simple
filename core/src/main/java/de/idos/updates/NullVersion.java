package de.idos.updates;

public class NullVersion implements Version{
    @Override
    public boolean isGreaterThan(Version version) {
        return false;
    }

    @Override
    public boolean isEqualTo(Version version) {
        return version instanceof NullVersion;
    }

    @Override
    public String asString() {
        return "No version or unknown version";
    }
}