package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				new IntakeAt(0),
				new IntakeAt(1),
				new IntakeAt(2),
				new SetPosition(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
		);

		setInterruptible(true);

		setRequiredSubsystems(
				subsystem(LeverTransfer.class),
				subsystem(Spindexer.class)
		);
	}
}