package de.idos.updates;

import java.io.File;

public class RootFolderSelector {

    public File getRootFolder() {
        File file = new File(".", "core");
        if (file.exists()) {
            return file;
        } else {
            return new File(".");
        }
    }
}
