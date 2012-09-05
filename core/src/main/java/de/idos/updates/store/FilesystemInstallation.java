package de.idos.updates.store;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static de.idos.updates.store.InstallationUtil.createMarkerFile;

public class FilesystemInstallation implements Installation {
  public static Installation create(File versionFolder, ProgressReport report) {
    try {
      versionFolder.mkdirs();
      boolean lockCreatedSuccessFully = createMarkerFile(versionFolder);
      if (!lockCreatedSuccessFully) {
        return createNullInstallation(report);
      }
      return new FilesystemInstallation(versionFolder);
    } catch (IOException e) {
      return createNullInstallation(report);
    }
  }

  private static Installation createNullInstallation(ProgressReport report) {
    report.updateAlreadyInProgress();
    return new NullInstallation();
  }

  private boolean running = true;
  private File versionFolder;

  private FilesystemInstallation(File versionFolder) throws IOException {
    this.versionFolder = versionFolder;
  }

  @Override
  public synchronized void addContent(DataInVersion dataInVersion) {
    if (this.running) {
      dataInVersion.storeIn(versionFolder);
    }
  }

  @Override
  public synchronized void abort() {
    this.running = false;
    FileUtils.deleteQuietly(versionFolder);
  }

  @Override
  public synchronized void finish() {
    this.running = false;
    InstallationUtil.deleteMarkerFile(versionFolder);
  }

  @Override
  public boolean isRunning() {
    return running;
  }
}