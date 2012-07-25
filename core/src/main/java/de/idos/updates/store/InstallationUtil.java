package de.idos.updates.store;

import java.io.File;
import java.io.IOException;

public class InstallationUtil {

    public static boolean createMarkerFile(File parent) throws IOException {
        return getMarkerFile(parent).createNewFile();
    }

    public static boolean hasMarkerFile(File parent) {
        return getMarkerFile(parent).exists();
    }

    public static void deleteMarkerFile(File parent) {
        getMarkerFile(parent).delete();
    }

    private static File getMarkerFile(File parent) {
        return new File(parent, "installation.running");
    }
}