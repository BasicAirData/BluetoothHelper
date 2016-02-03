/*
  BluetoothHelper_example.ino - Arduino Sketch for testing
  Bluetooth communication with Android devices using BluetoothHelper java class
  with the example android app.

  Created by G.Capelli (BasicAirData) on 02/02/16.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
*/


#define INPUT_SIZE 1024
#define DELIMITER '\n'      // Message delimiter. It must match with Android class one;

const int BTTX_pin = 0;     // TX-O pin of bluetooth mate, Arduino D2
const int BTRX_pin = 1;     // RX-I pin of bluetooth mate, Arduino D3

const int LEDBTN_pin = 8;   // Digital output for LED (button)
const int LEDPOT_pin = 9;   // Digital output for LED (potentiometer)

int buttonValue = 0;        // Digital LED value
int potValue = 0;           // PWM LED value
int linearpotValue = 0;
int lightValue = 0;         // Analog sensor value

char input[INPUT_SIZE + 1];
char *ch;
bool endmsg = false;


void setup()
{
  pinMode(LEDBTN_pin, OUTPUT);
  pinMode(LEDPOT_pin, OUTPUT);

  Serial.begin(9600);                         // Begin the serial monitor at 9600bps
  
  ch = &input[0];
}


void loop()
{
  delay(2);

  if (Serial.available()) // If the bluetooth has received any character
  {
    while (Serial.available() && (!endmsg)) { // until (end of buffer) or (newline)
      *ch = Serial.read();                    // read char from serial
      if (*ch == DELIMITER) {
        endmsg = true;                        // found DELIMITER
        *ch == 0;
      }
      else ++ch;                              // increment index
    }

    if ((endmsg) && (ch != &input[0]))        // end of (non empty) message !!!
    {
      char *command = strtok(input, ",");

      if (!strcmp(command, "$PWM"))           // Set PWM led
      {
        command = strtok (NULL, ",");
        potValue = atoi(command);
        Serial.write("$PWM,");
        Serial.print(potValue);
        Serial.write(DELIMITER);
        // Linearization of light intensity related with PWM value:
                       //pow(out_max, in/in_max)
        linearpotValue = pow(256, (double)potValue / 255) - 1;
        analogWrite(LEDPOT_pin, linearpotValue);
      }
      else if (!strcmp(command, "$LED"))      // Set ON/OFF Led
      {
        command = strtok (NULL, ",");
        buttonValue = atoi(command);
        if (buttonValue == 1) Serial.print("$LED,1");
        else Serial.print("$LED,0");
        Serial.write(DELIMITER);

        if (buttonValue == HIGH) digitalWrite(LEDBTN_pin, HIGH);
        else digitalWrite(LEDBTN_pin, LOW);
      }
    }
    if (endmsg) {
      endmsg = false;
      *ch = 0;
      ch = &input[0];                         // Return to first index, ready for the new message;
    }
  }
}

