package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				new InstantlyDo(()->{
					subsystem(PedroDrivebase.class).getRightLight().setPosition(0.333);
				}
				),
				new IntakeAt(0),
				new IntakeAt(1),
				new IntakeAt(2),
				new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(1, Spindexer.Position.TRANSFER)),
				new InstantlyDo(() -> {
					subsystem(PedroDrivebase.class).getRightLight().setPosition(0.444);
					subsystem(Intake.class).setIntakeSpeed(-1.0);
					subsystem(Intake.class).setMotorSpeed();
				}),
				new Sleep(0.2),
				new InstantlyDo(() -> {
					subsystem(Intake.class).setIntakeSpeed(1.0);
					subsystem(Intake.class).setMotorSpeed();
				})
		);

		setInterruptible(true);

		setRequiredSubsystems(

		);
	}
}