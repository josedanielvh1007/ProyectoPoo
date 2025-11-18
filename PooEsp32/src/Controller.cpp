#include "Controller.h"

Controller::Controller()
    : tap(27),                        // Modify pin as needed
      mqtt("192.168.1.100", 1883)     // MQTT broker IP
{}

void Controller::begin() {
    orientation.begin();
    tap.begin();
    mqtt.begin("YOUR_SSID", "YOUR_PASSWORD");
}

void Controller::loop() {
    mqtt.loop();

    orientation.update();
    TapEvent event = tap.update();

    // Publish orientation
    String json = "{";
    json += "\"roll\":"  + String(orientation.getRoll())  + ",";
    json += "\"pitch\":" + String(orientation.getPitch()) + ",";
    json += "\"yaw\":"   + String(orientation.getYaw());
    json += "}";

    mqtt.publish("esp32/orientation", json);

    // Publish tap events
    if (event != NONE) {
        mqtt.publish("esp32/tap", String(event));
    }
}
