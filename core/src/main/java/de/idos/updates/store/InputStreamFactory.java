package de.idos.updates.store;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamFactory {
    
    InputStream openStream() throws IOException;

    long getExpectedSize() throws IOException;
}