package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.util.GameState;

public class FullPatternOuttake extends Procedure {
    public FullPatternOuttake() {
        super(
                "FullPatternOuttake",
                new OuttakeColor(GameState.getMotifPatternAt(0)),
                new OuttakeColor(GameState.getMotifPatternAt(1)),
                new OuttakeColor(GameState.getMotifPatternAt(2))
        );

        setWaitForStartingConditions(false);
        setRequiredSubsystems(
                subsystem(Spindexer.class),
                subsystem(Transfer.class)
        );

        setInterruptible(false);
    }
}
