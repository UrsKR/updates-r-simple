This is an auto-update system for Java that hopes to be simple to integrate and use.

It is licensed under the Apache License v2.0.

Initial work was sponsored by **[IDOS AE GmbH](http://www.idos.de)**.
When setting out, my goal was to create an auto-updater that did not not force me to depend on a major framework, or required my dependencies to be signed.

## To Build
* You need JDK 6
* Run ``gradlew build``

## To See
* Run ``FXCalendarDemo``

![Main view](https://github.com/UrsKR/fxcalendar/raw/master/screenshots/calendarmain.PNG)
![Quick navigation](https://github.com/UrsKR/fxcalendar/raw/master/screenshots/calendaryearchooser.PNG)

## To Use
* For a one stop solution, instantiate a ``FXCalendar`` and add it to your pane.
* For a clickable calendar image, use the ``DatePopupButton`` and add its component to your pane.
* If you just want the calendar popup, create your own button, and make it trigger a ``DatePickerPopup``

## Example

```
    CalendarProperties properties = new CalendarProperties();
    DateSelection selection = ...; //This is where the selected date ends up
    DatePickerPopup popup = new DatePickerPopup(representation, properties, selection);
    DatePopupButton popUpButton = new DatePopupButton(popup);
```