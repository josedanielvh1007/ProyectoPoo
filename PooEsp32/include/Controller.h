#pragma once
#include <Arduino.h>
#include "OrientationSensor.h"
#include "TapSensor.h"
#include "MqttClientManager.h"

class Controller {
public:
    Controller();
    void begin();
    void loop();

private:
    OrientationSensor orientation;
    TapSensor tap;
    MqttClientManager mqtt;
};
