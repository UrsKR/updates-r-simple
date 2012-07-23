package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class DataImportTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    InputStreamFactory factory = new LongInputStreamFactory(3000);

    @Test
    public void storesInputInDesignatedFile() throws Exception {
        new DataImport().takeDataFromFactory(factory).andStoreThemIn(folder.getRoot(), "Test");
        File resultFile = new File(folder.getRoot(), "Test");
        assertThat(resultFile.length(), is(3000L));
    }

    @Test
    public void reportsProgressToMonitor() throws Exception {
        ProgressReport report = mock(ProgressReport.class);
        new DataImport().takeDataFromFactory(factory).reportProgressTo(report).andStoreThemIn(folder.getRoot(), "Test");
        InOrder inOrder = inOrder(report);
        inOrder.verify(report).expectedSize(3000);
        inOrder.verify(report, atLeastOnce()).progress(isA(Long.class));
    }
}