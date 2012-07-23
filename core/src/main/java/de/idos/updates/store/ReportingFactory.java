package de.idos.updates.store;

import java.io.IOException;
import java.io.InputStream;

public class ReportingFactory implements InputStreamFactory {
    private final InputStreamFactory factory;
    private final ProgressReport report;

    public ReportingFactory(InputStreamFactory factory, ProgressReport report) {
        this.factory = factory;
        this.report = report;
    }

    @Override
    public InputStream openStream() throws IOException {
        InputStream wrappedStream = factory.openStream();
        report.expectedSize(factory.getExpectedSize());
        return new ReportingInputStream(wrappedStream, report);
    }

    @Override
    public long getExpectedSize() throws IOException {
        return factory.getExpectedSize();
    }
}