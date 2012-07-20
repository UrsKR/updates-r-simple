package de.idos.updates.store;

import de.idos.updates.UpdateFailedException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

public class DataImporter {

    private final InputStreamFactory factory;

    public DataImporter(InputStreamFactory factory) {
        this.factory = factory;
    }

    public void importTo(File folder, String fileName) {
        try {
            InputStream source = factory.openStream();
            copyStreamToFileInFolder(folder, fileName, source);
        } catch (IOException e) {
            String message = MessageFormat.format("Could not import {0} into {1}.", fileName, folder.getName());
            throw new UpdateFailedException(message, e);
        }
    }

    private void copyStreamToFileInFolder(File folder, String fileName, InputStream source) throws IOException {
        File targetFile = new File(folder, fileName);
        FileUtils.copyInputStreamToFile(source, targetFile);
    }
}