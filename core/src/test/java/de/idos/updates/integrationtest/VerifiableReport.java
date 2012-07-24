package de.idos.updates.integrationtest;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VerifiableReport implements ProgressReport {

    private boolean somethingWasReported = false;

    @Override
    public void startingInstallationOf(Version version) {
        this.somethingWasReported = true;
    }

    @Override
    public void assemblingFileList() {
        this.somethingWasReported = true;
    }

    @Override
    public void foundElementsToInstall(int numberOfElements) {
        this.somethingWasReported = true;
    }

    @Override
    public void expectedSize(long size) {
        this.somethingWasReported = true;
    }

    @Override
    public void progress(long progress) {
        this.somethingWasReported = true;
    }

    @Override
    public void finishedFile() {
        this.somethingWasReported = true;
    }

    @Override
    public void finishedInstallation() {
        this.somethingWasReported = true;
    }

    @Override
    public void installingFile(String name) {
        this.somethingWasReported = true;
    }

    public void assertThatSomethingWasReported() {
        assertThat(somethingWasReported, is(true));
    }
}
