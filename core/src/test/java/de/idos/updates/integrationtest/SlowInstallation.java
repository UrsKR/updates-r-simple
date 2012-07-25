package de.idos.updates.integrationtest;

import de.idos.updates.store.DataInVersion;
import de.idos.updates.store.Installation;

import java.util.concurrent.CountDownLatch;

public class SlowInstallation implements Installation {
    private Installation installation;
    private CountDownLatch slowThreadStartSignal;

    public SlowInstallation(Installation installation, CountDownLatch slowThreadStartSignal) {
        this.installation = installation;
        this.slowThreadStartSignal = slowThreadStartSignal;
    }

    @Override
    public void addContent(DataInVersion dataInVersion) {
        try {
            slowThreadStartSignal.await();
            installation.addContent(dataInVersion);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void abort() {
        installation.abort();
    }

    @Override
    public void finish() {
        installation.finish();
    }
}