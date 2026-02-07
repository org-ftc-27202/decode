package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.runnables.directives.SetSpinPos;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

import java.util.function.Supplier;

public class OuttakeAt extends Procedure {
    // todo: make suppliers easier to use for procedures
    public OuttakeAt(Supplier<Integer> segmentSupplier) {
        super(
                "OuttakeAt",
                new SetPos(subsystem(Transfer.class).getLeverTransferServo(), Transfer.LEVER_DOWN_POSITION),

                new SetSpinPos(subsystem(Spindexer.class).getSpindexerServo(), () -> subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),
                //new SetPosition(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),

                new WaitUntil(() -> subsystem(Spindexer.class).spindexerEncoderIsWithinTolerance(subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER), 0.05)),

                new WaitUntil(() -> subsystem(Turret.class).velocityWithinTolerance()),
                new PulseLever(),

                new InstantlyDo(
                    () -> subsystem(Spindexer.class).setArtifactColorsInSpindexerFromSupplier(segmentSupplier, DecodeDataTypes.ArtifactColor.NONE)
                )
        );

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(Transfer.class)
        );

        setInterruptible(false);
    }
}