package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

import java.util.function.Supplier;

public class SetSpindexerPosition extends Directive {
	private final StellarServo servo;
	private final Supplier<Double> targetPositionSupplier;
	private double targetPosition = 0;

	public SetSpindexerPosition(@NonNull StellarServo servo, Supplier<Double> targetPosition) {
		super();
		this.servo = servo;
		this.targetPositionSupplier = targetPosition;

		setInterruptible(true);
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		this.targetPosition = targetPositionSupplier.get();

		servo.setPosition(targetPosition);
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
		return String.format("Set Position %s to %f", servo.toString(), targetPosition);
	}
}
