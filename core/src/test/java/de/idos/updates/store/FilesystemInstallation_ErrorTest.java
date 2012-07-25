package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FilesystemInstallation_ErrorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    ProgressReport report = mock(ProgressReport.class);

    @Test
    public void returnsNullInstallationIfInstallationIsAlreadyInProgress() throws Exception {
        InstallationUtil.createMarkerFile(folder.getRoot());
        Installation installation = FilesystemInstallation.create(folder.getRoot(), report);
        assertThat(installation, is(instanceOf(NullInstallation.class)));
    }

    @Test
    public void reportsProblem() throws Exception {
        InstallationUtil.createMarkerFile(folder.getRoot());
        FilesystemInstallation.create(folder.getRoot(), report);
        verify(report).updateAlreadyInProgress();
    }
}
