package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class FullOuttake extends Procedure {
    public FullOuttake() {
        super(
                "FullOuttake",
                new InstantlyDo(() -> subsystem(Transfer.class).setTransferPower(1.0)),
                new Sleep(3.0),
                new InstantlyDo(() -> subsystem(Transfer.class).setTransferPower(0.0))
        );

        setRequiredSubsystems(
                subsystem(Transfer.class),
                subsystem(Turret.class)
        );

        setInterruptible(false);
    }
}
