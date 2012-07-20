package de.idos.updates.store;

import de.idos.updates.UpdateFailedException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

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
            String message = MessageFormat.format("Could not import {0} into {1}.", fileName, versionFolder.getName());
            throw new UpdateFailedException(message, e);
        }
    }

    private void copyStreamToFileInVersion(File versionFolder, String fileName, InputStream source) throws IOException {
        File targetFile = new File(versionFolder, fileName);
        FileUtils.copyInputStreamToFile(source, targetFile);
    }
}