package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeColor extends Procedure {
    public OuttakeColor(@NonNull DecodeDataTypes.ArtifactColor artifactColor) {
        super(
                "OuttakeColor",
                new OuttakeAt(() -> Spindexer.getInstance().getFirstColorSegmentLocation(artifactColor))
        );

        setWaitForStartingConditions(false);
        setStartingConditions(() -> Spindexer.getInstance().getHasArtifactColor(artifactColor));

        setRequiredSubsystems(
                Intake.getInstance(),
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );
    }
}