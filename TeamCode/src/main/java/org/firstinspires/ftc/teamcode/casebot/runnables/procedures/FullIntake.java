package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				new IntakeAt(0),
				new IntakeAt(1),
				new IntakeAt(2),
				new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(1, Spindexer.Position.TRANSFER))
		);

		setInterruptible(true);

		setRequiredSubsystems(
				subsystem(LeverTransfer.class),
				subsystem(Spindexer.class)
		);
	}
}