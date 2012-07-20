package de.idos.updates.store;

import java.io.File;
import java.net.URL;

import static de.idos.updates.store.GenericDataInVersion.storeDataFromFactoryIn;

public class UrlDataInVersion implements DataInVersion{

    private URL url;
    private String filename;

    public UrlDataInVersion(URL url, String filename) {
        this.url = url;
        this.filename = filename;
    }

    @Override
    public void storeIn(File versionFolder) {
        InputStreamFactory factory = new UrlStreamFactory(url);
        storeDataFromFactoryIn(factory, versionFolder, filename);
    }
}