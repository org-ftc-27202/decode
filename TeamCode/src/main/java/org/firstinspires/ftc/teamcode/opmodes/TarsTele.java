package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TARS -Ethan's Approval", group = "Robot")
public class TarsTele extends LinearOpMode {
	final StellarBot tars = new StellarBot(
			Drivebase.getInstance(),
			Intake.getInstance(),
			LeverTransfer.getInstance(),
			Spindexer.getInstance()
	);

	@Override
	public void runOpMode() {
		// set up subsystems
		tars.init(hardwareMap);

		// set up gamepads
		tars.setGamepads(gamepad1, gamepad2);

		waitForStart();

		if (isStopRequested()) return;

		while (opModeIsActive()) {
			// run subsystems logic
			tars.update();

			//print telemetry
			try {
				telemetry.addLine(tars.toString());
			} catch (Exception e) {
				telemetry.addData("telemetry didn't work", e);
			}
			telemetry.addLine("Honesty setting: 90%");
			telemetry.addLine("Humor setting: 75%");

			telemetry.update();
		}

		// cancel triggers and runnables
		tars.cancelAll();
	}
}