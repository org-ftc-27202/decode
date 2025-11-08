package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class FullIntake extends Procedure {
	public FullIntake() {
		super(
				"FullIntake",
				// set transfer lever down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

				// set to position and wait until artifact
				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(0, Spindexer.Position.INTAKE), 0.01),
				new Sleep(0.5),
				new WaitUntil(() -> !Spindexer.getInstance().getBeamBreak().getState()),
				new Sleep(0.05),
				//new WaitUntil(() -> Spindexer.getInstance().getBeamBreak().getState()),

				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(1, Spindexer.Position.INTAKE), 0.01),
				new Sleep(0.5),
				new WaitUntil(() -> !Spindexer.getInstance().getBeamBreak().getState()),
				new Sleep(0.05),
				//new WaitUntil(() -> Spindexer.getInstance().getBeamBreak().getState()),

				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(2, Spindexer.Position.INTAKE), 0.01),
				new Sleep(0.5),
				new WaitUntil(() -> !Spindexer.getInstance().getBeamBreak().getState()),
				new Sleep(0.05),
				//new WaitUntil(() -> Spindexer.getInstance().getBeamBreak().getState()),

				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(2, Spindexer.Position.TRANSFER), 0.01)
		);
		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/
	}
}