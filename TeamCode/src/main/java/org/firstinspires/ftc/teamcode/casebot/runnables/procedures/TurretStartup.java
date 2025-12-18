package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class TurretStartup extends Procedure {
    public TurretStartup() {
        super("TurretStartup",
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(160.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(400.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(620.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(860.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(1000.0)),
                new Sleep(0.06),

                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(1200.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(1400.0)),
                new Sleep(0.06),
                new InstantlyDo(()-> Turret.getInstance().setTurretVelocity(1600.0))
        );

        setRequiredSubsystems();
    }
}