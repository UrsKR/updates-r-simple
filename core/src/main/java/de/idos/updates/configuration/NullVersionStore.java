package de.idos.updates.configuration;

import de.idos.updates.Version;
import de.idos.updates.VersionFinder;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataInVersion;

import java.io.File;

public class NullVersionStore implements VersionStore {
    @Override
    public void addVersion(Version version) {
        //nothing to do
    }

    @Override
    public void addContent(Version version, DataInVersion dataInVersion) {
        //nothing to do
    }

    @Override
    public void removeOldVersions() {
        //nothing to do
    }

    @Override
    public void removeVersion(Version version) {
        //nothing to do
    }

    @Override
    public Version getLatestVersion() {
        return VersionFinder.BASE_VERSION;
    }

    @Override
    public File getFolderForVersionToRun() {
        throw new UnsupportedOperationException("Null version store can't be run");
    }
}
