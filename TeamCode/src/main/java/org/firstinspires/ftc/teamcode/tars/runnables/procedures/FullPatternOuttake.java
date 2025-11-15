package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.OuttakeColor;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class FullPatternOuttake extends Procedure {
    public FullPatternOuttake() {
        super(
                "FullPatternOuttake",
                new OuttakeColor(DecodeDataTypes.ArtifactColor.PURPLE),
                new OuttakeColor(DecodeDataTypes.ArtifactColor.GREEN),
                new OuttakeColor(DecodeDataTypes.ArtifactColor.PURPLE)
        );
    }
}
