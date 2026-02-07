package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

public class OuttakeIdealAt extends Procedure {
	public OuttakeIdealAt(int motifIndex) {
		super(
				"OuttakeMotifOrArtifactAt",
				new OuttakeAt(() -> subsystem(Spindexer.class).getMotifSegmentOrFirstArtifact(motifIndex))
		);

		setWaitForStartingConditions(false);
		setStartingConditions(() -> subsystem(Spindexer.class).getFirstArtifactLocation() != -1);

		setRequiredSubsystems(
				subsystem(Spindexer.class),
				subsystem(Transfer.class)
		);

		setInterruptible(false);
	}
}