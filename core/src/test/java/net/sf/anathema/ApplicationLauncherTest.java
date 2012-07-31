package net.sf.anathema;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApplicationLauncherTest {

    @Test
    public void callsMainClass() throws Exception {
        ApplicationLauncher.loadFromFolder(new File(".")).launch("net.sf.anathema.DemoLauncherClass", "launch");
        assertThat(DemoLauncherClass.launched, is(true));
    }
}
