# ScreenColorImitator
Arduino powered LED strip that instantly imitates colors on PC screen(sections of screen).

The Arduino actually serves as an intermediary that receives information from a Java application and forwards it to the LED strip, more precisely it sends the information to each microcontroller separately (on the LED strip).

The main part of the project (apart from the code of course) is actually an addressable LED strip that serves for lighting. The LED strip is one meter long and consists of 30 LEDs. Each LED can be lit individually using small microcontrollers located on each LED, which can be seen in the image below.

![](gifs/preview1.gif)
![](gifs/preview2.gif)
![](gifs/preview3.gif)
