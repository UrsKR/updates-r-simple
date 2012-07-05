package de.idos.updates;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesystemVersionStore implements VersionStore {
    private File folder;

    public FilesystemVersionStore(File folder) {
        this.folder = folder;
    }

    @Override
    public void addVersion(Version version) {
        File file = getVersionFolder(version);
        file.mkdir();
    }

    @Override
    public void addContent(Version version, File file) {
        try {
            File versionFolder = getVersionFolder(version);
            FileUtils.copyFileToDirectory(file, versionFolder);
        } catch (IOException e) {
            throw new UpdateFailedException("Could not import version "+ version.asString(), e);
        }
    }

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }
}
