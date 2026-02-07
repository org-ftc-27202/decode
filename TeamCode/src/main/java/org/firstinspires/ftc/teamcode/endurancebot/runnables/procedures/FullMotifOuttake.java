package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

public class FullMotifOuttake extends Procedure {
    public FullMotifOuttake() {
        super(
                "FullMotifOuttake",
                new OuttakeIdealAt(0),
                new OuttakeIdealAt(1),
                new OuttakeIdealAt(2)
        );

        setWaitForStartingConditions(false);
        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(Transfer.class)
        );

        setInterruptible(false);
    }
}
