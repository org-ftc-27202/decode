package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;

public class SetVelocity extends Directive {
	private final StellarDcMotor motor;
	private final double velocity;
	private final double tolerance;

	public SetVelocity(@NonNull StellarDcMotor motor, double velocity, double tolerance) {
		this.motor = motor;
		this.velocity = velocity;
		this.tolerance = tolerance;
		setInterruptible(true);
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		motor.setTargetVelocity(velocity);
	}

	@Override
	protected void onUpdate() {}

	@Override
	protected void onStop(boolean interrupted) {}

	@Override
	protected boolean isFinished() {
		return Math.abs(motor.getVelocity() - velocity) < tolerance;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Velocity: %s to %f", motor.toString(), velocity);
	}
}