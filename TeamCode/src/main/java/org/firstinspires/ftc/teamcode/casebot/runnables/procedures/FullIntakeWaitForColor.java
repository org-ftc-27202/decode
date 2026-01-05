package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;

public class FullIntakeWaitForColor extends Procedure {
    public FullIntakeWaitForColor() {
        super(
                "FullIntakeWaitForColor",
                new IntakeAtWaitForColor(0),
                new IntakeAtWaitForColor(1),
                new IntakeAtWaitForColor(2),
                new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
        );

        setRequiredSubsystems(
                subsystem(LeverTransfer.class),
                subsystem(Spindexer.class)
        );
    }
}