package de.idos.updates.store;

public class NullInstallation implements Installation {
    @Override
    public void addContent(DataInVersion dataInVersion) {
        //nothing to do
    }

    @Override
    public void abort() {
        //nothing to do
    }

    @Override
    public void finish() {
        //nothing to do
    }
}
