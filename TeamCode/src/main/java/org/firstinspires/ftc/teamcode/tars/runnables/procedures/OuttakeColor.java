package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetColor;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeColor extends Procedure {
    public OuttakeColor(DecodeDataTypes.ArtifactColor artifactColor) {
        super(
                "OuttakeColor",
                new OuttakeAt(Spindexer.getInstance().getColorLocation(artifactColor))
        );
    }
}