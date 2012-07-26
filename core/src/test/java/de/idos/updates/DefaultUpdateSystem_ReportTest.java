package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.Mockito.*;

public class DefaultUpdateSystem_ReportTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    Repository repository;
    VersionStore versionStore = mock(VersionStore.class);
    DefaultUpdateSystem updateSystem;

    @Before
    public void setUp() throws Exception {
        repository = spy(new FilesystemRepository(folder.getRoot()));
        folder.newFolder(FilesystemRepository.AVAILABLE_VERSIONS, "5.3.2");
        updateSystem = new DefaultUpdateSystem(versionStore, repository);
    }

    @Test
    public void installsReportOnRepository() throws Exception {
        verify(repository, atLeastOnce()).reportAllProgressTo(Mockito.isA(ProgressReport.class));
    }

    @Test
    public void installsReportOnTransfer() throws Exception {
        VersionTransfer transfer = mock(VersionTransfer.class);
        new DefaultUpdateSystem(versionStore, repository, transfer);
        verify(transfer).reportAllProgressTo(Mockito.isA(ProgressReport.class));
    }

    @Test
    public void installsReportOnDiscovery() throws Exception {
        VersionDiscovery discovery = mock(VersionDiscovery.class);
        new DefaultUpdateSystem(versionStore, discovery, repository);
        verify(discovery).reportAllProgressTo(Mockito.isA(ProgressReport.class));
    }

    @Test
    public void installsReportOnStore() throws Exception {
        verify(versionStore).reportAllProgressTo(Mockito.isA(ProgressReport.class));
    }

    @Test
    public void reportsToAnyNumberOfListeners() throws Exception {
        ProgressReport firstReport = mock(ProgressReport.class);
        ProgressReport secondReport = mock(ProgressReport.class);
        updateSystem.reportAllProgressTo(firstReport);
        updateSystem.reportAllProgressTo(secondReport);
        updateSystem.checkForUpdates();
        verify(firstReport).lookingUpLatestAvailableVersion();
        verify(secondReport).lookingUpLatestAvailableVersion();
    }

    @Test
    public void doesNotReportToRemovedListener() throws Exception {
        ProgressReport firstReport = mock(ProgressReport.class);
        updateSystem.reportAllProgressTo(firstReport);
        updateSystem.stopReportingTo(firstReport);
        updateSystem.checkForUpdates();
        verifyZeroInteractions(firstReport);
    }
}