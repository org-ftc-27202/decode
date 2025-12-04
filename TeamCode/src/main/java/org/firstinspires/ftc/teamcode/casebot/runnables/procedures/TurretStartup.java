package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;

public class TurretStartup extends Procedure {
    public TurretStartup() {
        super("TurretStartup",
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(.2)
                        ),
                new Sleep(.3),
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(.4)
                ),
                new Sleep(.3),
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(.6)
                ),
                new Sleep(.3),
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(.8)
                ),
                new Sleep(.3),
                new InstantlyDo(()-> Turret.getInstance().setPIDFScale(1.0)
                )



        );

        setRequiredSubsystems(PedroDrivebase.getInstance(), Turret.getInstance());
    }
}
