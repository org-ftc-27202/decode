package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Runnable;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeColor extends Procedure {
    public OuttakeColor(DecodeDataTypes.ArtifactColor artifactColor) {
        super(
                "OuttakeColor",
                new OuttakeAt(Spindexer.getInstance().getFirstColorSegmentLocation(artifactColor))
        );
        setWaitForStartingConditions(false);
    }
}