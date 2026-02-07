package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;

public class IntakeAt extends Procedure {
    public IntakeAt(int segment) {
        super(
                "IntakeAt",
                new SetPos(subsystem(Transfer.class).getLeverTransferServo(), Transfer.LEVER_DOWN_POSITION),

                new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(segment, Spindexer.Position.INTAKE)),

                new WaitUntil(() -> subsystem(Spindexer.class).spindexerEncoderIsWithinTolerance(subsystem(Spindexer.class).getServoPositionFromSegment(segment, Spindexer.Position.INTAKE), 0.05)),
                new WaitUntil(() ->
                        subsystem(Spindexer.class).getBreamBreak1Broken() ||
                        subsystem(Spindexer.class).getBeamBreak2Broken()
                )
        );

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(Transfer.class)
        );

        setInterruptible(false);
    }
}
