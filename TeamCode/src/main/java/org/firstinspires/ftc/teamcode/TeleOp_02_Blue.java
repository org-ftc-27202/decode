package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "02 Blue (TeleOp)", group = "00 Robot")
//@Disabled

public class TeleOp_02_Blue extends TeleOp_00_base {
    @Override
    public void runOpMode() {
        super.setAllianceColor(AllianceColors.BLUE);
        super.runOpMode();
    }
}
