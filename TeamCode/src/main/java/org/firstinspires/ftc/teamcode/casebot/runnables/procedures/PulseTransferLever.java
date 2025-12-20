package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class PulseTransferLever extends Procedure {
	public PulseTransferLever() {
		super(
				"PulseTransferLever",
				// down
				//new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
				//new Sleep(0.10),

				// up
				new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_UP_POSITION),
				new Sleep(0.10),

				// down
				new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION)//,
				//new Sleep(0.05)
		);

		setRequiredSubsystems(subsystem(LeverTransfer.class));

		/*
		setStartingConditions(
				// spindexer outtake position
				() -> Spindexer.getInstance().getIsTransferPosition()
		);*/

		setRequiredSubsystems(
				subsystem(LeverTransfer.class)
		);
	}
}