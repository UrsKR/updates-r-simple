This is an **auto-update system for Java** that hopes to be **simple to integrate** and use.
The library is designed to be **thread-safe** and should even handle multiple VMs updating simultaneously.
It **uses threads internally** to provide a more capable API,
 and you should watch for bad access if you use it in a GUI framework.

It is licensed under the Apache License v2.0.

Work so far was sponsored by **[IDOS AE GmbH](http://www.idos.de)**.
When setting out, my goal was to create an auto-updater that did not force me to depend on a major framework, or required my dependencies to be signed.

#### Current Topic of Interest
Testing the new zip support in a real-life application.

## Maven Coordinates
The project is in Maven Central at ``de.idos.updates:updates-core:1.1.1``

## To Build
* You need JDK 6
* Run ``gradlew build``
* Distribute through ``gradlew uploadArchives`` (Sonatype registration required)

## To Use
1. Instantiate a ``Configurator`` and use its API to create a configuration file.
2. Put the resulting file on the classpath or into your working directory.
3. Pull the latest update and then launch the main method (e.g. ``Demo.startDemo()``):

```
    String mainClass = "de.idos.updates.Demo";
    String mainMethod = "startDemo";
    UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
    updateSystem.reportAllProgressTo(new ConsoleReport());
    updateSystem.checkForUpdates().updateToLatestVersion();
    File versionFolder = updateSystem.getFolderForVersionToRun();
    new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
```

For a more complex example that can also cope with IDEs, please refer to the demo module.

## Changelog

#### v1.1.1
* Uses the full base-URL to retrieve the list of available updates, if it ends in a slash "/".
* Java 6 required (once again)

#### v1.1.0
* Unpacks zipped archives in the update fileset automatically
* Uses HEAD requests to get the file size, saving bandwidth and server load.

#### v1.0.1
* Java 6 required (instead of Java 7)

#### v1.0.0
* Initial Release
* Discovers new versions
* Downloads and installs updates
* Dynamic classloading
* Java 7 required
