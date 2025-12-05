package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class TurretStartup extends Procedure {
    public TurretStartup() {
        super("TurretStartup",
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(0.2)),
                new Sleep(0.3),

                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(0.4)),
                new Sleep(0.3),

                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(0.6)),
                new Sleep(0.3),

                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(0.8)),
                new Sleep(0.3),

                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(1.0))
        );

        setRequiredSubsystems(Turret.getInstance());
    }
}