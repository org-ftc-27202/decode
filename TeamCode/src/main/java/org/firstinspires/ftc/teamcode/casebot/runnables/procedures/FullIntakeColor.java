package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class FullIntakeColor extends Procedure {
    public FullIntakeColor() {
        super(
                "FullIntakeWaitForColor",
                new IntakeColor(0),
                new IntakeColor(1),
                new IntakeColor(2),
                new SetPos(subsystem(Spindexer.class).getSpindexerServo(), subsystem(Spindexer.class).getServoPositionFromSegment(1, Spindexer.Position.TRANSFER)),
                new InstantlyDo(() -> {
                    subsystem(Intake.class).setIntakeSpeed(-1.0);
                    subsystem(Intake.class).setMotorSpeed();
                }),
                new Sleep(0.2),
                new InstantlyDo(() -> {
                    subsystem(Intake.class).setIntakeSpeed(1.0);
                    subsystem(Intake.class).setMotorSpeed();
                })
        );

        setInterruptible(true);

        setRequiredSubsystems(
                subsystem(LeverTransfer.class),
                subsystem(Spindexer.class)
        );
    }
}