package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class FullOuttakeThenIntake extends Procedure {
    public FullOuttakeThenIntake() {
        super(
                "FullOuttake",
                new InstantlyDo(()-> {
                    subsystem(Turret.class).setLaunchMode(true);
                    subsystem(Turret.class).setCoverOpen();
                    subsystem(Transfer.class).setTransferPower(1.0);
                    subsystem(Transfer.class).setTransferWheelPower(-1.0);
                    subsystem(Transfer.class).setIntakePhase(false);
                    subsystem(Transfer.class).setWasIntakePhaseLast(false);
                }),
                new Sleep(2.0),
                new InstantlyDo(() -> {
                    subsystem(Turret.class).setLaunchMode(false);
                    subsystem(Turret.class).setCoverClosed();
                    subsystem(Transfer.class).setTransferPower(0.0);
                    subsystem(Transfer.class).setTransferWheelPower(0.0);
                    subsystem(Transfer.class).setIntakePhase(true);
                })
        );

        setRequiredSubsystems(
                subsystem(Transfer.class),
                subsystem(Turret.class)
        );

        setInterruptible(false);
    }
}
