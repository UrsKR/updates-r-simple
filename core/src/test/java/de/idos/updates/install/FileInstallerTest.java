package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FileInstallerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    Version version = mock(Version.class);
    ProgressReport report = mock(ProgressReport.class);
    Installation installation = mock(Installation.class);
    private FileInstaller fileInstaller;

    @Before
    public void setUp() throws Exception {
        File sourceFolder = folder.newFolder();
        fileInstaller = new FileInstaller(report, sourceFolder, installation);
    }

    @Test
    public void reportsInstallation() throws Exception {
        File element = folder.newFile();
        fileInstaller.installElement(element, version);
        verify(report).installingFile(element.getName());
    }

    @Test
    public void abortsInstallationIfAnErrorOccurs() throws Exception {
        fileInstaller.handleException();
        verify(installation).abort();
    }

    @Test
    public void forwardsFinalizationCallToInstallationInstance() throws Exception {
        fileInstaller.finalizeInstallation();
        verify(installation).finish();
    }
}