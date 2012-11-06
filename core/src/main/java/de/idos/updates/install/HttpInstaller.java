package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.DataImport;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import de.idos.updates.store.UrlDataInVersion;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HttpInstaller implements InstallationStrategy<String> {

    private ProgressReport report;
    private URL baseUrl;
    private Installation installation;

    public HttpInstaller(ProgressReport report, URL baseUrl, Installation installation) {
        this.report = report;
        this.baseUrl = baseUrl;
        this.installation = installation;
    }

    @Override
    public List<String> findAllElementsToInstall(Version version) throws IOException {
        URL contentList = new URL(baseUrl, version.asString() + "/content");
        InputStream input = contentList.openStream();
        return IOUtils.readLines(input);
    }

    @Override
    public void installElement(String file, Version version) throws IOException {
        report.installingFile(file);
        URL fileUrl = new URL(baseUrl, version.asString() + "/" + file);
        DataImport dataImport = new DataImport().reportProgressTo(report);
        UrlDataInVersion dataInVersion = new UrlDataInVersion(fileUrl, file, dataImport);
        installation.addContent(dataInVersion);
    }

    @Override
    public void handleException() {
        installation.abort();
    }

    @Override
    public void finalizeInstallation() {
        installation.finish();
    }
}