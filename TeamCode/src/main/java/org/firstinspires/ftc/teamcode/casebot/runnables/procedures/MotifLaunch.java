package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;

public class MotifLaunch extends Procedure {
    public MotifLaunch() {
        super("FarMotifLaunch",
                new InstantlyDo(()-> {
                    subsystem(Intake.class).setIntakeSpeed(0.2);
                    subsystem(Intake.class).setMotorSpeed();
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    if (subsystem(PedroDrivebase.class).getLocalizationMode()) {
                        subsystem(PedroDrivebase.class).getFollower().turnTo(Math.toRadians(subsystem(PedroDrivebase.class).getLaunchYaw()));
                    }
                }),
                new Parallel(
                        "Launch+Stop",
                        new FullMotifOuttake(),
                        new Procedure(
                                "Start Driving",
                                new Sleep(1.0),
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
                subsystem(Spindexer.class),
                subsystem(Intake.class)
        );

        setInterruptible(false);
    }
}
