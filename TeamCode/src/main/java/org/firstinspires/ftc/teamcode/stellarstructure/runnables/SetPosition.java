package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
public class SetPosition extends Directive {
	private final StellarServo servo;
	private final double targetPosition;
	public SetPosition(@NonNull StellarServo servo, double targetPosition) {
		super();
		this.servo = servo;
		this.targetPosition = targetPosition;

		setInterruptible(true);
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		servo.setPosition(targetPosition);
	}

	@Override
	public void update() {
		servo.setPosition(targetPosition);
	}

	@Override
	public void stop(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return targetPosition == servo.getPosition();
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Position %s to %f", servo.toString(), targetPosition);
	}
}
