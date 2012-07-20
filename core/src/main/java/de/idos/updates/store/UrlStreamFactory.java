package de.idos.updates.store;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlStreamFactory implements InputStreamFactory{
    private URL url;

    public UrlStreamFactory(URL url) {
        this.url = url;
    }

    @Override
    public InputStream openStream() throws IOException {
        return url.openStream();
    }
}