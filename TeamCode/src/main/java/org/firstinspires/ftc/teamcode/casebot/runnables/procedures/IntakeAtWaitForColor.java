package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.SetLight;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class IntakeAtWaitForColor extends Procedure {
    public IntakeAtWaitForColor(int segment) {
        super(
                "IntakeAtWaitForColor",
                new SetPosition(subsystem(LeverTransfer.class).getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(segment, Spindexer.Position.INTAKE)),

                new WaitUntil(() -> subsystem(Spindexer.class).spindexerEncoderIsWithinTolerance(subsystem(Spindexer.class).getServoPositionFromSegment(segment, Spindexer.Position.INTAKE), 0.05)),
                new WaitUntil(() ->
                        (
                                subsystem(Spindexer.class).getBreamBreak1Broken() ||
                                        subsystem(Spindexer.class).getBeamBreak2Broken()
                        ) &&
                                subsystem(Spindexer.class).setArtifactColorAtSegmentToColorSensor(segment) != DecodeDataTypes.ArtifactColor.NONE
                ),
                new SetLight(subsystem(PedroDrivebase.class).getRightLight(), subsystem(Spindexer.class).getArtifactColorAt(segment))
        );

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );
    }
}
