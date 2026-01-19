package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "01A Red Near From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01A Red Near From Goal (TeleOp)")

public class auto_01a_NearFromGoal_Red extends auto_01_NearFromGoal_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.RED;
        Config.goalOption = Config.GoalOptions.NEAR;
        super.onInit();
    }
}