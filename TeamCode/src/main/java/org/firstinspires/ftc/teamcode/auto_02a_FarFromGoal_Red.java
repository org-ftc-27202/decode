package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "02A Red Far From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01A Red Near From Goal (TeleOp)")

public class auto_02a_FarFromGoal_Red extends auto_02_FarFromGoal_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.RED;
        Config.goalOption = Config.GoalOptions.FAR;
        super.onInit();
    }
}