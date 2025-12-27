package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Robot Controller Cycle Time")

public class RobotControllerCycleTime extends LinearOpMode {
    @Override
    public void runOpMode() {
        long lastCycleTime = System.currentTimeMillis();
        // wait for the onStart button to be pressed.
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine(
                    String.format("Cycle Time: %d", System.currentTimeMillis() - lastCycleTime)
            );
            lastCycleTime = System.currentTimeMillis();
            telemetry.update();
        }
    }
}