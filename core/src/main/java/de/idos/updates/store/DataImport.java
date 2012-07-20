package de.idos.updates.store;

import java.io.File;

public class DataImport {

    private InputStreamFactory factory;

    public static DataImport takeDataFromFactory(InputStreamFactory factory) {
        DataImport dataImport = new DataImport();
        dataImport.factory = factory;
        return dataImport;
    }

    public DataImport reportProgressTo(ProgressReport report) {
        this.factory = new ReportingFactory(factory, report);
        return this;
    }

    public void andStoreThemIn(File versionFolder, String fileName) {
        DataImporter dataImporter = new DataImporter(factory);
        dataImporter.importTo(versionFolder, fileName);
    }
}