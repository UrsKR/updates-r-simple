package de.idos.updates.store;

import de.idos.updates.Version;

public class ProgressReportAdapter implements ProgressReport{
    @Override
    public void lookingUpLatestAvailableVersion() {
        //nothing to do
    }

    @Override
    public void latestAvailableVersionIs(Version value) {
        //nothing to do
    }

    @Override
    public void versionLookupFailed() {
        //nothing to do
    }

    @Override
    public void versionLookupFailed(Exception e) {
        //nothing to do
    }

    @Override
    public void startingInstallationOf(Version version) {
        //nothing to do
    }

    @Override
    public void assemblingFileList() {
        //nothing to do
    }

    @Override
    public void foundElementsToInstall(int numberOfElements) {
        //nothing to do
    }

    @Override
    public void installingFile(String name) {
        //nothing to do
    }

    @Override
    public void expectedSize(long size) {
        //nothing to do
    }

    @Override
    public void progress(long progress) {
        //nothing to do
    }

    @Override
    public void finishedFile() {
        //nothing to do
    }

    @Override
    public void finishedInstallation() {
        //nothing to do
    }

    @Override
    public void installationFailed(Exception e) {
        //nothing to do
    }

    @Override
    public void updateAlreadyInProgress() {
        //nothing to do
    }
}