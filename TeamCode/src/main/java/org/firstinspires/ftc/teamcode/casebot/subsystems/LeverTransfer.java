package org.firstinspires.ftc.teamcode.casebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class LeverTransfer extends Subsystem {

	private StellarServo leverTransferServo;

	public final static double LEVER_DOWN_POSITION = 0.00;
	public final static double LEVER_UP_POSITION = 0.41;

	@Override
	public void init(HardwareMap hardwareMap) {
		leverTransferServo = new StellarServo(hardwareMap, "leverTransferServo");

		//todo
		leverTransferServo.setPosition(LEVER_DOWN_POSITION);
	}

	@Override
	public void update() {}

	public StellarServo getLeverTransferServo() {
		return this.leverTransferServo;
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("Lever Target Position: %f", leverTransferServo.getPosition());
	}
}