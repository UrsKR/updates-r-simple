package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataImport;
import de.idos.updates.store.FileDataInVersion;
import de.idos.updates.store.ProgressReport;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileInstaller implements InstallationStrategy<File> {

    private VersionStore store;
    private ProgressReport report;
    private File availableVersions;

    public FileInstaller(VersionStore store, ProgressReport report, File availableVersions) {
        this.store = store;
        this.report = report;
        this.availableVersions = availableVersions;
    }

    @Override
    public List<File> findAllElementsToInstall(Version version) throws IOException {
        File versionFolder = new File(availableVersions, version.asString());
        return Arrays.asList(versionFolder.listFiles());
    }

    @Override
    public void installElement(File file, Version version) throws IOException {
        report.installingFile(file.getName());
        DataImport dataImport = new DataImport().reportProgressTo(report);
        FileDataInVersion dataInVersion = new FileDataInVersion(file, dataImport);
        store.addContent(version, dataInVersion);
    }

    @Override
    public void handleException(IOException e, Version version) {
        //nothing to do
    }
}
