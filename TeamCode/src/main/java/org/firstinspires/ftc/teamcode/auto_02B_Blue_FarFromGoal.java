package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "02A Blue Far From Goal (Auto)", group = "00 Robot", preselectTeleOp = "02 Blue (TeleOp)")

public class auto_02B_Blue_FarFromGoal extends auto_00_base {

    @Override
    public void runOpMode() {
        super.setAllianceColor(TeleOp_00_base.AllianceColors.BLUE);
        super.runOpMode();
    }
}