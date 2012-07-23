package de.idos.updates.integrationtest;

import de.idos.updates.Repository;
import de.idos.updates.UpdateSystem;
import de.idos.updates.VersionStore;
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

    public UpdateSystem create() {
        UpdateSystem updateSystem = new UpdateSystem(versionStore, repository);
        updateSystem.reportAllProgressTo(report);
        return updateSystem;
    }

    public void addReporter(VerifiableReport report) {
        this.report = report;
    }
}
