package org.firstinspires.ftc.teamcode.util;

public class LaunchParameters {
    private double angle;
    private double velocity;

    public LaunchParameters(double angle, double velocity) {
        this.angle = angle;
        this.velocity = velocity;
    }

    // Optional: Add getter methods
    public double getAngle() {
        return angle;
    }

    public double getVelocity() {
        return velocity;
    }


}