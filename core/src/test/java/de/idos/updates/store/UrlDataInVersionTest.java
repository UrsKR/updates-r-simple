package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UrlDataInVersionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void addsToGivenImport() throws Exception {
        String name = "test";
        URL url = new URL("http://test.de");
        DataImport dataImport = mock(DataImport.class);
        when(dataImport.takeDataFromFactory(any(InputStreamFactory.class))).thenReturn(dataImport);
        new UrlDataInVersion(url, name, dataImport).storeIn(folder.getRoot());
        verify(dataImport).takeDataFromFactory(isA(UrlStreamFactory.class));
        verify(dataImport).andStoreThemIn(folder.getRoot(), name);
    }
}
