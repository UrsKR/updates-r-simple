package net.sf.anathema;

import java.io.File;
import java.lang.reflect.Method;

public class ApplicationLauncher {
  public static ApplicationLauncher loadFromFolder(File classPathFolder) throws Exception {
    return new ApplicationLauncher(classPathFolder);
  }

  public static ApplicationLauncher loadFromSystemClasspath() throws Exception {
    return new ApplicationLauncher(ClassLoader.getSystemClassLoader());
  }

  private ClassLoader loader;

  private ApplicationLauncher(File classPathFolder) throws Exception {
    this(new EasyLoader(classPathFolder));
  }

  public ApplicationLauncher(ClassLoader loader) {
    this.loader = loader;
  }

  public void launch(String mainClass, String mainMethod) throws Exception {
    Class<?> aClass = loader.loadClass(mainClass);
    Object instance = aClass.newInstance();
    Method method = aClass.getMethod(mainMethod);
    method.invoke(instance);
  }
}