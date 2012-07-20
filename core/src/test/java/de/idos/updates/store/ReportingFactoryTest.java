package de.idos.updates.store;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReportingFactoryTest {

    InputStreamFactory factory = new LongInputStreamFactory(20);
    ProgressReport report = mock(ProgressReport.class);
    ReportingFactory reportingFactory = new ReportingFactory(factory, report);

    @Test
    public void reportsTotalSizeWhenOpeningTheStream() throws Exception {
        reportingFactory.openStream();
        verify(report).expectedSize(20);
    }

    @Test
    public void reportsLengthFromWrappedFactory() throws Exception {
        assertThat(reportingFactory.getExpectedSize(),is(20L));
    }
}