package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class FarColorLaunch extends Procedure {
    public FarColorLaunch() {
        super("FarColorLaunch",
                new InstantlyDo(()-> {
                    //subsystem(PedroDrivebase).getFollower().activateAllPIDFs();
                    //subsystem(PedroDrivebase).getFollower().turnTo(Math.toRadians(subsystem(PedroDrivebase).getLaunchYaw()));
                }),
                new InstantlyDo(()->
                        subsystem(Turret.class).setTurretVelocity(1500.0)
                ),
                new SetPos(subsystem(Turret.class).getTurretHoodServo(), 0.2),
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
                subsystem(Turret.class),
                subsystem(Transfer.class),
                subsystem(Spindexer.class)
        );

        setInterruptible(false);
    }
}
