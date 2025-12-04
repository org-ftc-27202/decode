package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetVelocity;

public class FarLaunch extends Procedure {
    public FarLaunch() {
        super("FarLaunch",
                new InstantlyDo(()-> {
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    PedroDrivebase.getInstance().getFollower().turnTo(Math.toRadians(PedroDrivebase.getInstance().getLaunchYaw()));
                }),
                new InstantlyDo(()->
                        Turret.getInstance().setTurretVelocity(1600)),
                new SetPosition(Turret.getInstance().getTurretHoodServo(), 0.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    PedroDrivebase.getInstance().getFollower().startTeleopDrive(true);
                })
        );

        setRequiredSubsystems(PedroDrivebase.getInstance(), Turret.getInstance());
    }
}
