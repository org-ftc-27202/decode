package org.firstinspires.ftc.teamcode.casebot.runnables.procedures.unused;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.OuttakeAt;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeColor extends Procedure {
    public OuttakeColor(@NonNull DecodeDataTypes.ArtifactColor artifactColor) {
        super(
                "OuttakeColor",
                new OuttakeAt(() -> subsystem(Spindexer.class).getFirstColorSegmentLocation(artifactColor))
        );

        setWaitForStartingConditions(false);
        setStartingConditions(() -> subsystem(Spindexer.class).getHasArtifactColor(artifactColor));

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );

        setInterruptible(false);
    }
}