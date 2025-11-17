package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
public class MoveTo extends Directive {
	private final StellarDcMotor motor;
	private final int targetPosition;
	private final double power;

	public MoveTo(@NonNull StellarDcMotor motor, int targetPosition, double power) {
		this.motor = motor;
		this.targetPosition = targetPosition;
		this.power = power;
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

	@Override
	public boolean isFinished() {
		return true;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("MoveTo: %s to %d ", motor.toString(), targetPosition);
	}
}
