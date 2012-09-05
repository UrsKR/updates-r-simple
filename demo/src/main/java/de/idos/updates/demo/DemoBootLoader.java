package de.idos.updates.demo;

import de.idos.updates.UpdateSystem;
import de.idos.updates.configuration.ConfiguredUpdateSystem;
import net.sf.anathema.ApplicationLauncher;

import java.io.File;

public class DemoBootLoader {

  public static final String MAIN_CLASS = "de.idos.updates.Demo";
  public static final String MAIN_METHOD = "startDemo";

  public static void main(String[] arguments) throws Exception {
    if (mainClassAlreadyOnClasspath()) {
      ApplicationLauncher.loadFromSystemClasspath().launch(MAIN_CLASS, MAIN_METHOD);
    }
    else{
      UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
      updateSystem.reportAllProgressTo(new ConsoleReport());
      updateSystem.checkForUpdates().updateToLatestVersion();
      File versionFolder = updateSystem.getFolderForVersionToRun();
      ApplicationLauncher.loadFromFolder(versionFolder).launch(MAIN_CLASS, MAIN_METHOD);
    }
  }

  private static boolean mainClassAlreadyOnClasspath() {
    try {
      ClassLoader.getSystemClassLoader().loadClass(MAIN_CLASS);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}