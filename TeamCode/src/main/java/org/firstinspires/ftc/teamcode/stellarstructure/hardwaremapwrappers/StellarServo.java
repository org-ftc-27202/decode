package org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StellarServo {
	private final Servo servo;
	private final String servoName;

	public StellarServo(HardwareMap hardwareMap, @NonNull String servoName) {
		this.servo = hardwareMap.get(Servo.class, servoName);
		this.servoName = servoName;
	}
	public void setPosition(double position) {
		servo.setPosition(position);
	}
	public double getPosition() {
		return this.servo.getPosition();
	}
	public void setDirection(Servo.Direction direction) {
		servo.setDirection(direction);
	}
	public Servo.Direction getDirection() {
		return servo.getDirection();
	}
	public void scaleRange(double min, double max) {
		servo.scaleRange(min, max);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("StellarServo: %s", servoName);
	}
}