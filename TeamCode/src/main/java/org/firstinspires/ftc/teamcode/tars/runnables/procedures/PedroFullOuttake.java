package org.firstinspires.ftc.teamcode.tars.runnables.procedures;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetLight;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class PedroFullOuttake extends Procedure {
    public PedroFullOuttake(){
        super(
                "PedroOuttake",

                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(2, Spindexer.Position.TRANSFER), 0.01),

                new WaitUntil(()->PedroDrivebase.getInstance().checkForLaunchPose()),

                new SetLight(PedroDrivebase.getInstance().getLight(), "RED"),

                new PulseTransferLever(),
                new Sleep(0.05),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(0, Spindexer.Position.TRANSFER), 0.01),
                new Sleep(0.05),
                new PulseTransferLever(),
                new Sleep(0.05),

                new SetPosition(Spindexer.getInstance().getSpindexerServo(), Spindexer.getInstance().getDegreesForSegmentPosition(1, Spindexer.Position.TRANSFER), 0.01),
                new Sleep(0.05),
                new PulseTransferLever(),
                new Sleep(0.05)
        );
    }
}
