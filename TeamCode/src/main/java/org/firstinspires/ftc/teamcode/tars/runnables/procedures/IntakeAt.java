package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class IntakeAt extends Procedure {
    public IntakeAt(int segment) {
        super(
                "IntakeAt",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(segment, Spindexer.Position.INTAKE), 0.01),

                new Sleep(0.5),
                new WaitUntil(() ->
                        !Spindexer.getInstance().getBeamBreak().getState() &&
                        Spindexer.getInstance().setArtifactColorAtSegmentToColorSensor(segment) != DecodeDataTypes.ArtifactColor.NONE
                ),
                new Sleep(0.05)
        );
    }
}
