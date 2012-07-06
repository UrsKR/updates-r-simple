package de.idos.updates.integrationtest;

import de.idos.updates.Repository;
import de.idos.updates.UpdateSystem;
import de.idos.updates.VersionStore;

public class UpdateSystemBuilder {
    private VersionStore versionStore;
    private Repository repository;

    public void useStore(VersionStore versionStore) {
        this.versionStore = versionStore;
    }

    public void useRepository(Repository repository) {
        this.repository = repository;
    }

    public UpdateSystem create() {
        return new UpdateSystem(versionStore, repository);
    }
}
