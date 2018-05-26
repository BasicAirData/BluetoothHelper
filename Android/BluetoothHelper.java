package eu.basicairdata.bluetoothhelper;

/**
 * BluetoothHelper Java Helper Class for Android
 * Created by G.Capelli (BasicAirData) on 06/02/16.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 **/

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
* A Bluetooth Java helper Class for Android. 
* This Java Class implements an easy message-based Bluetooth wireless communication layer between an **Android device** (the client) and a **Microcontroller** (the server).
* 
* Using this class you can Connect, Disconnect, Send String messages, Receive String messages via Listener (best way)
* or with explicit polling, automatically reconnect and check the status of your Bluetooth connection in a simple and thread-safe way.<br>
* You can read the incoming messages attaching a Listener or using explicit polling.<br>
* Connection, reading and writing processes are asynchronously made using 3 separated Threads.<br>
* This Class is compatible with Android 4.0+
* @version 1.0.6b_20180526
* @author BasicAirData
*/

public class BluetoothHelper {

    private BluetoothAdapter mBluetoothAdapter = null;
    private boolean isInStreamConnected = false;
    private boolean isOutStreamConnected = false;
    private ConnectThread CT = null;
    private ConnectedThreadClass_Read readThread = null;
    private ConnectedThreadClass_Write writeThread = null;
    private Handler handler = new Handler();
    private BlockingQueue<String> inputMessagesQueue = new LinkedBlockingQueue<String>();
    private BlockingQueue<String> outputMessagesQueue = new LinkedBlockingQueue<String>();


