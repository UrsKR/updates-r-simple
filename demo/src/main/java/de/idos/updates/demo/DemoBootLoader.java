package de.idos.updates.demo;

import de.idos.updates.*;
import de.idos.updates.configuration.ConfiguredUpdateSystemFactory;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;

public class DemoBootLoader {

    public static void main(String[] arguments) throws Exception {
        String mainClass = "de.idos.updates.Demo";
        String mainMethod = "startDemo";
        UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
        updateSystem.reportAllProgressTo(new ConsoleReport());
        updateSystem.updateToLatestVersion();
        File versionFolder = updateSystem.getFolderForLatestVersion();
        new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
    }
}