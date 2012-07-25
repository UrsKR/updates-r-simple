package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
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

    @Test
    public void reportsInstallation() throws Exception {
        File element = folder.newFile();
        File sourceFolder = folder.newFolder();
        FileInstaller fileInstaller = new FileInstaller(report, sourceFolder, installation);
        fileInstaller.installElement(element, version);
        verify(report).installingFile(element.getName());

    }
}
