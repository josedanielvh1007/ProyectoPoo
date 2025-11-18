#pragma once
#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>

class MqttClientManager {
public:
    MqttClientManager(const char* server, int port);
    void begin(const char* ssid, const char* password);
    void loop();

    void publish(const char* topic, const String& msg);

private:
    WiFiClient wifiClient;
    PubSubClient client;

    const char* mqttServer;
    int mqttPort;

    void reconnect();
};
