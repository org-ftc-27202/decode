package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "02B Blue Far From Goal (TeleOp)", group = "00 Robot")
@Disabled

public class TeleOp_02b_FarFromGoal_Blue extends TeleOp_01_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.BLUE;
        Config.goalOption = Config.GoalOptions.FAR;
        super.onInit();
    }
}
