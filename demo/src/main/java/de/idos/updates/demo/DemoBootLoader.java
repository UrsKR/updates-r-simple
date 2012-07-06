package de.idos.updates.demo;

import de.idos.updates.*;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;

public class DemoBootLoader {

    public static void main(String[] arguments) throws Exception {
        String mainClass = "de.idos.updates.Demo";
        String mainMethod = "startDemo";
        String applicationName = "updatedemo";

        File userHome = new File(System.getProperty("user.home"));
        File applicationHome = new File(userHome, "." + applicationName);
        File versionStore = new File(applicationHome, "versions");
        VersionStore store = new FilesystemVersionStore(versionStore);

        UpdateSystem updateSystem = new UpdateSystem(store, new FilesystemRepository(new File("./src/main/resources")));
        updateSystem.checkForUpdatesSinceVersion(VersionFinder.BASE_VERSION);
        updateSystem.updateToLatestVersion();
        Version latestVersion = updateSystem.getLatestVersion();

        File versionFolder = new File(versionStore, latestVersion.asString());

        new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
    }
}