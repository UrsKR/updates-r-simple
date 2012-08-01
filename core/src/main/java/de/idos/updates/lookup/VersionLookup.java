package de.idos.updates.lookup;

import de.idos.updates.Version;
import de.idos.updates.VersionFinder;
import de.idos.updates.store.ProgressReport;

public class VersionLookup {
    private LookupStrategy strategy;
    private ProgressReport report;

    public VersionLookup(LookupStrategy strategy, ProgressReport report) {
        this.strategy = strategy;
        this.report = report;
    }

    public Version lookUpLatestVersion() {
        report.lookingUpLatestAvailableVersion();
        try {
            Version latestVersion = strategy.findLatestUpdate().getVersion();
            report.latestAvailableVersionIs(latestVersion);
            return latestVersion;
        } catch (Exception e) {
            report.versionLookupFailed(e);
            return VersionFinder.BASE_VERSION;
        }
    }
}
