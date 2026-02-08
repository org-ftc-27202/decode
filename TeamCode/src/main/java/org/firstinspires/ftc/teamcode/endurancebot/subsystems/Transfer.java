package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class Transfer extends Subsystem {

	public StellarServo getTransferRed() {
		return transferRed;
	}

	public StellarServo getTransferBlue() {
		return transferBlue;
	}

	public void setTransferPower(double power) {
		transferRed.setPosition(power);
		transferBlue.setPosition(power);
	}
	private StellarServo transferRed, transferBlue;


	@Override
	public void init(HardwareMap hardwareMap) {
		transferRed = new StellarServo(hardwareMap, "transfer_red");
		transferBlue = new StellarServo(hardwareMap, "transfer_blue");

		transferRed.setDirection(Servo.Direction.FORWARD);
		transferBlue.setDirection(Servo.Direction.REVERSE);
	}

	@Override
	public void update() {}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("Transfer Red Power: %f, Transfer Blue Power: %f", transferRed.getPosition(), transferBlue.getPosition());
	}
}