    // -------------------------------------------------------------------------------------------- The thread class that takes care of connection process
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private BluetoothSocket tmp = null;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            mmDevice = device;
            try {
                Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                try {
                    tmp = (BluetoothSocket) m.invoke(mmDevice, 1);
                } catch (IllegalAccessException e) {
                    Log.w("myApp", "[ ! ] Unable to connect socket (IllegalAccessException): " + e);
                } catch (InvocationTargetException e) {
                    Log.w("myApp", "[ ! ] Unable to connect socket (InvocationTargetException): " + e);
                }
            } catch (NoSuchMethodException e) {
                Log.w("myApp", "[ ! ] Unable to connect socket (NoSuchMethodException): " + e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.w("myApp", "[ ! ] Connection through socket failed: " + connectException);
                Log.w("myApp", "[ ! ] Trying fallback method");
                try {
                    // fallback method for android >= 4.2
                    tmp = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                } catch (IllegalAccessException e) {
                    Log.w("myApp", "[ ! ] Failed to create fallback Illegal Access: " + e);
                    return;
                } catch (IllegalArgumentException e) {
                    Log.w("myApp", "[ ! ] Failed to create fallback Illegal Argument: " + e);
                    return;
                } catch (InvocationTargetException e) {
                    Log.w("myApp", "[ ! ] Failed to create fallback Invocation Target" + e);
                    return;
                } catch (NoSuchMethodException e) {
                    Log.w("myApp", "[ ! ] Failed to create fallback No Such Method" + e);
                    return;
                }
                try {
                    // linked to tmp, so basicly a new socket
                    mmSocket.connect();
                } catch (IOException e) {
                    Log.w("myApp", "[ ! ] Failed to connect with fallback socket: " + e);
                    try {
                        sleep(200);
                    } catch (InterruptedException ex) {
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            InCaseFireonBluetoothHelperConnectionStateChanged();
                        }
                    });
                    return;
                }
                Log.w("myApp", "[ # ] Succesfully connected with fallback socket");
            }
            // Do work to manage the connection (in a separate thread)
            // manageConnectedSocket(mmSocket);
            Log.w("myApp", "[ # ] Socket connected. Opening streams....");
            readThread = new ConnectedThreadClass_Read(mmSocket);
            readThread.start();
            writeThread = new ConnectedThreadClass_Write(mmSocket);
            writeThread.start();

            boolean oldConnectionStatus = false;
            // Check for termination request
            do {
                //checkConnectionStatus();
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    Log.w("myApp", "[ ! ] ConnectThread Interrupted");
                    break;
                }
                // check the status of the connection and send listeners in case of changes
                if (isConnected() != oldConnectionStatus) {
                    oldConnectionStatus = isConnected();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            InCaseFireonBluetoothHelperConnectionStateChanged();
                        }
                    });
                }
            } while (isConnected());
            cancel();
            //checkConnectionStatus();
            Log.w("myApp", "[ # ] Socket closed");
        }

        //Will cancel an connection and close streams and socket
        public void cancel() {
            isInStreamConnected = false;
            isOutStreamConnected = false;
            try {
                readThread.interrupt();
                writeThread.interrupt();
                if (mmSocket.isConnected()) mmSocket.close();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
    }


    private void InCaseFireonBluetoothHelperConnectionStateChanged() {
        if (listener != null) {
            if (isConnected()) Log.w("myApp", "[ # ] Listener fired: onBluetoothHelperConnectionStateChanged = true");
            else Log.w("myApp", "[ # ] Listener fired: onBluetoothHelperConnectionStateChanged = false");
            listener.onBluetoothHelperConnectionStateChanged(this, isConnected()); // <---- fire listener
        }
    }


    // -------------------------------------------------------------------------------------------- The thread class that READS messages from BT
    private class ConnectedThreadClass_Read extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        public ConnectedThreadClass_Read(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = mmSocket.getInputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            isInStreamConnected = true;
        }

        public void run() {
            Log.w("myApp", "[ # ] Input Stream opened");
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                byte ch;
                int i = 0;
                try {
                    // Read from the InputStream until DELIMITER found
                    while ((ch = (byte) mmInStream.read()) != Delimiter) {
                        buffer[i++] = ch;
                    }
                    buffer[i] = '\0';
                    final String msg = new String(buffer);
                    MessageReceived(msg.trim());
                } catch (IOException e) {
                    isInStreamConnected = false;
                    break;
                }
            }
            isInStreamConnected = false;
            Log.w("myApp", "[ # ] Input stream closed");
        }
    }

    // Service function: Message received!
    private void MessageReceived(String msg) {
        // if the listener is attached fire it
        // else put the message into buffer to be read
        Log.w("myApp", "[ # ] Message received: " + msg);
        try {
            if (listener != null) {
                Log.w("myApp", "[ # ]  Listener fired: onBluetoothHelperMessageReceived");
                listener.onBluetoothHelperMessageReceived(this, msg); // <---- fire listener
            } else if (!inputMessagesQueue.offer(msg))
                Log.w("myApp", "[ ! ] Message thrown (unable to store into buffer): " + msg)
                ;
        } catch (Exception e) {
            Log.w("myApp", "[ ! ] Failed to receive message: " + e.getMessage());
        }
    }


    // -------------------------------------------------------------------------------------------- The thread class that WRITES messages to BT
    private class ConnectedThreadClass_Write extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;

        public ConnectedThreadClass_Write(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
            }
            mmOutStream = tmpOut;
            isOutStreamConnected = true;
        }

        public void run() {
            Log.w("myApp", "[ # ] Output Stream opened");
            // Keep sending messages to OutputStream until an exception occurs
            while (true) {
                String msg;
                try {
                    msg = outputMessagesQueue.take();
                } catch (InterruptedException e) {
                    isOutStreamConnected = false;
                    Log.w("myApp", "[ ! ] Buffer not available: " + e.getMessage());
                    break;
                }
                try {
                    mmOutStream.write(msg.getBytes());
                    mmOutStream.write(Delimiter);
                    Log.w("myApp", "[ # ] Message send: " + msg);
                } catch (IOException e) {
                    isOutStreamConnected = false;
                    Log.w("myApp", "[ ! ] Unable to write data to output stream: " + e.getMessage());
                    break;
                }
            }
            isOutStreamConnected = false;
            Log.w("myApp", "[ # ] Output stream closed");
        }
    }


    // -------------------------------------------------------------------------------------------- Listeners interface
    public interface BluetoothHelperListener {
        /**
        * Event fired each time a message is received from the remote device.
        * The event is received by your listeners in a worker thread;
        * @param bluetoothhelper The BluetoothHelper class
        * @param message The message received
        */
        public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, String message);

        /**
        * Event fired when the connection status changes.
        * The event is also fired when the Connect() method ends, returning the result of the Connect() request.
        * @param bluetoothhelper The BluetoothHelper class
        * @param isConnected The status of the connection
        */
        public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected);
    }


    // -------------------------------------------------------------------------------------------- MAIN CLASS METHODS

    // This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private BluetoothHelperListener listener;

