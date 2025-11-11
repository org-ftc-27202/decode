package org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StellarLight {
    private final Servo light;
    public StellarLight(HardwareMap hardwareMap, String lightName) {light = hardwareMap.get(Servo.class, lightName);}
    public void setPosition(double position) {light.setPosition(position);}
    public double getPosition() {return this.light.getPosition();}
    public void setDirection(Servo.Direction direction) {light.setDirection(direction);}
    public Servo.Direction getDirection() {return light.getDirection();}
    public void scaleRange(double min, double max) {light.scaleRange(min, max);}
}
