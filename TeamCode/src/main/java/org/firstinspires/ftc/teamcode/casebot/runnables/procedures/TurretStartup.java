package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class TurretStartup extends Procedure {
    public TurretStartup() {
        super("TurretStartup",
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(300)),
                new Sleep(0.5),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(600)),
                new Sleep(0.5),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(900)),
                new Sleep(0.5),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(1200)),
                new Sleep(0.5)
        );

        setRequiredSubsystems(Turret.getInstance());
    }
}