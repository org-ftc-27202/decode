package org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarLaunch;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;

public class PedroDefaultDrivebase extends DefaultDirective {
	private final Gamepad gamepad1;
	private final Gamepad gamepad2;

	public PedroDefaultDrivebase(Gamepad gamepad1, Gamepad gamepad2) {
		super(Drivebase.getInstance());

		this.gamepad1 = gamepad1;
		this.gamepad2 = gamepad2;
		addTrigger(new ActionTrigger(
				// when dpad up just first pressed
				new StatefulCondition(
						new GamepadButtonMap(this.gamepad2, GamepadButtonMap.Button.A),
						StatefulCondition.Edge.RISING
				),
				() -> {
					new FarLaunch().schedule();
				}
		));
	}
	@Override
	public void update() {
		double max, axial, lateral, yaw;
		double leftFrontPower, rightFrontPower, leftBackPower, rightBackPower;
			// make the last parameter false for field-centric
			// in case the drivers want to use a "slowMode" you can scale the vectors
			// this is the normal version to use in the teleop

			PedroDrivebase.getInstance().getFollower().setTeleOpDrive(
					-gamepad1.left_stick_y,
					-gamepad1.left_stick_x,
					-gamepad1.right_stick_x * 0.75001,
					true
			); // robot centric

	}
}