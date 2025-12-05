package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

public class OuttakeMotifOrArtifactAt extends Procedure {
	public OuttakeMotifOrArtifactAt(int motifIndex) {
		super(
				"OuttakeMotifOrArtifactAt",
				new OuttakeAt(() -> Spindexer.getInstance().getMotifSegmentOrFirstArtifact(motifIndex))
		);

		setWaitForStartingConditions(false);
		setStartingConditions(() -> Spindexer.getInstance().getFirstArtifactLocation() != -1);

		setRequiredSubsystems(
				Spindexer.getInstance(),
				LeverTransfer.getInstance()
		);
	}
}