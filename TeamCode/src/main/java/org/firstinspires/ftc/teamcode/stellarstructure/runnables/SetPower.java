package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;

public class SetPower extends Directive {
	private final StellarDcMotor motor;
	private final double power;

	public SetPower(@NonNull StellarDcMotor motor, double power) {
		this.motor = motor;
		this.power = power;
		setInterruptible(true);
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		motor.setPower(power);
	}

	@Override
	public void update() {}

	@Override
	public void stop(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return true;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Power %s to %f", motor.toString(), power);
	}
}