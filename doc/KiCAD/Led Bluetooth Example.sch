EESchema Schematic File Version 2
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:special
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:BlueSMIRF
LIBS:valves
LIBS:arduino_shieldsNCL
LIBS:Led Bluetooth Example-cache
EELAYER 25 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title "Electrical Scheme for Testing Example"
Date "dom 31 gen 2016"
Rev "1.0"
Comp "BasicAirData"
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L BlueSMiRF_Gold Modem
U 1 1 56AE5F1A
P 7500 3950
F 0 "Modem" H 7500 4350 60  0000 C CNN
F 1 "BlueSMiRF Gold" H 7500 3550 60  0000 C CNN
F 2 "" H 7500 3550 60  0000 C CNN
F 3 "" H 7500 3550 60  0000 C CNN
	1    7500 3950
	1    0    0    1   
$EndComp
$Comp
L R R2
U 1 1 56AE611A
P 7850 3000
F 0 "R2" V 7930 3000 50  0000 C CNN
F 1 "1K" V 7850 3000 50  0000 C CNN
F 2 "" V 7780 3000 30  0000 C CNN
F 3 "" H 7850 3000 30  0000 C CNN
	1    7850 3000
	0    -1   -1   0   
$EndComp
$Comp
L LED ON/OFF
U 1 1 56AE63A1
P 7100 3000
F 0 "ON/OFF" H 7100 3100 50  0000 C CNN
F 1 "LED" H 7100 2900 50  0000 C CNN
F 2 "" H 7100 3000 60  0000 C CNN
F 3 "" H 7100 3000 60  0000 C CNN
	1    7100 3000
	1    0    0    -1  
$EndComp
$Comp
L ARDUINO_SHIELD Arduino
U 1 1 56AE683E
P 4900 3100
F 0 "Arduino" H 4550 4050 60  0000 C CNN
F 1 "ARDUINO_SHIELD" H 4950 2150 60  0000 C CNN
F 2 "" H 4900 3100 60  0000 C CNN
F 3 "" H 4900 3100 60  0000 C CNN
	1    4900 3100
	1    0    0    -1  
$EndComp
$Comp
L +5V #PWR?
U 1 1 56AE6CA9
P 3000 5000
F 0 "#PWR?" H 3000 4850 50  0001 C CNN
F 1 "+5V" H 3000 5140 50  0000 C CNN
F 2 "" H 3000 5000 60  0000 C CNN
F 3 "" H 3000 5000 60  0000 C CNN
	1    3000 5000
	0    -1   -1   0   
$EndComp
$Comp
L GND #PWR?
U 1 1 56AE6CC1
P 3000 5250
F 0 "#PWR?" H 3000 5000 50  0001 C CNN
F 1 "GND" H 3000 5100 50  0000 C CNN
F 2 "" H 3000 5250 60  0000 C CNN
F 3 "" H 3000 5250 60  0000 C CNN
	1    3000 5250
	0    1    1    0   
$EndComp
Wire Wire Line
	8500 2550 8500 3000
Wire Wire Line
	8500 3000 8500 5250
Wire Wire Line
	8500 3000 8000 3000
Connection ~ 6200 5250
Wire Wire Line
	5850 3000 6900 3000
Wire Wire Line
	7300 3000 7700 3000
Connection ~ 3400 5250
Wire Wire Line
	6200 4000 6800 4000
Wire Wire Line
	6200 5250 6200 4000
Connection ~ 3300 5000
Wire Wire Line
	6100 4100 6800 4100
Wire Wire Line
	6100 5000 6100 4100
Wire Wire Line
	3400 3000 3950 3000
Wire Wire Line
	3400 5250 3400 3000
Wire Wire Line
	8500 5250 6200 5250
Wire Wire Line
	6200 5250 3400 5250
Wire Wire Line
	3400 5250 3000 5250
Wire Wire Line
	3300 2900 3950 2900
Wire Wire Line
	3300 5000 3300 2900
Wire Wire Line
	3000 5000 3300 5000
Wire Wire Line
	3300 5000 6100 5000
Wire Wire Line
	6400 4200 6800 4200
Wire Wire Line
	6400 3700 6400 4200
Wire Wire Line
	6800 3700 6400 3700
Wire Wire Line
	5850 3800 6800 3800
Wire Wire Line
	5850 3900 6800 3900
$Comp
L LED PWM
U 1 1 56AE9004
P 7100 2550
F 0 "PWM" H 7100 2650 50  0000 C CNN
F 1 "LED" H 7100 2450 50  0000 C CNN
F 2 "" H 7100 2550 60  0000 C CNN
F 3 "" H 7100 2550 60  0000 C CNN
	1    7100 2550
	1    0    0    -1  
$EndComp
$Comp
L R R1
U 1 1 56AE904C
P 7850 2550
F 0 "R1" V 7930 2550 50  0000 C CNN
F 1 "1K" V 7850 2550 50  0000 C CNN
F 2 "" V 7780 2550 30  0000 C CNN
F 3 "" H 7850 2550 30  0000 C CNN
	1    7850 2550
	0    -1   -1   0   
$EndComp
Wire Wire Line
	5850 2900 6500 2900
Wire Wire Line
	6500 2900 6500 2550
Wire Wire Line
	6500 2550 6900 2550
Wire Wire Line
	7300 2550 7700 2550
Wire Wire Line
	8000 2550 8500 2550
Connection ~ 8500 3000
$EndSCHEMATC
