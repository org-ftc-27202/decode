package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "01 Red (TeleOp)", group = "00 Robot")
//@Disabled

public class TeleOp_01_Red extends TeleOp_00_base {
    @Override
    public void onInit() {
        Config.allianceColor = Config.AllianceColors.RED;
        super.init();
    }
}
