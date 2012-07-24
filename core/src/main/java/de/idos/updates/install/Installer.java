package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

import java.io.IOException;
import java.util.List;

public class Installer<T> {

    private InstallationStrategy<T> strategy;
    private ProgressReport report;

    public Installer(InstallationStrategy<T> strategy, ProgressReport report) {
        this.strategy = strategy;
        this.report = report;
    }

    public void install(Version version) {
        try {
            report.startingInstallationOf(version);
            report.assemblingFileList();
            List<T> elements = strategy.findAllElementsToInstall(version);
            report.foundElementsToInstall(elements.size());
            for (T element : elements) {
                strategy.installElement(element, version);
            }
            report.finishedInstallation();
        } catch (IOException e) {
            strategy.handleException(e, version);
        }
    }
}