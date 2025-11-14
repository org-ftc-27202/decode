package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetLight;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetSpeedScale;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class PedroFullOuttake extends Procedure {
    public PedroFullOuttake(){
        super(
                "PedroOuttake",

                new SetLight(PedroDrivebase.getInstance().getLight(), "YELLOW"),
                new SetSpeedScale(.2),

                new WaitUntil(()->PedroDrivebase.getInstance().checkForLaunchPose()),

                new SetLight(PedroDrivebase.getInstance().getLight(), "RED"),
                new OuttakeAt(1),
                new OuttakeAt(2),
                new OuttakeAt(3),

                new Sleep(Spindexer.BUFFER_TIME),
                new SetSpeedScale(1),
                new SetLight(PedroDrivebase.getInstance().getLight(), "BLUE")
        );
    }
}
