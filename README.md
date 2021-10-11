# ScreenColorImitator
Arduino powered LED strip that instantly imitates colors on PC screen(sections of screen).

The Arduino actually serves as an intermediary that receives information from a Java application and forwards it to the LED strip, more precisely it sends the information to each microcontroller separately (on the LED strip).

The main part of the project (apart from the code of course) is actually an addressable LED strip that serves for lighting. The LED strip is one meter long and consists of 30 LEDs. Each LED can be lit individually using small microcontrollers located on each LED, which can be seen in the image below.

![](photos/IMG_20190527_145617.jpg)

The LED strip is powered via 5V and consists of 3 pins or wires, red represents plus (+), white minus (-) and green (DATA) which receives data from a source such as an Arduino microcontroller. The LED strips are normally powered via 12V, but this LED strip seems ideal because it can be connected directly to the Arduino Uno for a 5V power supply. However, it is not that simple. Each color on one diode needs 20mA to be able to work at the strongest light. The LED has 3 colors, red, blue and green. Which means one diode will take 60mA. 30 diodes x 60mA = 1800mA. The calculation shows that the entire LED strip will take 1800mA, ie 1.8A, in the highest possible light, which the Arduino Uno cannot handle. The Arduino Uno has an output of 400mA 5V. It is concluded that an external power supply is needed for the LED strip to work. The 9W power supply (5V x 1.8A = 9W) is a good choice. 

But that’s not the end of the story of merging the Arduino Uno and the LED strip. A capacitor with a capacity between 100uF and 1000uF has to be connected from the plus power supply to the minus Arduino to smooth the power supply. A 470 Ohm resistor also has to be added to reduce unwanted interference in the electrical signal (noise) on the line between the Arduino digital output pin and the green data input pin. Only after these steps LED strip be connected to the Arduino Uno via a digital pin and via GND. Image below shows the connected components.

![](gifs/preview1.gif)
![](gifs/preview2.gif)
![](gifs/preview3.gif)
