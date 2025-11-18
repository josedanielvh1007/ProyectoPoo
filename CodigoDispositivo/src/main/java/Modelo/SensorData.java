/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jose
 */
// SensorData.java
public class SensorData {

    // Absolute orientation
    private float roll;
    private float pitch;
    private float yaw;

    // Raw acceleration
    private float ax;
    private float ay;
    private float az;

    // Filtered acceleration (optional)
    private float axFiltered;
    private float ayFiltered;
    private float azFiltered;

    // Estimated short-range displacement
    private float dx;
    private float dy;
    private float dz;

    // Timestamp from Arduino (in ms)
    private long timestamp;

    // Tap/shake info
    private boolean isPressed;
    private boolean isShaken;
    private String pressType;
    
    

    // Getters and setters...
    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }

    public float getAz() {
        return az;
    }

    public void setAz(float az) {
        this.az = az;
    }

    public float getAxFiltered() {
        return axFiltered;
    }

    public void setAxFiltered(float axFiltered) {
        this.axFiltered = axFiltered;
    }

    public float getAyFiltered() {
        return ayFiltered;
    }

    public void setAyFiltered(float ayFiltered) {
        this.ayFiltered = ayFiltered;
    }

    public float getAzFiltered() {
        return azFiltered;
    }

    public void setAzFiltered(float azFiltered) {
        this.azFiltered = azFiltered;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getDz() {
        return dz;
    }

    public void setDz(float dz) {
        this.dz = dz;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isShaken() {
        return isShaken;
    }

    public void setShaken(boolean isShaken) {
        this.isShaken = isShaken;
    }

    public String getPressType() {
        return pressType;
    }

    public void setPressType(String pressType) {
        this.pressType = pressType;
    }

}
