package de.idos.updates;

import de.idos.updates.store.Installation;
import de.idos.updates.store.OngoingInstallation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultUpdateConnectionTest {

  @Test
  public void handsOutInstallationFromTransfer() throws Exception {
    VersionDiscovery installedDiscovery = null;
    VersionReceptacle receptacle = null;
    VersionDiscovery availableDiscovery = null;
    VersionTransfer transfer = mock(VersionTransfer.class);
    OngoingInstallation installation = mock(Installation.class);
    Version version = mock(Version.class);
    when(transfer.transferVersionTo(version, receptacle)).thenReturn(installation);
    OngoingInstallation install = new DefaultUpdateConnection(installedDiscovery, receptacle, availableDiscovery, transfer).install(version);
    assertThat(install, is(installation));
  }
}
