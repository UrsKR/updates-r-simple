package de.idos.updates.store;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileMother {
    public static File createContentFileForZip(File targetFolder) throws IOException {
        File file = File.createTempFile("content", "test", targetFolder);
        file.deleteOnExit();
        FileUtils.writeStringToFile(file, "FILECONTENT");
        return file;
    }

    public static File createContentFileForZip(File staging, String name) throws IOException {
        File file = new File(staging, name);
        file.deleteOnExit();
        FileUtils.writeStringToFile(file, "FILECONTENT");
        return file;
    }

    public static File createZipFileInTemporaryFolder(File targetFolder, String name, File content) throws IOException {
        File file = new File(targetFolder, name);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
        zipOutputStream.putNextEntry(new ZipEntry(content.getName()));
        zipOutputStream.write(FileUtils.readFileToByteArray(content));
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        file.deleteOnExit();
        return file;
    }
}