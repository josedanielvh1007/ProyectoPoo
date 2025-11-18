#include "TapSensor.h"

TapSensor::TapSensor(uint8_t pin) : pin(pin) {}

void TapSensor::begin() {
    pinMode(pin, INPUT_PULLUP);
}

TapEvent TapSensor::update() {
    bool current = digitalRead(pin);

    // Press start
    if (current == LOW && lastState == HIGH) {
        pressStart = millis();
    }

    // Release
    if (current == HIGH && lastState == LOW) {
        unsigned long duration = millis() - pressStart;

        if (duration < 250)
            return SHORT_PRESS;
        else if (duration >= 250)
            return LONG_PRESS;
    }

    lastState = current;

    // Shake simulation (placeholder)
    if (random(0, 10000) == 500) {
        return SHAKE;
    }

    return NONE;
}
