/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Vista;

import Modelo.*;

/**
 *
 * @author jose
 */
public class CodigoDispositivo {

    public static void main(String[] args) {
        SensorData sensorData = new SensorData();

        // Connect to local MQTT broker
        MqttReceiver receiver = new MqttReceiver("tcp://localhost:1883", "JavaClient", sensorData);

        // Example: read sensor data periodically
        new Thread(() -> {
            while (true) {
                System.out.printf("Orientation: Roll=%.2f Pitch=%.2f Yaw=%.2f%n",
                        sensorData.getRoll(),
                        sensorData.getPitch(),
                        sensorData.getYaw());
                System.out.printf("Tap: Pressed=%b Shaken=%b Type=%s%n",
                        sensorData.isPressed(),
                        sensorData.isShaken(),
                        sensorData.getPressType());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
