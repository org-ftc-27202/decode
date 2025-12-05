package org.firstinspires.ftc.teamcode.casebot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultDrivebase;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@TeleOp(name = "-Case +Ethan's Approval", group = "Robot")
public final class CaseTele extends LinearOpMode {
	private final Drivebase drivebase = Drivebase.getInstance();
	private final Intake intake = Intake.getInstance();
	private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
	private final Spindexer spindexer = Spindexer.getInstance();

	private final StellarBot caseBot = new StellarBot(
			drivebase,
			intake,
			leverTransfer,
			spindexer
	);

	@Override
	public void runOpMode() {
		// set up subsystems
		caseBot.init(hardwareMap);

		// set up default directives
		drivebase.setDefaultDirective(new DefaultDrivebase(gamepad1));
		intake.setDefaultDirective(new DefaultIntake(gamepad1));
		leverTransfer.setDefaultDirective(new DefaultLeverTransfer(gamepad1));
		spindexer.setDefaultDirective(new DefaultSpindexer(gamepad1, gamepad2));

		// print telemetry
		BootScreen bootScreen = new BootScreen(telemetry, new TerminalVelocityLogo(), true);
		try {
			bootScreen.updateBootScreen();
			sleep(bootScreen.getMillisBetweenFrames());
		} catch (Exception e) {
			telemetry.addData("telemetry didn't work", e);
		}
		telemetry.update();

		waitForStart();

		while (opModeIsActive()) {
			// panic cancel
			if (gamepad2.left_bumper && gamepad2.right_bumper) {
				caseBot.cancelAll();
			}

			// run scheduler and subsystems logic
			caseBot.update();

			//print telemetry
			try {
				telemetry.addLine(caseBot.toString());
			} catch (Exception e) {
				telemetry.addData("telemetry didn't work", e);
			}

			telemetry.addLine();
			telemetry.addLine("Honesty setting: 90%");
			telemetry.addLine("Humor setting: 75%");

			telemetry.update();
		}

		// cancel triggers and runnables
		caseBot.cancelAll();
	}
}