package net.sf.anathema;

import java.io.File;

public class AnathemaBootLoader {

    public static void main(String[] arguments) throws Exception {
        File classPathFolder = new File("./lib");
        String mainClass = "net.sf.anathema.Anathema";
        String mainMethod = "startApplication";
        new ApplicationLauncher(classPathFolder).launch(mainClass, mainMethod);
    }
}