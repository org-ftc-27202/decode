package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;


public final class Intake extends Subsystem {
	private StellarDcMotor intakeMotor;

	@Override
	public void init(HardwareMap hardwareMap) {
		intakeMotor = new StellarDcMotor(hardwareMap, "intake");
	}

	@Override
	public void update() {}

	public StellarDcMotor getIntakeMotor() {return intakeMotor;}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("Intake Speed: %f, Intake Power: %f", intakeMotor.getVelocity(), intakeMotor.getPower());
	}
}