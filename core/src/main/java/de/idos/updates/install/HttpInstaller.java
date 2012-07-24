package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataImport;
import de.idos.updates.store.ProgressReport;
import de.idos.updates.store.UrlDataInVersion;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HttpInstaller implements InstallationStrategy<String> {

    private VersionStore store;
    private ProgressReport report;
    private URL baseUrl;

    public HttpInstaller(VersionStore store, ProgressReport report, URL baseUrl) {
        this.store = store;
        this.report = report;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<String> findAllElementsToInstall(Version version) throws IOException {
        URL contentList = new URL(baseUrl, "updates/" + version.asString() + "/content");
        InputStream input = contentList.openStream();
        return IOUtils.readLines(input);
    }

    @Override
    public void installElement(String file, Version version) throws IOException {
        report.installingFile(file);
        URL fileUrl = new URL(baseUrl, "updates/" + version.asString() + "/" + file);
        DataImport dataImport = new DataImport();
        UrlDataInVersion dataInVersion = new UrlDataInVersion(fileUrl, file, dataImport.reportProgressTo(report));
        store.addContent(version, dataInVersion);
    }

    @Override
    public void handleException(IOException e, Version version) {
        store.removeVersion(version);
    }
}
