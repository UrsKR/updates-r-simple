Feature: Updates-R-Simple discovers and downloads updates
    In order to provide developers with a simple mechanism to update their applications,
    the library must discover, download and install updates from a version repository.

    Scenario: Updates-R-Simple discovers an update
        Given the repository contains a new version
        When the application checks for updates
        Then the library reports an update
        And the library reports the new version

    Scenario: Updates-R-Simple discovers several possible updates
        Given the repository contains several new versions
        When the application checks for updates
        Then the library reports the most recent version

    Scenario: The application is up-to-date
        Given the repository does not contain a new version
        When the application checks for updates
        Then the library does not indicate a new version
        And the library reports the current version as latest

    Scenario: Updates-R-Simple downloads updates
        Given the repository contains a new version
        When the application requests an update
        Then the library downloads and stores the required files

    Scenario: Updates-R-Simple deletes old versions
        Given the application was updated
        When I instruct the library to clean up
        Then the library deletes all version but current one

    Scenario: Updates-R-Simple supports HTTP-Repositories
        Given an HTTP-server with new versions
        And a repository for that server
        When the application checks for updates
        Then the library reports an update