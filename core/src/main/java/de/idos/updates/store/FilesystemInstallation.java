package de.idos.updates.store;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesystemInstallation implements Installation {
    public static Installation create(File file) {
        try {
            return new FilesystemInstallation(file);
        } catch (IOException e) {
            return new NullInstallation();
        }
    }

    private File versionFolder;

    public FilesystemInstallation(File versionFolder) throws IOException {
        this.versionFolder = versionFolder;
        versionFolder.mkdirs();
        new File(versionFolder, "installation.running").createNewFile();
    }

    @Override
    public void addContent(DataInVersion dataInVersion) {
        dataInVersion.storeIn(versionFolder);
    }

    @Override
    public void abort() {
        FileUtils.deleteQuietly(versionFolder);
    }

    @Override
    public void finish() {
        new File(versionFolder, "installation.running").delete();
    }
}