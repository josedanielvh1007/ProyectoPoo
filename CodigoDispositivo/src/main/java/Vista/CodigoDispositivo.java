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
        MqttReceiver receiver = new MqttReceiver(
                "tcp://192.168.43.155:1883", // broker IP:port
                "LaptopClient",
                sensorData
        );

        while (true) {
            // Orientation
            System.out.println("Roll: " + sensorData.getRoll());
            System.out.println("Pitch: " + sensorData.getPitch());
            System.out.println("Yaw: " + sensorData.getYaw());

            // Tap events
            if (sensorData.isPressed()) {
                System.out.println("Pressed: " + sensorData.getPressType());
            }
            if (sensorData.isShaken()) {
                System.out.println("Shaken!");
            }

            // Small delay so output is readable
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
