package de.idos.updates.integrationtest;

import de.idos.updates.DefaultUpdateSystem;
import de.idos.updates.VersionStore;
import de.idos.updates.repository.Repository;
import de.idos.updates.store.NullReport;
import de.idos.updates.store.ProgressReport;

public class UpdateSystemBuilder {
    private VersionStore versionStore;
    private Repository repository;
    private ProgressReport report = new NullReport();

    public void useStore(VersionStore versionStore) {
        this.versionStore = versionStore;
    }

    public void useRepository(Repository repository) {
        this.repository = repository;
    }

    public DefaultUpdateSystem create() {
        DefaultUpdateSystem updateSystem = new DefaultUpdateSystem(versionStore, versionStore, repository, repository);
        updateSystem.reportAllProgressTo(report);
        return updateSystem;
    }

    public void addReporter(ProgressReport report) {
        this.report = report;
    }
}
