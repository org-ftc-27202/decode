package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class PulseTransferLever extends Procedure {
	public PulseTransferLever() {
		super(
				"PulseTransferLever",
				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
				new Sleep(0.05),

				// up
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_UP_POSITION),
				new Sleep(0.05),

				// down
				new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION)//,
				//new Sleep(0.05)
		);

		setRequiredSubsystems(LeverTransfer.getInstance());

		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/

		setRequiredSubsystems(
				LeverTransfer.getInstance()
		);
	}
}