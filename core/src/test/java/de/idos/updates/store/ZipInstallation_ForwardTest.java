package de.idos.updates.store;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZipInstallation_ForwardTest {

    private Installation wrapped = mock(Installation.class);
    private ProgressReport report = mock(ProgressReport.class);
    private ZipInstallation installation = new ZipInstallation(wrapped, report);

    @Test
    public void isRunningWhenWrappedIsRunning() throws Exception {
        when(wrapped.isRunning()).thenReturn(true);
        assertThat(installation.isRunning(), is(true));
    }

    @Test
    public void abortsInnerInstallationWhenAborting() throws Exception {
        installation.abort();
        verify(wrapped).abort();
    }

    @Test
    public void finishesInnerInstallationWhenFinishing() throws Exception {
        installation.finish();
        verify(wrapped).finish();
    }
}
