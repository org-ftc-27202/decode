package org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StellarLight {
    private final Servo light;
    private final String lightName;

    public StellarLight(HardwareMap hardwareMap, @NonNull String lightName) {
        this.light = hardwareMap.get(Servo.class, lightName);
        this.lightName = lightName;
    }
    public void setPosition(double position) {
        light.setPosition(position);
    }
    public double getPosition() {
        return this.light.getPosition();
    }
    public void setDirection(Servo.Direction direction) {
        light.setDirection(direction);
    }
    public Servo.Direction getDirection() {
        return light.getDirection();
    }
    public void scaleRange(double min, double max) {
        light.scaleRange(min, max);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("StellarLight: %s", lightName);
    }
}
