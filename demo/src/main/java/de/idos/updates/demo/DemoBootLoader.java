package de.idos.updates.demo;

import de.idos.updates.*;
import de.idos.updates.store.FilesystemVersionStore;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DemoBootLoader {

    public static void main(String[] arguments) throws Exception {
        String mainClass = "de.idos.updates.Demo";
        String mainMethod = "startDemo";
        UpdateSystem updateSystem = createUpdateSystem();
        updateSystem.reportAllProgressTo(new ConsoleReport());
        updateSystem.updateToLatestVersion();
        File versionFolder = updateSystem.getFolderForLatestVersion();
        new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
    }

    private static UpdateSystem createUpdateSystem() {
        String applicationName = "updatedemo";
        VersionStore store = VersionStoreBuilder.inUserHomeForApplication(applicationName).create();
        return new UpdateSystem(store, new FilesystemRepository(new File("./src/main/resources")));
    }
}