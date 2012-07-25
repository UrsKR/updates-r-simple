package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataInVersion;
import de.idos.updates.store.Installation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FixedVersionStoreTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private VersionStore store;
    private VersionStore wrapped = mock(VersionStore.class);
    private Version version = new NumericVersion(1, 1, 1);

    @Before
    public void setUp() throws Exception {
        store = new FixedVersionStore(folder.getRoot(), wrapped);
    }

    @Test
    public void forwardsAdditionToWrappedStore() throws Exception {
        Installation installation = mock(Installation.class);
        when(wrapped.beginInstallation(version)).thenReturn(installation);
        Installation actualInstallation = store.beginInstallation(version);
        assertThat(actualInstallation, is(installation));
    }

    @Test
    public void statesLatestVersionFromWrappedStore() throws Exception {
        when(wrapped.getLatestVersion()).thenReturn(version);
        assertThat(store.getLatestVersion(), is(version));
    }
}