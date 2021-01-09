# JoyStickModule

[![](https://jitpack.io/v/Scrappers-glitch/JoystickModule.svg)](https://jitpack.io/#Scrappers-glitch/JoystickModule)

## In order to use the Library : ##

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```gradle
dependencies {
	        implementation 'com.github.Scrappers-glitch:JoystickModule:1.0.1R'
	}
```

## Description :
Since , RPIs got no analog I/O pins , you cannot get direct analog mappings using the PI , so in order to read analog mappings ```0~1023``` as a hardware mapping or ```0%~100%``` as a pulse duty cycle , you will need to convert analog outputs to digital inputs through SPI (Serial peripheral Interface) of the PI

In order to find the wiringPI pins(wPi column on right & Left for RT & LT pins) on your device :

```
pi@raspberrypi:~ $ gpio readall
 +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
 |   2 |   8 |   SDA.1 |   IN | 1 |  3 || 4  |   |      | 5v      |     |     |
 |   3 |   9 |   SCL.1 |   IN | 1 |  5 || 6  |   |      | 0v      |     |     |
 |   4 |   7 | GPIO. 7 |   IN | 1 |  7 || 8  | 1 | IN   | TxD     | 15  | 14  |
 |     |     |      0v |      |   |  9 || 10 | 1 | IN   | RxD     | 16  | 15  |
 |  17 |   0 | GPIO. 0 |  OUT | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
 |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
 |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
 |     |     |    3.3v |      |   | 17 || 18 | 0 | IN   | GPIO. 5 | 5   | 24  |
 |  10 |  12 |    MOSI |   IN | 0 | 19 || 20 |   |      | 0v      |     |     |
 |   9 |  13 |    MISO |   IN | 0 | 21 || 22 | 0 | IN   | GPIO. 6 | 6   | 25  |
 |  11 |  14 |    SCLK |   IN | 0 | 23 || 24 | 1 | IN   | CE0     | 10  | 8   |
 |     |     |      0v |      |   | 25 || 26 | 1 | IN   | CE1     | 11  | 7   |
 |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
 |   5 |  21 | GPIO.21 |   IN | 1 | 29 || 30 |   |      | 0v      |     |     |
 |   6 |  22 | GPIO.22 |   IN | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
 |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
 |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
 |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | IN   | GPIO.28 | 28  | 20  |
 |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
```


## Attachments : 

### Wrining PI for RPI4b & RPI3b :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/j8header-3b.png)

### JoyStick Module Mappings in Vx & Vy where Vx is the potential difference across the POTx(potentiometer X) & Vy is the potential difference across the POTy(potentiometer y) :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/Joystick-Module-Analog-Output.png)

### Arduino JoyStick Module Datasheet (NB: examples are in Arduinos) : 

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/joystick_module.pdf

### MCP3008 Channel & Communication Pins :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/mcp3008pins.png)

### MCP3008 MicroChip ADC(Analog~Digital Converter) datasheet :

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/MCP3008.pdf

### If you need to know more about MCP3008 at MicroChip : 

https://www.microchip.com/wwwproducts/en/en010530#additional-features

### A photo for the Breadboard circuit :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/IMG_20210109_145230.jpg)

