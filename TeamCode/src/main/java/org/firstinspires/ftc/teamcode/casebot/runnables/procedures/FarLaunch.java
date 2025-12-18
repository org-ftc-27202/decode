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

public class FarLaunch extends Procedure {
    public FarLaunch() {
        super("FarLaunch",
                new InstantlyDo(()-> {
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    PedroDrivebase.getInstance().getFollower().turnTo(Math.toRadians(PedroDrivebase.getInstance().getLaunchYaw()));
                }),
                new InstantlyDo(()->
                        Turret.getInstance().setTurretVelocity(1600)
                ),
                new SetPosition(Turret.getInstance().getTurretHoodServo(), 0.0),
                new Parallel(
                        "Launch+Stop",
                        new FullOuttake(),
                        new Procedure(
                                "Start Driving",
                                new Sleep(2.0),
                                new InstantlyDo(()->
                                        PedroDrivebase.getInstance().getFollower().startTeleopDrive(true)
                                )
                        )
                )

        );

        setRequiredSubsystems(
                PedroDrivebase.getInstance(),
                Turret.getInstance(),
                LeverTransfer.getInstance(),
                Spindexer.getInstance()
        );
    }
}
