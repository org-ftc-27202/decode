package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

public class OuttakeMotifOrArtifactAt extends Procedure {
	public OuttakeMotifOrArtifactAt(int motifIndex) {
		super(
				"OuttakeMotifOrArtifactAt",
				new OuttakeAt(() -> subsystem(Spindexer.class).getMotifSegmentOrFirstArtifact(motifIndex))
		);

		setWaitForStartingConditions(false);
		setStartingConditions(() -> subsystem(Spindexer.class).getFirstArtifactLocation() != -1);

		setRequiredSubsystems(
				subsystem(Spindexer.class),
				subsystem(LeverTransfer.class)
		);
	}
}