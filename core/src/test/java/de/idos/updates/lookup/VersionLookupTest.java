package de.idos.updates.lookup;

import de.idos.updates.NumericVersion;
import de.idos.updates.store.ProgressReport;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VersionLookupTest {
    ProgressReport report = mock(ProgressReport.class);
    LookupStrategy strategy = mock(LookupStrategy.class);
    private VersionLookup versionLookup = new VersionLookup(strategy, report);

    @Test
    public void reportsVersionLookup() throws Exception {
        versionLookup.lookUpLatestVersion();
        verify(report).lookingUpLatestAvailableVersion();
    }

    @Test
    public void reportsLatestVersion() throws Exception {
        NumericVersion value = new NumericVersion(3, 2, 1);
        when(strategy.findLatestVersion()).thenReturn(value);
        versionLookup.lookUpLatestVersion();
        verify(report).latestAvailableVersionIs(value);
    }

    @Test
    public void reportsLookupFailure() throws Exception {
        RuntimeException exception = new RuntimeException();
        when(strategy.findLatestVersion()).thenThrow(exception);
        versionLookup.lookUpLatestVersion();
        verify(report).versionLookupFailed(exception);
    }
}
