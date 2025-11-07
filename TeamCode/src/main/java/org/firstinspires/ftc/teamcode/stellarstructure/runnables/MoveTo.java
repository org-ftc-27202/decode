package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

public class MoveTo extends Directive {
	private final StellarDcMotor motor;
	private final int targetPosition;
	private final double power;
	private final double tolerance;

	public MoveTo(@NonNull StellarDcMotor motor, int targetPosition, double power) {
		this(motor, targetPosition, power, 5);
	}

	public MoveTo(@NonNull StellarDcMotor motor, int targetPosition, double power, double acceptableRange) {
		this.motor = motor;
		this.targetPosition = targetPosition;
		this.power = power;
		this.tolerance = acceptableRange;
		setInterruptible(true);
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		motor.setTargetPosition(targetPosition);
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		motor.setPower(Math.abs(power));
	}

	@Override
	public void update() {}

	@Override
	public void stop(boolean interrupted) {
		motor.setPower(0);
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}

	public MoveTo requires(Subsystem... subsystems) {
		setRequiredSubsystems(subsystems);
		return this;
	}

	public MoveTo interruptible(boolean interruptible) {
		setInterruptible(interruptible);
		return this;
	}

	@Override
	public boolean isFinished() {
		return Math.abs(motor.getCurrentPosition() - targetPosition) <= tolerance;
	}
}
