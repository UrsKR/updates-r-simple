Feature: Updates-R-Simple reports its status
    In order to provide users with feedback while the update is in running,
    the library must report its status and progress to registered clients

    Scenario: Updates-R-Simple reports download progress
        Given the repository contains a new version
        And I have registered a client to receive status updates
        When the application requests an update
        Then the library reports the download's progress to the client