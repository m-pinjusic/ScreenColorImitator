#include <Adafruit_NeoPixel.h>
#define PIN 7 //data pin number
#define LEDS 30 //broj LED dioda
#define SECTIONS 10 //broj sekcija
#define LEDS_PER_SECTION 3 //LED po sekciji
#define DELAY 10
//konfiguracija
Adafruit_NeoPixel strip = Adafruit_NeoPixel(LEDS, PIN, NEO_GRB + NEO_KHZ800);
//polje boja
int r[SECTIONS];
int g[SECTIONS];
int b[SECTIONS];
void setup() {
  Serial.begin(9600);
  strip.begin();
  strip.setBrightness(128);
  strip.show();
}
void loop() {
  //čita podatke sa serijskog porta
  if(Serial.available() > 30) {
    if(Serial.read() == 0xff) {
      for(int i = 0; i<SECTIONS; i++) {
        r[i] = Serial.read();
        g[i] = Serial.read();
        b[i] = Serial.read();
      }
    }
  }
  //pali sljedeću sekciju
  for(int i=0; i<SECTIONS; i++) {
    strip.setPixelColor(i*LEDS_PER_SECTION, r[i], g[i], b[i]);
    strip.setPixelColor(i*LEDS_PER_SECTION+1, r[i], g[i], b[i]);
    strip.setPixelColor(i*LEDS_PER_SECTION+2, r[i], g[i], b[i]);
  }
  strip.show();
  delay(DELAY);
}
