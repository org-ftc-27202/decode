package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

public class FullMotifOuttake extends Procedure {
    public FullMotifOuttake() {
        super(
                "FullMotifOuttake",
                new OuttakeIdealAt(0),
                new OuttakeIdealAt(1),
                new OuttakeIdealAt(2),
                new InstantlyDo(()->{
                    subsystem(PedroDrivebase.class).getRightLight().setPosition(0.333);
                }
                )
        );

        setWaitForStartingConditions(false);
        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );

        setInterruptible(false);
    }
}
