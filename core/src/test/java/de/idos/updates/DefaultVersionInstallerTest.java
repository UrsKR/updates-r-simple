package de.idos.updates;

import de.idos.updates.store.Installation;
import de.idos.updates.store.OngoingInstallation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultVersionInstallerTest {

  VersionTransfer transfer = mock(VersionTransfer.class);
  VersionReceptacle receptacle = mock(VersionReceptacle.class);
  DefaultVersionInstaller installer = new DefaultVersionInstaller(transfer, receptacle);

  @Test
  public void handsOutInstallationFromTransfer() throws Exception {
    OngoingInstallation installation = mock(Installation.class);
    Version version = mock(Version.class);
    when(transfer.transferVersionTo(version, receptacle)).thenReturn(installation);
    OngoingInstallation install = installer.install(version);
    assertThat(install, is(installation));
  }
}
