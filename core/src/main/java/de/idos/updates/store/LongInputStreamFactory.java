package de.idos.updates.store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LongInputStreamFactory implements InputStreamFactory {
    private int length;

    public LongInputStreamFactory(int length) {
        this.length = length;
    }

    @Override
    public InputStream openStream() throws IOException {
        return new ByteArrayInputStream(new byte[length]);
    }

    @Override
    public long getExpectedSize() {
        return length;
    }
}
