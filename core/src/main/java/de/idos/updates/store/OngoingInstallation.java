package de.idos.updates.store;

public interface OngoingInstallation {
  void abort();

  boolean isRunning();
}