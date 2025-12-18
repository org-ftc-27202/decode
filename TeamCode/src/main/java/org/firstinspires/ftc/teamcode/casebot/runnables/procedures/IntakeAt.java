package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;

public class IntakeAt extends Procedure {
    public IntakeAt(int segment) {
        super(
                "IntakeAt",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getServoPositionFromSegment(segment, Spindexer.Position.INTAKE)),

                new WaitUntil(() -> Spindexer.getInstance().spindexerEncoderIsWithinTolerance(Spindexer.getInstance().getServoPositionFromSegment(segment, Spindexer.Position.INTAKE), 0.05)),
                new WaitUntil(() ->
                        Spindexer.getInstance().getBreamBreak1Broken() ||
                        Spindexer.getInstance().getBeamBreak2Broken()
                )
        );

        setRequiredSubsystems(
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );

        setRequiredSubsystems(Spindexer.getInstance(), LeverTransfer.getInstance());
    }
}
