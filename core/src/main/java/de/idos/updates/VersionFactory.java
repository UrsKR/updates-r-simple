package de.idos.updates;

import java.util.ArrayList;
import java.util.List;

public class VersionFactory {

    public List<Version> createVersionsFromStrings(List<String> versionsAsStrings) {
        List<Version> versions = new ArrayList<Version>();
        for (String string : versionsAsStrings) {
            versions.add(createVersionFromString(string));
        }
        return versions;
    }

    public Version createVersionFromString(String versionAsString) {
        String[] versionParts = versionAsString.split("\\.");
        return new NumericVersion(Integer.valueOf(versionParts[0]), Integer.valueOf(versionParts[1]), Integer.valueOf(versionParts[2]));
    }
}
