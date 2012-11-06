package de.idos.updates.store;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlStreamFactory implements InputStreamFactory {
    private URL url;

    public UrlStreamFactory(URL url) {
        this.url = url;
    }

    @Override
    public InputStream openStream() throws IOException {
        return url.openStream();
    }

    @Override
    public long getExpectedSize() throws IOException {
        if (!url.getProtocol().equals("http")){
            return url.openConnection().getContentLength();
        }
        return getSizeOfHttpResource();
    }

    private long getSizeOfHttpResource() throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            return connection.getContentLength();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}