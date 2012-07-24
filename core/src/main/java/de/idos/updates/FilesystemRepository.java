package de.idos.updates;

import de.idos.updates.install.FileInstaller;
import de.idos.updates.install.Installer;
import de.idos.updates.lookup.FileLookup;
import de.idos.updates.lookup.VersionLookup;
import de.idos.updates.store.NullReport;
import de.idos.updates.store.ProgressReport;

import java.io.File;

public class FilesystemRepository implements Repository {
    public static final String AVAILABLE_VERSIONS = "available_versions";
    private final File availableVersions;
    private ProgressReport report = new NullReport();

    public FilesystemRepository(File root) {
        this.availableVersions = new File(root, AVAILABLE_VERSIONS);
        if (!availableVersions.exists()) {
            availableVersions.mkdirs();
        }
    }

    @Override
    public Version getLatestVersion() {
        return new VersionLookup(new FileLookup(availableVersions), report).lookUpLatestVersion();
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        store.addVersion(version);
        FileInstaller fileInstaller = new FileInstaller(store, report, availableVersions);
        new Installer<File>(fileInstaller, report).install(version);
    }

    @Override
    public void reportAllProgressTo(ProgressReport report) {
        this.report = report;
    }
}