package de.idos.updates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VersionedFileFactory {
    public List<VersionedFile> createVersionedFilesFrom(File parent) {
        List<VersionedFile> versionedFiles = new ArrayList<VersionedFile>();
        for (File file : parent.listFiles()) {
            String versionAsString = file.getName();
            String[] versionParts = versionAsString.split("\\.");
            NumericVersion version = new NumericVersion(Integer.valueOf(versionParts[0]), Integer.valueOf(versionParts[1]), Integer.valueOf(versionParts[2]));
            versionedFiles.add(new VersionedFile(version, file));
        }
        return versionedFiles;
    }
}