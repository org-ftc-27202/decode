package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name = "02B Blue Far From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01B Blue Near From Goal (TeleOp)")

public class auto_02b_FarFromGoal_Blue extends auto_02_FarFromGoal_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.BLUE;
        Config.goalOption = Config.GoalOptions.FAR;
        super.onInit();
    }
}