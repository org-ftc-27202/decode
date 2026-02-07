package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class TurretStartup extends Procedure {
    public TurretStartup() {
        super("TurretStartup",
                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(160.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(400.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(620.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(860.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(1000.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(1050.0)),
                new Sleep(0.06)

        );

        setRequiredSubsystems(
                subsystem(Turret.class)
        );

        setInterruptible(false);
    }
}