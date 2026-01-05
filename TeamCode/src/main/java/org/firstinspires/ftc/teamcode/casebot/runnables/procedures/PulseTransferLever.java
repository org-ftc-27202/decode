package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class PulseTransferLever extends Procedure {
	public PulseTransferLever() {
		super(
				"PulseTransferLever",
				// down
				//new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
				//new Sleep(0.10),

				// up
				new SetPos(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_UP_POSITION),
				new Sleep(0.10),

				// down
				new SetPos(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION)//,
				//new Sleep(0.05)
		);

		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/

		setRequiredSubsystems(
				subsystem(LeverTransfer.class)
		);

		setInterruptible(false);
	}
}