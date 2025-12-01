package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				new IntakeAt(0),
				new IntakeAt(1),
				new IntakeAt(2),
				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(0, Spindexer.Position.TRANSFER))
		);

		setRequiredSubsystems(
				LeverTransfer.getInstance(),
				Spindexer.getInstance()
		);
	}
}