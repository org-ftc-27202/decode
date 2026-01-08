package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
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
                        subsystem(Turret.class).setTurretVelocity(1600.0)
                ),
                new SetPos(subsystem(Turret.class).getTurretHoodServo(), 0.01),
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
                subsystem(LeverTransfer.class),
                subsystem(Spindexer.class)
        );

        setInterruptible(false);
    }
}
