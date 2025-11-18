package Modelo;

import Controlador.MotionProcessor;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;

public class MqttReceiver {

    private MqttClient client;
    private final Gson gson = new Gson();

    private final SensorData sensorData;
    private final MotionProcessor motionProcessor;

    public MqttReceiver(String brokerUrl, String clientId, SensorData sensorData) {
        this.sensorData = sensorData;
        this.motionProcessor = new MotionProcessor();   // NEW: motion processor

        try {
            client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            System.out.println("Connected to MQTT broker: " + brokerUrl);

            subscribeTopics();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------
    // SUBSCRIPTIONS
    // ------------------------------------------------------------
    private void subscribeTopics() throws MqttException {

        // ORIENTATION (roll, pitch, yaw)
        client.subscribe("esp32/sensor/orientation", (topic, msg) -> {
            String payload = new String(msg.getPayload());

            SensorData incoming = gson.fromJson(payload, SensorData.class);

            // Update ONLY orientation
            sensorData.setRoll(incoming.getRoll());
            sensorData.setPitch(incoming.getPitch());
            sensorData.setYaw(incoming.getYaw());
            sensorData.setTimestamp(incoming.getTimestamp());

            // Not processed by MotionProcessor
            System.out.println("Orientation received: R=" + sensorData.getRoll());
        });

        // RAW ACCELERATION (gravity compensated ideally)
        client.subscribe("esp32/sensor/accel", (topic, msg) -> {
            String payload = new String(msg.getPayload());

            SensorData incoming = gson.fromJson(payload, SensorData.class);

            // Update accelerations
            sensorData.setAxFiltered(incoming.getAxFiltered());
            sensorData.setAyFiltered(incoming.getAyFiltered());
            sensorData.setAzFiltered(incoming.getAzFiltered());
            sensorData.setTimestamp(incoming.getTimestamp());

            // Call the motion processor (accel → velocity → position)
            motionProcessor.process(sensorData);

            System.out.println("Accel processed | pos: "
                    + sensorData.getDx() + ", "
                    + sensorData.getDy() + ", "
                    + sensorData.getDz());
        });

        // TAP SENSOR
        client.subscribe("esp32/sensor/tap", (topic, msg) -> {
            String payload = new String(msg.getPayload());

            SensorData incoming = gson.fromJson(payload, SensorData.class);

            sensorData.setPressed(incoming.isPressed());
            sensorData.setShaken(incoming.isShaken());
            sensorData.setPressType(incoming.getPressType());

            System.out.println("Tap event: " + sensorData.getPressType());
        });

        System.out.println("Subscribed to ESP32 topics");
    }
}
