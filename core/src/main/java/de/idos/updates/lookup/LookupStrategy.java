package de.idos.updates.lookup;

import de.idos.updates.Version;

import java.io.IOException;

public interface LookupStrategy {
    Version findLatestVersion() throws IOException;
}
