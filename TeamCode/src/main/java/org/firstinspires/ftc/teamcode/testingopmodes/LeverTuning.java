package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Lever Tuning", group = "Robot")

public class LeverTuning extends LinearOpMode {
	Servo leverTransfer;

	@Override
	public void runOpMode() {
		leverTransfer = hardwareMap.get(Servo.class, "leverTransferServo");

		telemetry.addData("Lever", "Press START to continue...");
		telemetry.update();

		waitForStart();

		double servoPos = 0.2;

		while (opModeIsActive()) {
			servoPos -= 0.0002 * gamepad1.left_stick_y;

			leverTransfer.setPosition(servoPos);

			telemetry.addData("Lever", "Pos: " + servoPos);
			telemetry.update();
		}
	}
}