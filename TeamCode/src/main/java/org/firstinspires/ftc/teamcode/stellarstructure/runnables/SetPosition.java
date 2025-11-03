package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

public class SetPosition extends Directive {
	private final StellarServo servo;
	private final double targetPosition;
	private final double tolerance;

	public SetPosition(StellarServo servo, double targetPosition) {
		this(servo, targetPosition, 0.01);
	}

	public SetPosition(StellarServo servo, double targetPosition, double acceptableRange) {
		this.servo = servo;
		this.targetPosition = targetPosition;
		this.tolerance = acceptableRange;
		setInterruptible(true);
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		servo.setPosition(targetPosition);
	}

	@Override
	public void update() {}

	@Override
	public void stop(boolean interrupted) {}

	public SetPosition requires(Subsystem... subsystems) {
		setRequiredSubsystems(subsystems);
		return this;
	}

	public SetPosition interruptible(boolean interruptible) {
		setInterruptible(interruptible);
		return this;
	}

	@Override
	public boolean isFinished() {
		return Math.abs(servo.getPosition() - targetPosition) <= tolerance;
	}
}
