package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

import java.util.List;

public class DefaultInstaller<T> implements Installer {

    private InstallationStrategy<T> strategy;
    private ProgressReport report;

    public DefaultInstaller(InstallationStrategy<T> strategy, ProgressReport report) {
        this.strategy = strategy;
        this.report = report;
    }

    @Override
    public void install(Version version) {
        try {
            report.startingInstallationOf(version);
            report.assemblingFileList();
            List<T> elements = strategy.findAllElementsToInstall(version);
            report.foundElementsToInstall(elements.size());
            for (T element : elements) {
                strategy.installElement(element, version);
            }
            strategy.finalizeInstallation();
            report.finishedInstallation();
        } catch (Exception e) {
            strategy.handleException();
            report.installationFailed(e);
        }
    }
}