package de.idos.updates.configuration;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataInVersion;

import java.io.File;

public class FixedVersionStore implements VersionStore
{
    private File rootFolder;

    public FixedVersionStore(File rootFolder) {
        this.rootFolder = rootFolder;
    }

    @Override
    public void addVersion(Version version) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addContent(Version version, DataInVersion dataInVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeOldVersions() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeVersion(Version version) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Version getLatestVersion() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getFolderForLatestVersion() {
        return rootFolder;
    }
}
