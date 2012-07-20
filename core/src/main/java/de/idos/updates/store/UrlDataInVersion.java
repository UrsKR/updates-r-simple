package de.idos.updates.store;

import java.io.File;
import java.net.URL;

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
        DataImport.takeDataFromFactory(factory).andStoreThemIn(versionFolder, filename);
    }
}