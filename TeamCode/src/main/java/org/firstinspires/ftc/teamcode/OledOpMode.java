package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name="OLED Test")
public class OledOpMode extends LinearOpMode {
    SSD1306 oled;

    @Override
    public void runOpMode() {
        oled = hardwareMap.get(SSD1306.class, "oled");

        waitForStart();

        while (opModeIsActive()) {
            oled.clear();
//            oled.drawString(0, 0, "20");
            oled.drawLargeDigit(0,0,'0',8);
//            oled.drawString(0, 15, "BATT: 20");
//            oled.drawString(0, 30, "STATE: RUNNING");
            oled.display(); // Push to screen

            sleep(100); // Slow down the loop to save I2C bandwidth
        }
    }
}