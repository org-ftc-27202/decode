package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class FullOuttake extends Procedure {
    public FullOuttake() {
        super(
                "FullOuttake",
                new OuttakeAt(() -> 0),
                new OuttakeAt(() -> 1),
                new OuttakeAt(() -> 2)
        );

        setRequiredSubsystems(LeverTransfer.getInstance(),
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );
    }
}
