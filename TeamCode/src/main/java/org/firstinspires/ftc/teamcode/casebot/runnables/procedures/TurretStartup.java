package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

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

                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(1200.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(1400.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> subsystem(Turret.class).setTurretVelocity(1600.0))
        );

        setRequiredSubsystems(
                subsystem(Turret.class)
        );
    }
}