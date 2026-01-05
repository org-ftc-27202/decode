package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
public class SetPos extends Directive {
	private final StellarServo servo;
	private final double targetPosition;
	public SetPos(@NonNull StellarServo servo, double targetPosition) {
		super();
		this.servo = servo;
		this.targetPosition = targetPosition;

		setInterruptible(true);
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		servo.setPosition(targetPosition);
	}

	@Override
	protected void onUpdate() {
		servo.setPosition(targetPosition);
	}

	@Override
	protected void onStop(boolean interrupted) {}

	@Override
	protected boolean isFinished() {
		return targetPosition == servo.getPosition();
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Position %s to %f", servo.toString(), targetPosition);
	}
}
