package org.firstinspires.ftc.teamcode.tars.opmodes;

import org.firstinspires.ftc.teamcode.stellarstructure.Scheduler;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultDrivebase;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.tars.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TARS +Ethan's Approval", group = "Robot")
public class TarsTele extends LinearOpMode {
	private final Drivebase drivebase = Drivebase.getInstance();
	private final Intake intake = Intake.getInstance();
	private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
	private final Spindexer spindexer = Spindexer.getInstance();

	private final StellarBot tars = new StellarBot(
			drivebase,
			intake,
			leverTransfer,
			spindexer
	);

	@Override
	public void runOpMode() {
		// set up subsystems
		tars.init(hardwareMap);

		// set up default directives
		drivebase.setDefaultDirective(new DefaultDrivebase(gamepad1));
		intake.setDefaultDirective(new DefaultIntake(gamepad1));
		leverTransfer.setDefaultDirective(new DefaultLeverTransfer(gamepad1));
		spindexer.setDefaultDirective(new DefaultSpindexer(gamepad1));

		int iteration = 0;

		while (!opModeIsActive()) {
			// print telemetry
			try {
				StringBuilder spaces = new StringBuilder();
				for (int i = 0; i < iteration % 6; i++) {
					spaces.append(" ");
				}

				//telemetry.addLine(String.format("Time: %f", System.currentTimeMillis() / 1000.0));
				telemetry.addLine(
						"\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								spaces + "█   █   █   █   █   █   █   █   █   █   █   █   █   █   █   " + (spaces.length() < 3 ? "█" : "") + "\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n" +
								"▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄\n"

						// 32x24 pixels with driver station warning
				);

				//telemetry.addLine(tars.getScheduler().toString());
				sleep(325); // around 3 fps

				iteration++;
			} catch (Exception e) {
				telemetry.addData("telemetry didn't work", e);
			}
			telemetry.update();

			//waitForStart();

			if (isStopRequested()) return;
		}


		while (opModeIsActive()) {
			// panic cancel
			if (gamepad1.left_bumper && gamepad1.right_bumper) {
				tars.cancelAll();
			}

			// run scheduler and subsystems logic
			tars.update();

			//print telemetry
			try {
				telemetry.addLine(tars.toString());
			} catch (Exception e) {
				telemetry.addData("telemetry didn't work", e);
			}

			telemetry.addLine();
			telemetry.addLine("Honesty setting: 90%");
			telemetry.addLine("Humor setting: 75%");

			telemetry.update();
		}

		// cancel triggers and runnables
		tars.cancelAll();
	}
}