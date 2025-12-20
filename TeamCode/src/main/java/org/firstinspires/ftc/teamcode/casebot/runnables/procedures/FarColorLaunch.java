package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

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
                new SetPosition(subsystem(Turret.class).getTurretHoodServo(), 0.0),
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
    }
}
