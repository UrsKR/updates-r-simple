package de.idos.updates.demo;

import de.idos.updates.*;
import de.idos.updates.store.FilesystemVersionStore;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;

public class DemoBootLoader {

    public static void main(String[] arguments) throws Exception {
        String mainClass = "de.idos.updates.Demo";
        String mainMethod = "startDemo";
        String applicationName = "updatedemo";
        FilesystemVersionStore store = FilesystemVersionStore.inUserHomeForApplication(applicationName);
        UpdateSystem updateSystem = new UpdateSystem(store, new FilesystemRepository(new File("./src/main/resources")));
        updateSystem.updateToLatestVersion();
        File versionFolder = store.getFolderForLatestVersion();
        new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
    }
}