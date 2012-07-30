package de.idos.updates.store;

public interface Installation extends OngoingInstallation {
  void addContent(DataInVersion dataInVersion);

  void finish();
}