package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.ClearColor;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;

import java.util.function.Supplier;

public class OuttakeAt extends Procedure {
    protected final Supplier<Integer> segmentSupplier;
    public OuttakeAt(Supplier<Integer> segmentSupplier) {
        super(
                "OuttakeAt",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(segmentSupplier.get(), Spindexer.Position.TRANSFER)),
                new ClearColor(segmentSupplier.get()),

                new Sleep(0.15),

                new WaitUntil(() -> Turret.getInstance().velocityWithinTolerance()),

                new PulseTransferLever(),
                new Sleep(0.05)
        );

        this.segmentSupplier = segmentSupplier;
    }
}
