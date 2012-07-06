package net.sf.anathema;

import java.io.File;
import java.lang.reflect.Method;

public class ApplicationLauncher {
    private File classPathFolder;

    public ApplicationLauncher(File classPathFolder) {
        this.classPathFolder = classPathFolder;
    }

    public void launch(String mainClass, String mainMethod) throws Exception {
        EasyLoader loader = new EasyLoader(classPathFolder);
        Class<?> aClass = loader.loadClass(mainClass);
        Object instance = aClass.newInstance();
        Method method = aClass.getMethod(mainMethod);
        method.invoke(instance);
    }
}