package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "02B Blue Far From Goal (Auto)", group = "00 Robot", preselectTeleOp = "02B Blue Far From Goal (TeleOp)")

public class auto_02b_FarFromGoal_Blue extends auto_01_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.BLUE;
        Config.goalOption = Config.GoalOptions.FAR;
        super.onInit();
    }
}