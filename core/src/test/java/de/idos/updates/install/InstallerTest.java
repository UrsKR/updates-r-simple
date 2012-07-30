package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InstallerTest {

  @SuppressWarnings("unchecked")
  InstallationStrategy<Object> strategy = Mockito.mock(InstallationStrategy.class);
  ProgressReport report = mock(ProgressReport.class);
  Version version = mock(Version.class);
  Installer installer = new DefaultInstaller<Object>(strategy, report);

  @Test
  public void reportsStartOfInstallation() throws Exception {
    installer.install(version);
    verify(report).startingInstallationOf(version);
  }

  @Test
  public void reportsTheStartOfTheUpdateProcess() throws Exception {
    installer.install(version);
    verify(report).assemblingFileList();
  }

  @Test
  public void reportsTheNumberOfFilesToInstall() throws Exception {
    when(strategy.findAllElementsToInstall(version)).thenReturn(Collections.nCopies(20, new Object()));
    installer.install(version);
    verify(report).foundElementsToInstall(20);
  }

  @Test
  public void reportsEndOfInstallation() throws Exception {
    installer.install(version);
    verify(report).finishedInstallation();
  }

  @Test
  public void reportsErrorDuringInstallation() throws Exception {
    RuntimeException runtimeException = new RuntimeException();
    when(strategy.findAllElementsToInstall(version)).thenThrow(runtimeException);
    installer.install(version);
    verify(report).installationFailed(runtimeException);
  }

  @Test
  public void finalizesInstallationWhenAllIsDone() throws Exception {
    installer.install(version);
    verify(strategy).finalizeInstallation();
  }
}