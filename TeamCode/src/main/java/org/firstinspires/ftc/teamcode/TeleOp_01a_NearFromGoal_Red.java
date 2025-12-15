package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "01A Red Near From Goal (TeleOp)", group = "00 Robot")
//@Disabled

public class TeleOp_01a_NearFromGoal_Red extends TeleOp_01_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.RED;
        Config.goalOption = Config.GoalOptions.NEAR;
        super.onInit();
    }
}
