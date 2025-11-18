#pragma once
#include <Arduino.h>

enum TapEvent {
    NONE,
    SHORT_PRESS,
    LONG_PRESS,
    SHAKE
};

class TapSensor {
public:
    TapSensor(uint8_t pin);
    void begin();
    TapEvent update();

private:
    uint8_t pin;
    unsigned long pressStart = 0;
    bool lastState = HIGH;
};
