package net.sf.anathema;

import de.idos.updates.RootFolderSelector;
import org.junit.Test;

import java.io.File;

public class EasyLoaderTest {

    File rootFolder = new RootFolderSelector().getRootFolder();

    @Test
    public void canLoadClassFromSingleJar() throws Exception {
        EasyLoader loader = new EasyLoader(new File(rootFolder, "src/test/resources/jaxen-1.1.3.jar"));
        loader.loadClass("org.jaxen.BaseXPath");
    }

    @Test
    public void canLoadClassFromJarInFolder() throws Exception {
        EasyLoader loader = new EasyLoader(new File(rootFolder, "src/test/resources/"));
        loader.loadClass("org.jaxen.BaseXPath");
    }

    @Test
    public void canLoadClassFromAnyJarInFolder() throws Exception {
        EasyLoader loader = new EasyLoader(new File(rootFolder, "src/test/resources/"));
        loader.loadClass("net.disy.commons.core.util.ArrayUtilities");
    }

    @Test
    public void canLoadSystemClasses() throws Exception {
        EasyLoader loader = new EasyLoader(new File(rootFolder, "src/test/resources/"));
        loader.loadClass("java.lang.Math");
    }
}
