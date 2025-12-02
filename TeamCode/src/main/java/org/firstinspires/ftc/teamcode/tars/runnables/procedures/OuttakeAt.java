package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetSpindexerPosition;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

import java.util.function.Supplier;

public class OuttakeAt extends Procedure {
    // todo: make suppliers easier to use for procedures
    public OuttakeAt(Supplier<Integer> segmentSupplier) {
        super(
                "OuttakeAt",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetSpindexerPosition(Spindexer.getInstance().getSpindexerServo(), () -> Spindexer.getInstance().getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),
                //new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),
                new Sleep(0.05),

                new WaitUntil(() -> Turret.getInstance().velocityWithinTolerance()),
                new PulseTransferLever(),

                new InstantlyDo(
                    () -> Spindexer.getInstance().setArtifactColorsInSpindexerFromSupplier(segmentSupplier, DecodeDataTypes.ArtifactColor.NONE)
                )
        );

        setRequiredSubsystems(
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );
    }
}
