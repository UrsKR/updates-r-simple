package de.idos.updates.store;

public interface Installation {
  void addContent(DataInVersion dataInVersion);

  void abort();

  void finish();

  boolean isRunning();
}