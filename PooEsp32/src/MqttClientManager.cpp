#include "MqttClientManager.h"

MqttClientManager::MqttClientManager(const char* server, int port)
    : client(wifiClient), mqttServer(server), mqttPort(port) {}

void MqttClientManager::begin(const char* ssid, const char* password) {
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
    }

    client.setServer(mqttServer, mqttPort);
}

void MqttClientManager::reconnect() {
    while (!client.connected()) {
        if (client.connect("esp32_client")) {
            break;
        }
        delay(500);
    }
}

void MqttClientManager::loop() {
    if (!client.connected()) reconnect();
    client.loop();
}

void MqttClientManager::publish(const char* topic, const String& msg) {
    client.publish(topic, msg.c_str());
}
