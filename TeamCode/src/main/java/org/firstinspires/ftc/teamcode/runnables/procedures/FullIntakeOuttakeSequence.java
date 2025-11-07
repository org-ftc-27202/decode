package org.firstinspires.ftc.teamcode.runnables.procedures;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

public class FullIntakeOuttakeSequence extends Procedure {
	public FullIntakeOuttakeSequence() {
		super(
				"FullIntakeOuttakeSequence",
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

				// move to transfer position and pulse lever
				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(2, Spindexer.Position.TRANSFER), 0.01),
				new Sleep(0.15),

				new PulseTransferLever(),
				new Sleep(0.05),

				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(0, Spindexer.Position.TRANSFER), 0.01),
				new Sleep(0.05),

				new PulseTransferLever(),
				new Sleep(0.05),

				new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(1, Spindexer.Position.TRANSFER), 0.01),
				new Sleep(0.05),

				new PulseTransferLever()
		);
		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/
	}
}