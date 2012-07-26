package de.idos.updates.util;

import de.idos.updates.store.ProgressReport;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnnouncerTest {

    @Test
    public void allowsAdditionOfListenersAfterAnnouncementStarted() throws Exception {
        ProgressReport report = mock(ProgressReport.class);
        Announcer<ProgressReport> announcer = Announcer.to(ProgressReport.class);
        ProgressReport proxyReport = announcer.announce();
        announcer.addListener(report);
        proxyReport.updateAlreadyInProgress();
        verify(report).updateAlreadyInProgress();
    }
}