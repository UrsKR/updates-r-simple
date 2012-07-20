package de.idos.updates.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileStreamFactory implements InputStreamFactory {
    private File file;

    public FileStreamFactory(File file) {
        this.file = file;
    }

    @Override
    public InputStream openStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public long getExpectedSize() {
        return file.length();
    }
}