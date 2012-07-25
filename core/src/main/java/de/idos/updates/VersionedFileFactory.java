package de.idos.updates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static de.idos.updates.store.InstallationUtil.hasMarkerFile;

public class VersionedFileFactory {
    public List<VersionedFile> createVersionedFilesFrom(File parent) {
        List<VersionedFile> versionedFiles = new ArrayList<VersionedFile>();
        for (File file : parent.listFiles()) {
            String versionAsString = file.getName();
            if (!hasMarkerFile(file)) {
                Version version = new VersionFactory().createVersionFromString(versionAsString);
                versionedFiles.add(new VersionedFile(version, file));
            }
        }
        return versionedFiles;
    }
}