/**
* The messages delimiter.
* That character is the separator between every incoming (and sent) messages.<br>
* It is automatically appended to every string passed to SendMessage, and removed to every message received.
*/
    public char Delimiter = '\n';


/**
* Adds the specified BluetoothHelper listener to receive events from this class.
* Events occur when a message is received, or the connection status is changed. If it is null, no exception is thrown and no action is performed.<br>
* This is the preferred method to receive messages.
*
* Each message you'll receive will be notified with a:
* <pre>
*    public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, String message)
* </pre>
*
* Each time the status of the connection changes will be notified with a:
* <pre>
*    public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected)
* </pre>
* @param listener The BluetoothHelperListener
*/
    public void setBluetoothHelperListener(BluetoothHelperListener listener) {
        this.listener = listener;
    }


/**
* Returns the state of the connection.
* The method returns true only if all the communication streams are opened.<br>
* It return false also in case of connection in progress.
* @return The the connection state: true if the connection is opened, false otherwhise.
*/
    public boolean isConnected() {
        return (isInStreamConnected && isOutStreamConnected);
    }


/**
* Connects to the remote device (server) with the specified name.
* It bootstraps the connection to the paired (bonded) device named “DeviceName” if exists.<br>
* The function does return immediately, when the connection process is yet in progress, with no result.<br>
* You can receive a notification when the connection process is completed attaching a BluetoothHelperListener.
* As an alternative you can check the connection status with the isConnected() method described below.<br>
* An onBluetoothHelperConnectionStateChanged event occurs (if listener is attached) when the connection process terminates, returning the new status of the connection.
* In case of success, the class will be ready to communicate with the remote device.
* @param DeviceName The DeviceName of the remote Device
* @see android.bluetooth.BluetoothAdapter
* @see android.bluetooth.BluetoothAdapter#getBondedDevices()
* @see android.bluetooth.BluetoothDevice#getName()
*/
    public void Connect(String DeviceName) {
        Log.w("myApp", "[ # ] Connect(String DeviceName)");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();                                   // Find adapter
        if ((mBluetoothAdapter != null) && (!DeviceName.isEmpty())) {
            if (isConnected()) Disconnect(false);
            if (mBluetoothAdapter.isEnabled()) {                                                    // Adapter found
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();                // Collect all bonded devices
                for (BluetoothDevice bt : devices) {
                    if (DeviceName.equals(bt.getName())) {                                          // Find requested device name
                        Log.w("myApp", "[ # ] Devicename match found: " + bt.getName());
                        Connect(bt);
                    } //else Log.w("myApp", "[ # ] Devicename doesn't match: " + bt.getName());
                }
            }

        }
    }



    /**
     * Connects to the remote BluetoothDevice (server).
     * It bootstraps the connection to the paired (bonded) device BluetoothDevice if exists.<br>
     * The function does return immediately, when the connection process is yet in progress, with no result.<br>
     * You can receive a notification when the connection process is completed attaching a BluetoothHelperListener.
     * As an alternative you can check the connection status with the isConnected() method described below.<br>
     * An onBluetoothHelperConnectionStateChanged event occurs (if listener is attached) when the connection process terminates, returning the new status of the connection.
     * In case of success, the class will be ready to communicate with the remote device.
     * @param bluetoothDevice The remote BluetoothDevice
     * @see android.bluetooth.BluetoothAdapter
     * @see android.bluetooth.BluetoothAdapter#getBondedDevices()
     * @see android.bluetooth.BluetoothDevice
     */
    public void Connect(BluetoothDevice bluetoothDevice) {
        Log.w("myApp", "[ # ] Connect(BluetoothDevice bluetoothDevice)");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();                                       // Find adapter
        if ((mBluetoothAdapter != null) && (bluetoothDevice != null)) {
            if (isConnected()) Disconnect(false);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();                               // Find adapter
            if (mBluetoothAdapter.isEnabled()) {                                                    // Adapter found
                CT = new ConnectThread(bluetoothDevice);
                CT.start();
            }
        }
    }

