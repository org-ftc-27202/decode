package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;

public class FarLaunch extends Procedure {
    public FarLaunch() {
        super("FarLaunch",
                new InstantlyDo(()-> {
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    PedroDrivebase.getInstance().getFollower().turnTo(Math.toRadians(PedroDrivebase.getInstance().getLaunchYaw()));
                }),
                new SetPosition(Turret.getInstance().getTurretHoodServo(), 0.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    PedroDrivebase.getInstance().getFollower().breakFollowing();
                    PedroDrivebase.getInstance().getFollower().deactivateAllPIDFs();
                })
        );

        setRequiredSubsystems(PedroDrivebase.getInstance());
    }
}
