This is an auto-update system for Java that hopes to be simple to integrate and use.

It is licensed under the Apache License v2.0.

Work so far was sponsored by **[IDOS AE GmbH](http://www.idos.de)**.
When setting out, my goal was to create an auto-updater that did not not force me to depend on a major framework, or required my dependencies to be signed.

## To Build
* You need JDK 6
* Run ``gradlew build``

## Example

To pull the latest update and then launch the main method ``Demo.startDemo()``:

```
    String mainClass = "de.idos.updates.Demo";
    String mainMethod = "startDemo";
    String applicationName = "updatedemo";
    FilesystemVersionStore store = FilesystemVersionStore.inUserHomeForApplication(applicationName);
    UpdateSystem updateSystem = new UpdateSystem(store, new FilesystemRepository(new File("./src/main/resources")));
    updateSystem.updateToLatestVersion();
    File versionFolder = store.getFolderForLatestVersion();
    new ApplicationLauncher(versionFolder).launch(mainClass, mainMethod);
```