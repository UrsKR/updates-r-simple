package de.idos.updates.lookup;

import de.idos.updates.NumericVersion;
import de.idos.updates.Update;
import de.idos.updates.UpdateAvailability;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileLookupTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private File available_versions;

  private LookupStrategy lookup;

  @Before
  public void fillRepository() throws Exception {
    available_versions = folder.newFolder("available_versions");
    lookup = new FileLookup(new File(folder.getRoot(), "available_versions"));
    addVersion("5.0.4");
  }

  @Test
  public void returnsLatestUpdate() throws Exception {
    Update latest = lookup.findLatestUpdate();
    assertThat(latest.isUpdateFrom(new NumericVersion(5, 0, 3)), is(UpdateAvailability.Available));
  }

  @Test
  public void returnsLatestUpdate2() throws Exception {
    Update latest = lookup.findLatestUpdate();
    assertThat(latest.isUpdateFrom(new NumericVersion(5, 0, 4)), is(UpdateAvailability.NotAvailable));
  }

  @Test
  public void returnsExpectedUpdateVersion() throws Exception {
    Update latest = lookup.findLatestUpdate();
    assertThat(latest.getVersion(), is(sameVersionAs(new NumericVersion(5, 0, 4))));
  }

  private void addVersion(String versionNumber) throws IOException {
    new File(available_versions, versionNumber).mkdir();
  }
}