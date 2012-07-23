Feature: Updates-R-Simple is easily configured
    In order to make the library accessible to developers, it has to be easily configurable.
    To allow for changes in configuration after deployment, the configuration must not be limited to the classpath.
    Since some people prefer software that does not change after installation, it must be able to use a set version.

   Scenario: Updates-R-Simple loads its configuration from the classpath
        Given a file called 'update.properties' on the classpath
        When I start the update system
        Then the system uses the file on the classpath to configure itself

    Scenario: Updates-R-Simple prefers configurations in the working directory
        Given a file called 'update.properties' on the classpath
        And a file called 'update.properties' in the working directory
        When I start the update system
        Then the system uses the file in the working directory to configure itself

    Scenario: Updates-R-Simple can use a fixed store for its version
        Given a file called 'update.properties' on the classpath
        When the file specifies a fixed version to be loaded
        Then the system points to that version to load