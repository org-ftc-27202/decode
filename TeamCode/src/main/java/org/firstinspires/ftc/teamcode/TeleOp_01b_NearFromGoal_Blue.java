package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "01B Blue Near From Goal (TeleOp)", group = "00 Robot")
//@Disabled

public class TeleOp_01b_NearFromGoal_Blue extends TeleOp_01_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.BLUE;
        Config.goalOption = Config.GoalOptions.NEAR;
        super.onInit();
    }
}
