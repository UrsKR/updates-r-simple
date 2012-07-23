package de.idos.updates.store;

import org.junit.Test;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReportingInputStreamTest {

    @Test
    public void reportsEndOfTransfer() throws Exception {
        ProgressReport report = mock(ProgressReport.class);
        InputStream wrapped = mock(InputStream.class);
        ReportingInputStream stream = new ReportingInputStream(wrapped, report);
        stream.afterRead(-1);
        verify(report).done();
    }
}
