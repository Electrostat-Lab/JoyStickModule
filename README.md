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

```bash
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

### So , Generally do like this : 

```bash
VDD(driving device) -> 3v3 source.
Vref(reference voltage) -> 3v3 source.
AGND(Analog ground for closed circuit during analogRead transmission) -> GND.
CLK -> SCLK pin of the SPI0 , GPIO14 or physical pin 23 of PI4.
DOUT(the converted digital out of the MCP3008) -> MISO0(Master-in slave-out) of the SPI0 , GPIO13 or physical pin 21 of PI4.
DIN(Digital input from the PI4, for MCP3008 channels configuration as PI4 digitalOut) -> MOSI0(Master-out slave-in) of the SPI0 , GPIO12 or physical pin 19.
CS/SHDN(Chip Select or Chip Enable : used to select which SPI to enable as PI4b has 4 SPIs) -> CE0(Chip enable 0) , GPIO10 or physical pin 24 to enable SPI0.
DGND(Digital ground for closing circuit during digitalOut transmission) -> GND.

```

### Then on the joystick module there are 5 pins : 

```bash
GND -> GND 
+5v -> 5v0 or 3v3 source (it's tested in both cases)
VRx -> CH0 or CH1 or CH2 or CH3 or CH4 or CH5 or CH6 or CH7 according to your code setup but it should differs from VRy
VRy -> CH0 or CH1 or CH2 or CH3 or CH4 or CH5 or CH6 or CH7 according to your code setup but it should differs from VRx
SW(switch button pin) -> GPIO7 or pin7 for digital input ( i think you can still use MCP3008 channels as digitalInputs too with provisionDigitalInput(Pin) code setup , try it & tell e if it works :-) ). 

```

### A photo for the Breadboard circuit :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/IMG_20210109_145230.jpg)

### Video of Operation :

https://youtu.be/9ZvhFQSwHF0

-----------------------------------------------------------------------------------------------------------

### More at : 
- Pi4j Examples : https://github.com/Scrappers-glitch/pi4j-v1/tree/master/pi4j-example/src/main/java
- wPi Examples : https://github.com/WiringPi/WiringPi/tree/master/examples
- Raspberry Pi : https://www.raspberrypi.com/documentation/computers/raspberry-pi.html
- JMonkeyEngine : https://jmonkeyengine.org/docs/
- Test `JoyStickModule` lib : https://github.com/Scrappers-glitch/JmeCarPhysicsTestRPI
- SPI (Serial Peripheral Interface) : https://learn.sparkfun.com/tutorials/serial-peripheral-interface-spi/all
 				      https://en.wikipedia.org/wiki/Serial_Peripheral_Interface
- MCP3008 ADC : https://www.microchip.com/en-us/product/MCP3008

### Empric References :
- General electronics : https://www.electronics-tutorials.ws/combination/analogue-to-digital-converter.html
- RPI intoduced by oracle press thro pi4j1.4 : https://eg1lib.org/book/5173454/11911d
- Pi4J website and docs : https://pi4j.com/1.4/example/control.html 
