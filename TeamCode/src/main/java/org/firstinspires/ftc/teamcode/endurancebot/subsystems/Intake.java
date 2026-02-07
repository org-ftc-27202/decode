package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;

import dev.nextftc.control.ControlSystem;


public final class Intake extends Subsystem {
	private StellarDcMotor intakeMotor;

	private double intakeSpeed = 0.0;

	private ControlSystem controller;

	private double intakePower;

	@Override
	public void init(HardwareMap hardwareMap) {
		intakeMotor = new StellarDcMotor(hardwareMap, "intake");
		controller = ControlSystem.builder()
				.posPid(0.01, 0.0, 0.0)
				.build();
	}

	@Override
	public void update() {}

	public StellarDcMotor getIntakeMotor() {return intakeMotor;}

	public void setIntakeSpeed(double intakeSpeed) {
		this.intakeSpeed = intakeSpeed;
	}

	public void setMotorSpeed() {
		intakeMotor.setPower(intakeSpeed);
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("Intake Speed: %f, Intake Power: %f", intakeMotor.getVelocity(), intakeSpeed);
	}
}