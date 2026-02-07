package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
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
                subsystem(Transfer.class)
        );

        setInterruptible(false);
    }
}