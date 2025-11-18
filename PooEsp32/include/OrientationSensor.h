#pragma once
#include <Arduino.h>
#include <MPU9250.h>
#include <MadgwickAHRS.h>

class OrientationSensor {
public:
    bool begin();
    void update();

    float getRoll();
    float getPitch();
    float getYaw();

private:
    MPU9250 mpu;
    Madgwick filter;

    float roll = 0;
    float pitch = 0;
    float yaw = 0;
};
