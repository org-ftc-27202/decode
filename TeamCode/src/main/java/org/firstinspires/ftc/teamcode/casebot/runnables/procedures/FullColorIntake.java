package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;

public class FullColorIntake extends Procedure {
    public FullColorIntake() {
        super(
                "FullColorIntake",
                new IntakeAtWaitForColor(0),
                new IntakeAtWaitForColor(1),
                new IntakeAtWaitForColor(2),
                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
        );

        setRequiredSubsystems(
                LeverTransfer.getInstance(),
                Spindexer.getInstance()
        );
    }
}