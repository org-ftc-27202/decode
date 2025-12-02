package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class IntakeAtWaitForColor extends Procedure {
    public IntakeAtWaitForColor(int segment) {
        super(
                "IntakeAtWaitForColor",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(segment, Spindexer.Position.INTAKE)),

                new Sleep(0.3),
                new WaitUntil(() ->
                        (
                                !Spindexer.getInstance().getBeamBreak1().getState() ||
                                !Spindexer.getInstance().getBeamBreak2().getState()
                        ) &&
                        Spindexer.getInstance().setArtifactColorAtSegmentToColorSensor(segment) != DecodeDataTypes.ArtifactColor.NONE
                )
        );

        setRequiredSubsystems(
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );

        setRequiredSubsystems(Spindexer.getInstance(), LeverTransfer.getInstance());
    }
}
