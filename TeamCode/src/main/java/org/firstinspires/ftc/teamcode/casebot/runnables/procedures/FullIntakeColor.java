package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;

public class FullIntakeColor extends Procedure {
    public FullIntakeColor() {
        super(
                "FullIntakeWaitForColor",
                new IntakeColor(0),
                new IntakeColor(1),
                new IntakeColor(2),
                new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(0, Spindexer.Position.TRANSFER))
        );

        setInterruptible(true);

        setRequiredSubsystems(
                subsystem(LeverTransfer.class),
                subsystem(Spindexer.class)
        );
    }
}