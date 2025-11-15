package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "02 Blue (TeleOp)", group = "Robot")
//@Disabled

public class blueTeleOp extends baseTeleOp  {
    @Override
    public void runOpMode() {
        super.setAllianceColor("BLUE");
        super.runOpMode();
    }
}
