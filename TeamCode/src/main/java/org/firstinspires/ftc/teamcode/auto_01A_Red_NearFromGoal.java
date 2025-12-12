package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "01A Red Near From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01 Red (TeleOp)")

public class auto_01A_Red_NearFromGoal extends auto_00_NearFromGoal {
    @Override
    public void init() {
        super.setAllianceColor(AllianceColors.RED);
        super.init();
    }
}