package de.idos.updates;

public interface Version {
    boolean isGreaterThan(Version version);

    boolean isEqualTo(Version version);

    String asString();
}