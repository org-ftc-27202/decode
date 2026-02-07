package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class ShortColorLaunch extends Procedure {
    public ShortColorLaunch() {
        super("ShortColorLaunch",
                new InstantlyDo(()-> {
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    //PedroDrivebase.getInstance().getFollower().turnTo(Math.toRadians(PedroDrivebase.getInstance().getLaunchYaw()));
                }),
                new InstantlyDo(()->
                        subsystem(Turret.class).setTurretVelocity(1300.0)
                ),
                new SetPos(subsystem(Turret.class).getTurretHoodServo(), 0.25),
                new Parallel(
                        "Launch+Stop",
                        new FullPatternOuttake(),
                        new Procedure(
                                "Start Driving",
                                new Sleep(0.5),
                                new InstantlyDo(()->
                                        subsystem(PedroDrivebase.class).getFollower().startTeleopDrive(true)
                                )
                        )
                )
        );

        setRequiredSubsystems(
                subsystem(PedroDrivebase.class),
                subsystem(Turret.class)
        );

        setInterruptible(false);
    }
}