/**
* Clear all pending incoming and outcoming messages.
* It clears the inputMessageQueue and then the outputMessageQueue, used by separate threads to perform communication.<br>
* Normally this method is called by class itself during the Disconnection process, and should not be called.<br>
* The method is public in case of particular user needs.
*/
    public void ClearBuffer() {
        if (inputMessagesQueue != null)  inputMessagesQueue.clear();                          // Clear the input message queue;
        if (outputMessagesQueue != null) outputMessagesQueue.clear();                         // Clear the output message queue;
    }


/**
* Disconnects from the connected remote device (server).
* The method closes the Streams, the Socket and terminates all the threads that manage the connection and the communication.<br>
* An onBluetoothHelperConnectionStateChanged event occurs (if listener is attached) when the disconnection process terminates, returning the new status of the connection.
*/
    public void Disconnect() {
        Disconnect(true);
    }


/**
* Disconnects from the connected remote device (server).
* The method closes the Streams, the Socket and terminates all the threads that manage the connection.<br>
* Then it clears all pending incoming and outcoming messages, if requested.<br>
* An onBluetoothHelperConnectionStateChanged event occurs (if listener is attached) when the disconnection process terminates, returning the new status of the connection.
* @param ClearBuffer If true, disconnects from the remote device clearing all pending incoming outcoming messages; otherwise, disconnects without touching the queues.
*/
    public void Disconnect(boolean ClearBuffer) {
        if (CT != null) {
            if (CT.isAlive()) CT.cancel();
        }
        if (ClearBuffer) ClearBuffer();
    }


/**
* Returns the oldest message received and buffered.
* The incoming messages are asynchronously stored in a LinkedBlockingQueue.
* With this method you can get the oldest received message that you have not yet read. That message is deleted from the queue.<br>
* Each time you call ReceiveMessage method you'll obtain the next unread message.<br>
*
* Please note that the preferred method to receive messages is attaching a listener, with the setBluetoothHelperListener method described below.<br>
* If the listener is attached, ReceiveMessage will ever returns an empty string, because each received message is sent directly to the attached listener.
* @see BluetoothHelper setBluetoothHelperListener()
* @return The String containing the message. An empty string otherwise
*/
    public String ReceiveMessage() {
        if (inputMessagesQueue != null) return "";

        String m = inputMessagesQueue.poll();
        return (m != null ? m : "");
    }


/**
* Send a message to the remote device.
* The message is stored in a LinkedBlockingQueue and asynchronously sent to the remote device by the dedicate thread.<br>
* The function returns true if the message is stored in the sending queue.
* @param msg The message to send
* @return true if the message is stored in the sending queue. false if a problem occurs
*/
    public boolean SendMessage(String msg) {
        if (isConnected() && (msg != null) && (outputMessagesQueue != null)) {
            return (outputMessagesQueue.offer(msg));
        } else return false;
    }
}
