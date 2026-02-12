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

public class Launch extends Procedure {
    public Launch() {
        super("FarLaunch",
                new InstantlyDo(()-> {
                    subsystem(Intake.class).setIntakeSpeed(-1.0);
                    subsystem(Intake.class).setMotorSpeed();
                    //PedroDrivebase.getInstance().getFollower().activateAllPIDFs();
                    if (subsystem(PedroDrivebase.class).getLocalizationMode()) {
                        subsystem(PedroDrivebase.class).getFollower().turn(Math.toRadians(subsystem(PedroDrivebase.class).getRobotHeadingErrorFromGoal()));
                    }
                }),
                new Sleep(0.5),
                new InstantlyDo(()-> {
                    subsystem(Intake.class).setIntakeSpeed(0.4);
                    subsystem(Intake.class).setMotorSpeed();
                }),
                new Parallel(
                        "Launch+Stop",
                        new FullOuttake(),
                        new Procedure(
                                "Start Driving",
                                new Sleep(1.5),
                                new InstantlyDo(()->
                                        subsystem(PedroDrivebase.class).getFollower().startTeleopDrive(true)
                                )
                        )
                ),
                new InstantlyDo(()-> {
                    subsystem(Intake.class).setIntakeSpeed(1.0);
                    subsystem(Intake.class).setMotorSpeed();
                })
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
