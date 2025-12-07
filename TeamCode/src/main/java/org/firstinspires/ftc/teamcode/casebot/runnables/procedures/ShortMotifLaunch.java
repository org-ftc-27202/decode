package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class ShortMotifLaunch extends Procedure {
    public ShortMotifLaunch() {
        super("ShortLaunch",
                new InstantlyDo(()-> {
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    PedroDrivebase.getInstance().getFollower().turnTo(Math.toRadians(PedroDrivebase.getInstance().getLaunchYaw()));
                }),
                new InstantlyDo(()->
                        Turret.getInstance().setTurretVelocity(1300)
                ),
                new SetPosition(Turret.getInstance().getTurretHoodServo(), 0.25),
                new Parallel(
                        "Launch+Stop",
                        new FullMotifOuttake(),
                        new Procedure(
                                "Start Driving",
                                new Sleep(.1),
                                new InstantlyDo(()->
                                        PedroDrivebase.getInstance().getFollower().startTeleopDrive(true)
                                )
                        )
                )
        );

        setRequiredSubsystems(PedroDrivebase.getInstance(), Turret.getInstance());
    }
}
