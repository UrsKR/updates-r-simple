package de.idos.updates.install;

import de.idos.updates.Version;

import java.io.IOException;
import java.util.List;

public interface InstallationStrategy<T> {
    List<T> findAllElementsToInstall(Version version) throws IOException;

    void installElement(T element, Version version) throws IOException;

    void handleException(Exception e, Version version);
}