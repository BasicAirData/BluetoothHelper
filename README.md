# BasicAirData BluetoothHelper
## Bluetooth Java Class for Android
This Java Class implements an easy message-based Bluetooth wireless communication layer between an **Android device** (the client) and a **Microcontroller** (the server).<br>
Using this class you can Connect, Disconnect, Send String messages, Receive String messages via Listener (best way) or by explicit polling, automatically reconnect and check the status of your Bluetooth connection in a simple and thread-safe way.

The library is compatible with android 4.0 (API 14) and newer.

For further information about this library and its usage you can read [this article](http://www.basicairdata.eu/projects/android/bluetooth-wireless-communication/).

## Dependencies
Use Gradle
```gradle
dependencies {
    compile 'eu.basicairdata:bluetoothhelper:1.0.5'
}
```

## Getting started
### Setup
- Add Bluetooth permissions to your AndroidManifest.xml:
```xml
<uses-permission android:name = "android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name = "android.permission.BLUETOOTH"/>
```
- Declare a new BluetoothHelper instance into your activity:
```java
BluetoothHelper mBluetoothHelper = new BluetoothHelper();
```
- Setup a BluetoothHelperListener to receive the messages and the changes of the connection status:
```java
mBluetoothHelper.setBluetoothHelperListener(new BluetoothHelper.BluetoothHelperListener() {
    @Override
    public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, final String message) {
        // Do something with the message received
        // runOnUiThread(new Runnable() {
        //     @Override
        //     public void run() {
        //         // Update your UI
        //     }
        // });
    }

    @Override
    public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected) {
        // Do something, depending on the new connection status
    }
});     
```
### Common Usage
- Connect to a bonded [BluetoothDevice](https://developer.android.com/reference/android/bluetooth/BluetoothDevice.html):
```java
mBluetoothHelper.Connect(mBluetoothDevice);
```
- As alternative, connect to a BluetoothDevice directly using its Device Name:
```java
mBluetoothHelper.Connect("HC-05");
```
- Send a message:
```java
mBluetoothHelper.SendMessage("Hello World");
```
- Check the connection status:
```java
mBluetoothHelper.isConnected();
```
- Disconnect:
```java
mBluetoothHelper.Disconnect();
```
For further information you can read the full [BluetoothHelper API Documentation](https://github.com/BasicAirData/BluetoothHelper/blob/master/doc/BluetoothHelper%20API%20Documentation.pdf).

## This repository contains
- <b>Android/BluetoothHelper.class</b> = The Helper Class for Android;

The repo includes also a very simple but fully functional example, that shows how to use the helper class:
- <b>Android/BluetoothHelperExample/</b> = Example app for Android - around 100 lines of code, including comments - that controls 2 leds (one ON/OFF using a button and one in PWM using a slider);
- <b>Arduino/BluetoothHelperExample/</b> = The sketch for Arduino; this example (ready to be uploaded on Arduino 2009) is made to communicate with the example app;
- <b>doc/</b> = The related documentation. The folder includes the API documentation of the Class and the electrical scheme of the microcontroller circuit, showing one of the possible hardware configurations (Arduino 2009 + BlueSMIRF Gold);

## License and General Information
[BasicAirData](http://www.basicairdata.eu) Open and free DIY air data instrumentation and telemetry.<br>
[License and general info](https://github.com/BasicAirData/Document-Templates/blob/master/general-info.md)<br>
[What is BasicAirData?](http://www.basicairdata.eu/attachments/others/BAD%20Brochure.pdf)
