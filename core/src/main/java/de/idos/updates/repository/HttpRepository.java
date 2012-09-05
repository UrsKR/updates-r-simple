package de.idos.updates.repository;

import de.idos.updates.install.HttpInstaller;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.lookup.HttpLookup;
import de.idos.updates.lookup.LookupStrategy;
import de.idos.updates.store.Installation;

import java.io.IOException;
import java.net.URL;

public class HttpRepository extends AbstractRepository<String> {
  private final URL baseUrl;

  public HttpRepository(String url) {
    try {
      this.baseUrl = new URL(url);
    } catch (IOException e) {
      throw new IllegalArgumentException("Please specify a valid URL as your repository base.", e);
    }
  }

  @Override
  protected LookupStrategy createLookup() {
    return new HttpLookup(baseUrl);
  }

  @Override
  protected InstallationStrategy<String> createInstallationStrategy(Installation installation) {
    return new HttpInstaller(getReport(), baseUrl, installation);
  }
}