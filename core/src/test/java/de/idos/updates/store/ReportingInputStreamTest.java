package de.idos.updates.store;

import org.junit.Test;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReportingInputStreamTest {

    InputStream wrapped = mock(InputStream.class);
    ProgressReport report = mock(ProgressReport.class);
    ReportingInputStream stream = new ReportingInputStream(wrapped, report);

    @Test
    public void reportsEndOfTransfer() throws Exception {
        stream.afterRead(-1);
        verify(report).finishedFile();
    }

    @Test
    public void reportsSkip() throws Exception {
        stream.skip(5);
        verify(report).progress(5);
    }
}
