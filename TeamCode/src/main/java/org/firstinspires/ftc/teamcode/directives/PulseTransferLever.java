package org.firstinspires.ftc.teamcode.directives;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

public class PulseTransferLever extends Procedure {
	public PulseTransferLever() {
		super(
				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION, 0.01),
				new Sleep(0.03),

				// up
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_UP_POSITION, 0.01),
				new Sleep(0.03),

				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION, 0.01)
		);

		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/
	}
}