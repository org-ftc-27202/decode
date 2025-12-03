package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.tars.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;

public class PedroDefaultDrivebase extends DefaultDirective {
	private final PedroDrivebase pedroDrivebase = PedroDrivebase.getInstance();
	private final Gamepad gamepad1;

	public PedroDefaultDrivebase(Gamepad gamepad1) {
		super(Drivebase.getInstance());

		this.gamepad1 = gamepad1;
	}

	@Override
	public void update() {
		double max, axial, lateral, yaw;
		double leftFrontPower, rightFrontPower, leftBackPower, rightBackPower;
		axial = -gamepad1.left_stick_y * Drivebase.CARDINAL_SPEED;  // Note: pushing stick forward gives negative value
		lateral = gamepad1.left_stick_x * Drivebase.CARDINAL_SPEED;
		yaw = gamepad1.right_stick_x * Drivebase.TURN_SPEED;

		// combine the joystick requests for each axis-motion to determine each wheel's power
		leftFrontPower = axial + lateral + yaw;
		rightFrontPower = axial - lateral - yaw;
		leftBackPower = axial - lateral + yaw;
		rightBackPower = axial + lateral - yaw;

		// normalize the values so no wheel power exceeds 100%
		// this ensures that the robot maintains the desired motion
		max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
		max = Math.max(max, Math.abs(leftBackPower));
		max = Math.max(max, Math.abs(rightBackPower));

		// maintain desired motion
		if (max > 1.0) {
			leftFrontPower /= max;
			rightFrontPower /= max;
			leftBackPower /= max;
			rightBackPower /= max;
		}

		// send calculated power to wheels
		pedroDrivebase.setDrivePower(
				leftFrontPower * PedroDrivebase.getInstance().speedScale,
				rightFrontPower * PedroDrivebase.getInstance().speedScale,
				leftBackPower * PedroDrivebase.getInstance().speedScale,
				rightBackPower * PedroDrivebase.getInstance().speedScale
		);
	}
}