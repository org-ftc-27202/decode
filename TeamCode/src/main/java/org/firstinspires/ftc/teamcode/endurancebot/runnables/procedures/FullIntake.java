package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				new IntakeAt(0),
				new IntakeAt(1),
				new IntakeAt(2),
				new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
		);

		setInterruptible(true);

		setRequiredSubsystems(
				subsystem(Transfer.class),
				subsystem(Spindexer.class)
		);
	}
}