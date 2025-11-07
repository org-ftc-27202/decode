package org.firstinspires.ftc.teamcode.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;

public class PulseTransferLever extends Procedure {
	private static final PulseTransferLever instance = new PulseTransferLever();
	public static PulseTransferLever getInstance() {
		return instance;
	}

	private PulseTransferLever() {
		super(
				"pulseTransferLever",
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