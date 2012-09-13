package de.idos.updates.store;

import java.io.File;

public interface InstallationStarter {

  Installation start(File targetFolder, ProgressReport report);
}