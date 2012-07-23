package de.idos.updates.demo;

import de.idos.updates.store.ProgressReport;

public class ConsoleReport implements ProgressReport {
    @Override
    public void expectedSize(long size) {
        System.out.println();
        System.out.println("Downloading " + size + " bytes");
    }

    @Override
    public void progress(long progress) {
        System.out.print(".");
    }

    @Override
    public void done() {
        System.out.println("Done.");
    }
}