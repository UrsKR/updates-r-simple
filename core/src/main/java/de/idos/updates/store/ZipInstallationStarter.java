package de.idos.updates.store;

import java.io.File;

public class ZipInstallationStarter implements InstallationStarter {

    private InstallationStarter wrapped;

    public ZipInstallationStarter(InstallationStarter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Installation start(File targetFolder, ProgressReport report) {
        Installation start = wrapped.start(targetFolder, report);
        return new ZipInstallation(start, report);
    }
}
