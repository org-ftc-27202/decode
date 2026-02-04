package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled

@Autonomous(name = "01B Blue Near From Goal (Auto)", group = "00 Robot", preselectTeleOp = "01B Blue Near From Goal (TeleOp)")

public class auto_01b_NearFromGoal_Blue extends auto_01_NearFromGoal_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.BLUE;
        Config.goalOption = Config.GoalOptions.NEAR;
        super.onInit();
    }
}