package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class FullIntakeWaitForColor extends Procedure {
    public FullIntakeWaitForColor() {
        super(
                "FullIntakeWaitForColor",
                new IntakeAtWaitForColor(0),
                new IntakeAtWaitForColor(1),
                new IntakeAtWaitForColor(2),
                new SetPosition(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
        );

        setRequiredSubsystems(
                subsystem(LeverTransfer.class),
                subsystem(Spindexer.class)
        );
    }
}