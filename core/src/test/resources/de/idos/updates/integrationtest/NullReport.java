package de.idos.updates.integrationtest;

import de.idos.updates.store.ProgressReport;

public class NullReport implements ProgressReport {
    @Override
    public void expectedSize(long size) {
        //nothing to do
    }

    @Override
    public void progress(long progress) {
        //nothing to do
    }

    @Override
    public void done() {
        //nothing to do
    }
}
