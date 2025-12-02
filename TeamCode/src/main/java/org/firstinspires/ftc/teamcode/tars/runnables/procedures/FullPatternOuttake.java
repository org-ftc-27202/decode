package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class FullPatternOuttake extends Procedure {
    public FullPatternOuttake() {
        super(
                "FullPatternOuttake",
                new OuttakeColor(DecodeDataTypes.ArtifactColor.PURPLE),
                new OuttakeColor(DecodeDataTypes.ArtifactColor.GREEN),
                new OuttakeColor(DecodeDataTypes.ArtifactColor.PURPLE)
        );

        setWaitForStartingConditions(false);
        setRequiredSubsystems(
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );
    }
}
