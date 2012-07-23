package de.idos.updates.store;

import java.io.File;
import java.net.URL;

public class UrlDataInVersion implements DataInVersion{

    private URL url;
    private String filename;
    private DataImport dataImport;

    public UrlDataInVersion(URL url, String filename) {
        this(url, filename, new DataImport());
    }

    public UrlDataInVersion(URL url, String filename, DataImport dataImport) {
        this.url = url;
        this.filename = filename;
        this.dataImport = dataImport;
    }

    @Override
    public void storeIn(File versionFolder) {
        InputStreamFactory factory = new UrlStreamFactory(url);
        dataImport.takeDataFromFactory(factory).andStoreThemIn(versionFolder, filename);
    }
}