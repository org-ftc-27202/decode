package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeAt extends Procedure {
    public OuttakeAt(int segment) {
        super(
                "OuttakeAt",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(segment, Spindexer.Position.TRANSFER), 0.01),
                new InstantlyDo(() -> {
                    Spindexer.getInstance().setArtifactInSpindexer(segment, DecodeDataTypes.ArtifactColor.NONE);
                }),
                new Sleep(0.15),
                new PulseTransferLever(),
                new Sleep(0.05)
        );
    }
}
