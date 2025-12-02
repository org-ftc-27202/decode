package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;

@TeleOp(name = "Bad Apple", group = "Robot")
public final class BadApple extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        BootScreen bootScreen = new BootScreen(telemetry, new org.firstinspires.ftc.teamcode.util.bootscreen.BadApple(), true);
        while (true) {
            bootScreen.updateBootScreen();
            sleep(bootScreen.getMillisBetweenFrames()); // around 3 fps
            telemetry.update();

            //waitForStart();

            if (isStopRequested()) return;
        }
    }
}
