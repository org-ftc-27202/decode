package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class FarSingleLaunch extends Procedure {
    public FarSingleLaunch(DecodeDataTypes.ArtifactColor artifactColor) {
        super("FarSingleLaunch",
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
                        new OuttakeColor(artifactColor),
                        new Procedure(
                                "Start Driving",
                                new Sleep(.5),
                                new InstantlyDo(()->
                                        PedroDrivebase.getInstance().getFollower().startTeleopDrive(true)
                                )
                        )
                )

        );

        setRequiredSubsystems(PedroDrivebase.getInstance(), Turret.getInstance());
    }
}