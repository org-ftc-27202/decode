package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.runnables.defaultdirectives.DefaultDrivebase;
import org.firstinspires.ftc.teamcode.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

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

		// print telemetry
		telemetry.addLine();
		try {
			telemetry.addLine(tars.getScheduler().toString());
		} catch (Exception e) {
			telemetry.addData("telemetry didn't work", e);
		}
		telemetry.update();

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

			telemetry.addLine();
			telemetry.addLine("Honesty setting: 90%");
			telemetry.addLine("Humor setting: 75%");

			telemetry.update();
		}

		// cancel triggers and runnables
		tars.cancelAll();
	}
}