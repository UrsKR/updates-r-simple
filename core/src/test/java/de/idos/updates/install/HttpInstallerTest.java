package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpInstallerTest {

    Version version = mock(Version.class);
    ProgressReport report = mock(ProgressReport.class);
    Installation installation = mock(Installation.class);
    HttpInstaller httpInstaller;

    @Before
    public void setUp() throws Exception {
        URL baseUrl = new URL("http://www.idos.de");
        httpInstaller = new HttpInstaller(report, baseUrl, installation);
    }

    @Test
    public void reportsInstallation() throws Exception {
        httpInstaller.installElement("name", version);
        verify(report).installingFile("name");
    }

    @Test
    public void forwardsFinalizationCallToInstallationInstance() throws Exception {
        httpInstaller.finalizeInstallation();
        verify(installation).finish();
    }
}
