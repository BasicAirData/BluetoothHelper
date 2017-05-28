# BasicAirData BluetoothHelper
## Bluetooth Java Class for Android
This Java Class implements an easy message-based Bluetooth wireless communication layer between an **Android device** (the client) and a **Microcontroller** (the server).<br>
Using this class you can Connect, Disconnect, Send String messages, Receive String messages via Listener (best way) or with explicit polling, automatically reconnect and check the status of your Bluetooth connection in a simple and thread-safe way.

For further information about this library and its usage you can read [this article](http://www.basicairdata.eu/projects/android/bluetooth-wireless-communication/).

## Dependencies

Use Gradle
```gradle
dependencies {
    compile 'eu.basicairdata:bluetoothhelper:1.0.5'
}
```

## This repository contains
- <b>Android/BluetoothHelper.class</b> = The Helper Class for Android;

The repo includes also a very simple but fully functional example, that shows how to use the helper class:
- <b>Android/BluetoothHelperExample/</b> = Example app for Android - around 100 lines of code, including comments - that controls 2 leds (one ON/OFF using a button and one in PWM using a slider);
- <b>Arduino/BluetoothHelperExample/</b> = The sketch for Arduino; this example (ready to be uploaded on Arduino 2009) is made to communicate with the example app;
- <b>doc/</b> = The related documentation. The folder includes the API documentation of the Class and the electrical scheme of the microcontroller circuit, showing one of the possible hardware configurations (Arduino 2009 + BlueSMIRF Gold);

[BasicAirData](http://www.basicairdata.eu) Open and free DIY air data instrumentation and telemetry.<br>
[What is BasicAirData?](http://www.basicairdata.eu/attachments/others/BAD%20Brochure.pdf)

## License and General Information
[License and general info](https://github.com/BasicAirData/Document-Templates/blob/master/general-info.md)
