package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "02A Blue Near From Goal (Auto)", group = "00 Robot", preselectTeleOp = "02 Blue (TeleOp)")

public class auto_02A_Blue_NearFromGoal extends auto_00_NearFromGoal {

    @Override
    public void runOpMode() {
        super.setAllianceColor(TeleOp_00_base.AllianceColors.BLUE);
        super.runOpMode();
    }
}