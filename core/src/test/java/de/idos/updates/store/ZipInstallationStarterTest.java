package de.idos.updates.store;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZipInstallationStarterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private InstallationStarter wrapped = mock(InstallationStarter.class);
    private InstallationStarter starter = new ZipInstallationStarter(wrapped);
    private ProgressReport report = mock(ProgressReport.class);

    @Test
    public void startsInstallationWithWrappedStarter() throws Exception {
        Installation wrappedInstallation = mock(Installation.class);
        when(wrapped.start(folder.getRoot(), report)).thenReturn(wrappedInstallation);
        Installation installation = starter.start(folder.getRoot(), report);
        installation.finish();
        verify(wrappedInstallation).finish();
    }

    @Test
    public void returnsZipInstallation() throws Exception {
        Installation installation = starter.start(folder.getRoot(), report);
        assertThat(installation, CoreMatchers.is(instanceOf(ZipInstallation.class)));
    }
}
