package de.idos.updates.store;

public interface ProgressReport {
    void expectedSize(long size);

    void progress(long progress);
}
