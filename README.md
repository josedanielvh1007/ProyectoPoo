# Sensor Data Streaming with ESP32 and Java MQTT Client

## Overview

This project implements a local data streaming system using an **ESP32** microcontroller and a **Java application** running on a laptop. The system reads orientation and tap sensor data from the ESP32 and sends it to the laptop using the **MQTT protocol**, allowing real-time processing and response to sensor events.

This approach simulates an **API-like interaction**, but fully **locally**, without needing internet access. It is lightweight, fast, and ideal for projects requiring local device control.

---

## System Components

### 1. ESP32 Controller Code

- Reads **orientation** (roll, pitch, yaw) and **tap events** from onboard sensors.
- Converts the sensor readings into **JSON format**.
- Publishes sensor data to **MQTT topics**:
  - Orientation: `esp32/orientation`
  - Tap events: `esp32/tap`
- Uses an MQTT client library to connect to the laptop acting as the broker.
- Runs a `loop()` that continuously updates sensor readings and sends them to the broker.

### 2. Java MQTT Receiver

- Runs on the laptop and connects to the MQTT broker (Mosquitto).
- Subscribes to the ESP32 topics (`esp32/orientation` and `esp32/tap`).
- Parses the incoming JSON data using **Gson**.
- Updates a shared **SensorData** object representing the current state of the ESP32 sensors.
- Can be used in a local application to trigger responses based on sensor movements or taps.

### 3. Mosquitto MQTT Broker

- Runs locally on the laptop.
- Handles message routing between the ESP32 and the Java application.
- Ensures **real-time, lightweight communication** without internet dependency.
- Configured to listen on **port 1883** and accept connections from devices in the same network.

---

## Data Flow

1. **ESP32 reads sensors** → JSON data created → **published to MQTT broker**.  
2. **Mosquitto broker receives messages** → forwards them to all subscribers.  
3. **Java application receives messages** → updates `SensorData` object → application reacts accordingly.

[ESP32 Sensors] --JSON--> [MQTT Broker on Laptop] --JSON--> [Java MQTT Receiver]


---

## Advantages of This Implementation

- Works locally, like a **local API**: fast, responsive, and independent of internet connection.  
- Uses **MQTT**, a lightweight and reliable messaging protocol for IoT devices.  
- Flexible: multiple devices can subscribe to the same topics if needed.  
- Easy to integrate into other local applications or visualization tools.  

---

## Setup Instructions

### 1. Laptop

1. Install Mosquitto:
   ```bash
   sudo apt update
   sudo apt install mosquitto mosquitto-clients -y
   sudo systemctl enable mosquitto
   sudo systemctl start mosquitto
   ```
2. Ensure Mosquitto is running:
    ```bash
    sudo systemctl status mosquitto
    sudo lsof -i :1883
    ```
3. Update ```MqttReceiver.java``` with the broker IP (laptop IP on your network).

### 2. ESP32
1. Flash the ESP32 code.
2. Configure the MQTT client with the laptop IP and port 1883.
3. Ensure ESP32 connects to the same Wi-Fi network as the laptop
