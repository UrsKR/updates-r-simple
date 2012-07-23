package de.idos.updates.store;

import java.io.File;

public class DataImport {

    private InputStreamFactory factory;
    private ProgressReport report;

    public DataImport takeDataFromFactory(InputStreamFactory factory) {
        this.factory = factory;
        return this;
    }

    public DataImport reportProgressTo(ProgressReport report) {
        this.report = report;
        return this;
    }

    public void andStoreThemIn(File versionFolder, String fileName) {
        if (report != null) {
            this.factory = new ReportingFactory(factory, report);
        }
        DataImporter dataImporter = new DataImporter(factory);
        dataImporter.importTo(versionFolder, fileName);
    }
}