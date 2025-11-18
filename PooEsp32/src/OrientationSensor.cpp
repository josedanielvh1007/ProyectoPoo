#include "OrientationSensor.h"
#include <Wire.h>

bool OrientationSensor::begin() {
    Wire.begin();

    if (!mpu.setup(0x68)) {
        return false;
    }

    filter.begin(100); // 100 Hz
    return true;
}

void OrientationSensor::update() {
    if (!mpu.update()) return;

    filter.updateIMU(
        mpu.getGyroX(), mpu.getGyroY(), mpu.getGyroZ(),
        mpu.getAccX(),  mpu.getAccY(),  mpu.getAccZ()
    );

    roll  = filter.getRoll();
    pitch = filter.getPitch();
    yaw   = filter.getYaw();
}

float OrientationSensor::getRoll() { return roll; }
float OrientationSensor::getPitch() { return pitch; }
float OrientationSensor::getYaw() { return yaw; }
