#include <Arduino.h>
#include "Controller.h"

Controller controller;

void setup() {
    Serial.begin(115200);
    controller.begin();
}

void loop() {
    controller.loop();
    delay(10); // 100Hz loop
}
