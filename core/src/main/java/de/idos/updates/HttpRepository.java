package de.idos.updates;

import de.idos.updates.install.HttpInstaller;
import de.idos.updates.install.Installer;
import de.idos.updates.lookup.HttpLookup;
import de.idos.updates.lookup.VersionLookup;
import de.idos.updates.store.NullReport;
import de.idos.updates.store.ProgressReport;

import java.io.IOException;
import java.net.URL;

public class HttpRepository implements Repository {
    private final URL baseUrl;
    private ProgressReport report = new NullReport();

    public HttpRepository(String url) {
        try {
            this.baseUrl = new URL(url);
        } catch (IOException e) {
            throw new IllegalArgumentException("Please specify a valid URL as your repository base.", e);
        }
    }

    @Override
    public Version getLatestVersion() {
        return new VersionLookup(new HttpLookup(baseUrl), report).lookUpLatestVersion();
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        store.addVersion(version);
        HttpInstaller httpInstaller = new HttpInstaller(store, report, baseUrl);
        new Installer<String>(httpInstaller, report).install(version);
    }

    @Override
    public void reportAllProgressTo(ProgressReport report) {
        this.report = report;
    }
}