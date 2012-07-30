package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.Version;
import de.idos.updates.VersionDiscovery;
import de.idos.updates.store.ProgressReport;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FallbackVersionDiscoveryTest {
  VersionDiscovery wrapped = mock(VersionDiscovery.class);
  Version fallback = new NumericVersion(1, 0, 0);
  FallbackVersionDiscovery discovery = new FallbackVersionDiscovery(wrapped, fallback);

  @Test
  public void returnsOriginalVersionIfItIsNotTheNullVersion() throws Exception {
    Version original = new NumericVersion(0, 8, 9);
    when(wrapped.getLatestVersion()).thenReturn(original);
    assertThat(discovery.getLatestVersion(), is(original));
  }

  @Test
  public void forwardsReportingToWrapped() throws Exception {
    ProgressReport report = mock(ProgressReport.class);
    discovery.reportAllProgressTo(report);
    verify(wrapped).reportAllProgressTo(report);
  }
}
