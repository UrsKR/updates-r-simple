package de.idos.updates.store;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ZipInstallation implements Installation {

    private Installation wrapped;
    private ProgressReport report;

    public ZipInstallation(Installation wrapped, ProgressReport report) {
        this.wrapped = wrapped;
        this.report = report;
    }

    @Override
    public void addContent(DataInVersion dataInVersion) {
        File downloadDirectory = null;
        try {
            downloadDirectory = createTemporaryFolder("download");
            dataInVersion.storeIn(downloadDirectory);
            installNonArchivesFrom(downloadDirectory);
            installArchiveContentsFrom(downloadDirectory);
            FileUtils.deleteDirectory(downloadDirectory);
        } catch (Exception e) {
            report.installationFailed(e);
            FileUtils.deleteQuietly(downloadDirectory);
        }
    }

    @Override
    public void finish() {
        wrapped.finish();
    }

    @Override
    public void abort() {
        wrapped.abort();
    }

    @Override
    public boolean isRunning() {
        return wrapped.isRunning();
    }

    private void installNonArchivesFrom(File temporaryFolder) {
        for (File file : temporaryFolder.listFiles(new NoZips())) {
            wrapped.addContent(new FileDataInVersion(file));
        }
    }

    private void installArchiveContentsFrom(File sourceDirectory) throws IOException {
        File stagingDirectory = createTemporaryFolder("unpack");
        new Unzipper(wrapped).unzipAllArchivesInDirectory(sourceDirectory, stagingDirectory);
        FileUtils.deleteDirectory(stagingDirectory);
    }

    private File createTemporaryFolder(String remark) throws IOException {
        File temporaryFolder = File.createTempFile("updates-r-us", remark);
        temporaryFolder.delete();
        temporaryFolder.mkdir();
        return temporaryFolder;
    }

    private static class NoZips implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return !name.endsWith(".zip");
        }
    }
}