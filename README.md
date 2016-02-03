#BasicAirData BluetoothHelper
##Bluetooth Java Class for Android
This Java Class implements an easy message-based Bluetooth wireless comunication layer between an **Android device** (the client) and a **Microcontroller** (the server).<br>
Using this class you can Connect, Disconnect, Send String messages, Receive String messages via Listener (the preferred way) or with explicit poll, automatically reconnect and check the status of your Bluetooth connection in a simple and thread-safe way.

This repository contains:
- <b>Android/BluetoothHelper.class</b> = The Helper Class for Android;

The repo includes also a very simple but fully functional example, that shows how to use the helper class:
- <b>Android/BluetoothHelperExample/</b> = Example app for Android (less than 100 lines of code, with comments!) that controls 2 leds (one ON/OFF using a button and one in PWM using a slider);
- <b>Arduino/BluetoothHelperExample/</b> = The sketch for Arduino; this example (ready to be uploaded on Arduino 2009) is made to communicate with the example app;
- <b>doc/</b> = The related documentation. The folder includes the electrical scheme of the microcontroller circuit, showing one of the possible hardware configurations (Arduino 2009 + BlueSMIRF Gold);

[BasicAirData](http://www.basicairdata.eu) Open and free DIY air data instrumentation and telemetry
[What is BasicAirData?](http://www.basicairdata.eu/attachments/others/BAD%20Brochure.pdf)

##License##
BasicAirData provides free software licensed under [GNU GPL v3.0](http://www.gnu.org/licenses/gpl-3.0.txt) General Public License

[License information](http://www.basicairdata.eu/copyright.html)

##Info##
[Contact information](http://www.basicairdata.eu/social.html)

##Acknowledgements##
[Current members](http://www.basicairdata.eu/about.html)


