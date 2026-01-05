package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

import java.util.function.Supplier;

public class SetSpinPos extends Directive {
	private final StellarServo servo;
	private final Supplier<Double> targetPositionSupplier;
	private double targetPosition = 0.0;

	public SetSpinPos(@NonNull StellarServo servo, Supplier<Double> targetPosition) {
		super();
		this.servo = servo;
		this.targetPositionSupplier = targetPosition;

		setInterruptible(true);
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		this.targetPosition = targetPositionSupplier.get();

		servo.setPosition(targetPosition);
	}

	@Override
	protected void onUpdate() {}

	@Override
	protected void onStop(boolean interrupted) {}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Set Position %s to %f", servo.toString(), targetPosition);
	}
}
