package de.idos.updates.lookup;

import de.idos.updates.Update;

import java.io.IOException;

public interface LookupStrategy {

  Update findLatestUpdate() throws IOException;
}