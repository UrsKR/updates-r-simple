package de.idos.updates.demo;

import de.idos.updates.*;
import de.idos.updates.configuration.ConfiguredUpdateSystem;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;

public class DemoBootLoader {

    public static void main(String[] arguments) throws Exception {
        String mainClass = "de.idos.updates.Demo";
        String mainMethod = "startDemo";
        UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
        updateSystem.reportAllProgressTo(new ConsoleReport());
        updateSystem.checkForUpdates().updateToLatestVersion();
        File versionFolder = updateSystem.getFolderForVersionToRun();
        new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
    }
}