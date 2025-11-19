package org.firstinspires.ftc.teamcode.tars.opmodes;

import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultDrivebase;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.tars.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.BadApple;

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
		//long startTime = System.currentTimeMillis();
		//long currentTime = System.currentTimeMillis();
		//long lastTime = System.currentTimeMillis() - 333;

		while (!opModeIsActive()) {
			// print telemetry
			try {
				//lastTime = currentTime;
				//currentTime = System.currentTimeMillis();

				//telemetry.addLine(String.format("Time: %.3f", (currentTime - startTime) / 1000.0));
				//telemetry.addLine(String.format("Time Since: %.3f", (currentTime - lastTime) / 1000.0));
				telemetry.addLine(String.format("Frame %d / %d", iteration, BadApple.FRAMES.length));

				telemetry.addLine(
						BadApple.FRAMES[iteration]
				);

				//telemetry.addLine(tars.getScheduler().toString());
				sleep(328); // around 3 fps

				iteration++;
				if (iteration >= BadApple.FRAMES.length) {
					iteration = 0;
				}
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