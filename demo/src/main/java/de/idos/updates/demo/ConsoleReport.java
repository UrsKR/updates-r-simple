package de.idos.updates.demo;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

public class ConsoleReport implements ProgressReport {
    @Override
    public void lookingUpLatestAvailableVersion() {
        System.out.println("Looking for latest available version");
    }

    @Override
    public void latestAvailableVersionIs(Version value) {
        System.out.println("Found version: " + value);
    }

    @Override
    public void versionLookupFailed(Exception e) {
        System.out.println("Could not determine latest version: " + e.getMessage());
    }

    @Override
    public void startingInstallationOf(Version version) {
        System.out.println();
        System.out.println("Starting installation of " + version.asString());
    }

    @Override
    public void assemblingFileList() {
        System.out.println("Assembling list of files to install.");
    }

    @Override
    public void foundElementsToInstall(int numberOfElements) {
        System.out.println("Found " + numberOfElements + " files to install.");
    }

    @Override
    public void installingFile(String name) {
        System.out.println("Installing " + name);
    }

    @Override
    public void expectedSize(long size) {
        System.out.println("Downloading " + size + " bytes");
    }

    @Override
    public void progress(long progress) {
        System.out.print(".");
    }

    @Override
    public void finishedFile() {
        System.out.println("Done.");
    }

    @Override
    public void finishedInstallation() {
        System.out.println("Finished installation.");
    }

    @Override
    public void installationFailed(Exception e) {
        System.out.println("Installation failed: " + e.getMessage());
    }

    @Override
    public void updateAlreadyInProgress() {
        System.out.println("Installation failed: An installation is already in progress.");
    }
}