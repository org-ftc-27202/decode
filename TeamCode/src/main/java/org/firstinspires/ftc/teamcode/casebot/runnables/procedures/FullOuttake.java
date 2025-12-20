package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class FullOuttake extends Procedure {
    public FullOuttake() {
        super(
                "FullOuttake",
                new OuttakeAt(() -> 0),
                new OuttakeAt(() -> 1),
                new OuttakeAt(() -> 2)
        );

        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );
    }
}
