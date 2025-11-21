/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.SensorData;

/**
 *
 * @author jose
*/
public class MotionProcessor {

    // velocities
    private float vx, vy, vz;

    // previous timestamp
    private long lastTime = -1;

    public void process(SensorData data) {

        long timestamp = data.getTimestamp();
        if (lastTime < 0) {
            lastTime = timestamp;
            return;
        }

        float dt = (timestamp - lastTime) / 1000.0f; // seconds
        lastTime = timestamp;

        // 1. get acceleration (already gravity-compensated ideally)
        float ax = data.getAxFiltered();
        float ay = data.getAyFiltered();
        float az = data.getAzFiltered();

        // 2. check if IMU is nearly still (important!)
        if (Math.abs(ax) + Math.abs(ay) + Math.abs(az) < 0.2f) {
            vx = vy = vz = 0; // RESET DRIFT
            return;
        }

        // 3. integrate acceleration → velocity
        vx += ax * dt;
        vy += ay * dt;
        vz += az * dt;

        // 4. integrate velocity → displacement
        float dx = vx * dt;
        float dy = vy * dt;
        float dz = vz * dt;

        // store it in SensorData
        data.setDx(data.getDx() + dx);
        data.setDy(data.getDy() + dy);
        data.setDz(data.getDz() + dz);
    }

}
