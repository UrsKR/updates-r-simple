package de.idos.updates.lookup;

import de.idos.updates.NumericVersion;
import de.idos.updates.Update;
import de.idos.updates.UpdateAvailability;
import de.idos.updates.server.FileServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpLookupTest {


  private static FileServer fileServer;

  @BeforeClass
  public static void setUp() throws Exception {
    fileServer = new FileServer();
    fileServer.start();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    fileServer.stop();
  }

  HttpLookup lookup;

  @Before
  public void createLookup() throws MalformedURLException {
    lookup = new HttpLookup(new URL("http://localhost:8080/updates"));
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
}