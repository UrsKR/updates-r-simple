package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import org.junit.Test;

import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpInstallerTest {

    Version version = mock(Version.class);
    ProgressReport report = mock(ProgressReport.class);
    Installation installation = mock(Installation.class);

    @Test
    public void reportsInstallation() throws Exception {
        URL baseUrl = new URL("http://www.idos.de");
        new HttpInstaller(report, baseUrl, installation).installElement("name", version);
        verify(report).installingFile("name");
    }
}
