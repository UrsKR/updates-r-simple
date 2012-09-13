package de.idos.updates.store;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzipper {
    private Installation targetInstallation;

    public Unzipper(Installation targetInstallation) {
        this.targetInstallation = targetInstallation;
    }

    public void unzipAllArchivesInDirectory(File temporaryFolder, File stagingDirectory) throws IOException {
        for (File file : temporaryFolder.listFiles(new OnlyZips())) {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream inputStream = zipFile.getInputStream(entry);
                File contentFile = new File(stagingDirectory, entry.getName());
                FileOutputStream outputStream = new FileOutputStream(contentFile);
                IOUtils.copy(inputStream, outputStream);
                targetInstallation.addContent(new FileDataInVersion(contentFile));
                outputStream.close();
            }
            zipFile.close();
        }
    }

    private class OnlyZips implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".zip");
        }
    }
}