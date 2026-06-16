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
                new InstantlyDo(()-> {
                    subsystem(Turret.class).setLaunchMode(true);
                    subsystem(Turret.class).setCoverOpen();
                    subsystem(Transfer.class).setTransferPower(1.0);
                    subsystem(Transfer.class).setTransferWheelPower(-1.0);
                    subsystem(Transfer.class).setIntakePhase(false);
                }),
                new Sleep(3.0),
                new InstantlyDo(() -> {
                    subsystem(Turret.class).setLaunchMode(false);
                    subsystem(Turret.class).setCoverClosed();
                    subsystem(Transfer.class).setTransferPower(0.0);
                    subsystem(Transfer.class).setTransferWheelPower(0.0);
                }),
                new IntakeThree()
        );

        setRequiredSubsystems(
                subsystem(Transfer.class),
                subsystem(Turret.class)
        );

        setInterruptible(false);
    }
}
