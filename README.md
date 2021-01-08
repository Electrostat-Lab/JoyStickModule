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


## Attachments : 

### Wrining PI for RPI4b & RPI3b :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/j8header-3b.png)

### JoyStick Module Mappings in Vx & Vy where Vx is the potential difference across the POTx(potentiometer X) & Vy is the potential difference across the POTy(potentiometer y) :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/Joystick-Module-Analog-Output.png)

### Arduino JoyStick Module Datasheet (NB: examples are in Arduinos) : 

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/joystick_module.pdf

### MCP3008 MicroChip ADC(Analog~Digital Converter) datasheet :

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/MCP3008.pdf




