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
	public void start(boolean hadToInterruptToStart) {
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		motor.setTargetVelocity(velocity);
	}

	@Override
	public void update() {}

	@Override
	public void stop(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return Math.abs(motor.getVelocity() - velocity) < tolerance;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Velocity: %s to %f", motor.toString(), velocity);
	}
}