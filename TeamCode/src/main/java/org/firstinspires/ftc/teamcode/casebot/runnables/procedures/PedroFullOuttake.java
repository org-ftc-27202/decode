package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.SetSpeedScale;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;

public class PedroFullOuttake extends Procedure {
    public PedroFullOuttake(){
        super(
                "PedroOuttake",
                //new SetLight(PedroDrivebase.getInstance().getLeftLight(), "YELLOW"),
                //new SetLight(PedroDrivebase.getInstance().getRightLight(), "YELLOW"),

                new WaitUntil(() -> subsystem(PedroDrivebase.class).checkForLaunchPose()),
                new SetSpeedScale(0.5),
                //new SetLight(PedroDrivebase.getInstance().getLeftLight(), "RED"),
                //new SetLight(PedroDrivebase.getInstance().getRightLight(), "RED"),
                new OuttakeAt(() -> 0),
                new OuttakeAt(() -> 1),
                new OuttakeAt(() -> 2),

                new Sleep(1.0),
                new SetSpeedScale(1.0)
                //new SetLight(PedroDrivebase.getInstance().getLeftLight(), "BLUE"),
                //new SetLight(PedroDrivebase.getInstance().getRightLight(), "BLUE")
        );

        setRequiredSubsystems(
                subsystem(Intake.class),
                subsystem(Spindexer.class),
                subsystem(LeverTransfer.class)
        );
    }
}
