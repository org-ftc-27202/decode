package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "01B Red Far From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01 Red (TeleOp)")

public class auto_01B_Red_FarFromGoal extends auto_00_NearFromGoal {

    @Override
    public void runOpMode() {
        super.setAllianceColor(TeleOp_00_base.AllianceColors.RED);
        super.runOpMode();
    }
}