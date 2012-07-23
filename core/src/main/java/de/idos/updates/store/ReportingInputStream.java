package de.idos.updates.store;

import org.apache.commons.io.input.ProxyInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ReportingInputStream extends ProxyInputStream {
    private ProgressReport report;

    public ReportingInputStream(InputStream wrappedStream, ProgressReport report) {
        super(wrappedStream);
        this.report = report;
    }

    @Override
    public synchronized long skip(final long length) throws IOException {
        final long skip = super.skip(length);
        report.progress(length);
        return skip;
    }

    @Override
    protected synchronized void afterRead(int bytes) {
        if (bytes == -1) {
            report.done();
        } else {
            report.progress(bytes);
        }
    }
}