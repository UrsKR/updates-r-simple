package de.idos.updates.store;

import java.io.File;

public class FileDataInVersion implements DataInVersion {
    private File file;

    public FileDataInVersion(File file) {
        this.file = file;
    }

    @Override
    public void storeIn(File versionFolder) {
        InputStreamFactory factory = new FileStreamFactory(file);
        String fileName = file.getName();
        GenericDataInVersion dataInVersion = new GenericDataInVersion(factory);
        dataInVersion.storeIn(versionFolder, fileName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileDataInVersion that = (FileDataInVersion) o;
        if (file != null) {
            if (!file.equals(that.file)) {
                return false;
            }
        } else if (that.file != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }
}