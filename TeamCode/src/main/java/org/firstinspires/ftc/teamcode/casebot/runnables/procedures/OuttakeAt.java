package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.SetSpindexerPosition;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

import java.util.function.Supplier;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class OuttakeAt extends Procedure {
    // todo: make suppliers easier to use for procedures
    public OuttakeAt(Supplier<Integer> segmentSupplier) {
        super(
                "OuttakeAt",
                new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetSpindexerPosition(subsystem(Spindexer.class).getSpindexerServo(), () -> subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),
                //new SetPosition(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER)),

                new WaitUntil(() -> subsystem(Spindexer.class).spindexerEncoderIsWithinTolerance(subsystem(Spindexer.class).getDegreesForSegmentSupplierAndPosition(segmentSupplier, Spindexer.Position.TRANSFER), 0.05)),

                new WaitUntil(() -> subsystem(Turret.class).velocityWithinTolerance()),
                new PulseTransferLever(),

                new InstantlyDo(
                    () -> subsystem(Spindexer.class).setArtifactColorsInSpindexerFromSupplier(segmentSupplier, DecodeDataTypes.ArtifactColor.NONE)
                )
        );

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );
    }
}
