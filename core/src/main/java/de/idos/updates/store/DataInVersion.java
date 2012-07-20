package de.idos.updates.store;

import de.idos.updates.UpdateFailedException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataInVersion {

    private final InputStreamFactory factory;

    public DataInVersion(InputStreamFactory factory) {
        this.factory = factory;
    }

    public void storeIn(File versionFolder, String fileName) {
        try {
            InputStream source = factory.openStream();
            copyStreamToFileInVersion(versionFolder, fileName, source);
        } catch (IOException e) {
            throw new UpdateFailedException("Could not import into " + versionFolder.getName(), e);
        }
    }

    private void copyStreamToFileInVersion(File versionFolder, String fileName, InputStream source) throws IOException {
        File targetFile = new File(versionFolder, fileName);
        FileUtils.copyInputStreamToFile(source, targetFile);
    }
}