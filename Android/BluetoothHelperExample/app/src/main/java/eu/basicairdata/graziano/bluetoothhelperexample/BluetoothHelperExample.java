package eu.basicairdata.graziano.bluetoothhelperexample;

/**
 * BluetoothHelper Java Helper Class for Android - Example app
 * Created by G.Capelli (BasicAirData) on 06/02/16.
 * v.1.0.2
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
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class BluetoothHelperExample extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothHelper mBluetooth = new BluetoothHelper();
    private SeekBar mSeekBar;
    private TextView mTextViewStatus;
    boolean led = false;                                // The status of the ON/OFF led
    private String DEVICE_NAME = "RNBT-729D";           // The name of the remote device (BlueSMIRF Gold)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //force portrait screen
        setContentView(R.layout.activity_bluetooth_helper_example);

        // Link objects to UI
        mTextViewStatus = (TextView) findViewById(R.id.ID_STATUSTEXT);
        mSeekBar = (SeekBar) findViewById(R.id.ID_SEEKBAR);

        // Check if Bluetooth is supported by the device
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            finish();
        }

        // Start Bluetooth connection with the paired "RNBT-729D" device (BlueSMIRF Gold)
        mTextViewStatus.setText("Connecting to " + DEVICE_NAME);
        mBluetooth.Connect(DEVICE_NAME);

        // Setup listener for SeekBar:
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mBluetooth.isConnected()) {
                    // Write the new value to Bluetooth (The String is something like "$PWM,128")
                    mBluetooth.SendMessage("$PWM," + seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used in this demo app
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBluetooth.isConnected()) {
                    // Write the new value to Bluetooth
                    mBluetooth.SendMessage("$PWM," + seekBar.getProgress());
                }
            }
        });

        // Setup listener for Bluetooth helper;
        mBluetooth.setBluetoothHelperListener(new BluetoothHelper.BluetoothHelperListener() {
            @Override
            public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, String message) {
                // Do your stuff with the message received !!!
            }

            @Override
            public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected) {
                if (isConnected) {
                    mTextViewStatus.setText("Connected");
                } else {
                    mTextViewStatus.setText("Disconnected");
                    // Auto reconnect!
                    mBluetooth.Connect(DEVICE_NAME);
                }
            }
        });
    }

    // The event fired when you click the button
    public void onButtonClick(View view) {
        if (mBluetooth.isConnected()) {
            //Switch the value of the led
            mBluetooth.SendMessage(led ? "$LED,0" : "$LED,1");
            led = !led;
        }
    }
}