package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;

public class PulseTransferLever extends Procedure {
	public PulseTransferLever() {
		super(
				"PulseTransferLever",
				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
				new Sleep(0.1),

				// up
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_UP_POSITION),
				new Sleep(0.3),

				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
				new Sleep(0.1)
		);

		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/
	}
}