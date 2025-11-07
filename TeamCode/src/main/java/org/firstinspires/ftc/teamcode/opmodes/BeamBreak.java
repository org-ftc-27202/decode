package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "Sensor: Beam Break", group = "Sensor")

public class BeamBreak extends LinearOpMode {
    DigitalChannel digitalTouch;

    @Override
    public void runOpMode() {

        // get a reference to our touchSensor object.
        digitalTouch = hardwareMap.get(DigitalChannel.class, "beamBreak");

        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        telemetry.addData("Beam Break", "Press START to continue...");
        telemetry.update();

        // wait for the start button to be pressed.
        waitForStart();

        // while the OpMode is active, loop and read the digital channel.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // button is pressed if value returned is LOW or false.
            // send the info back to driver station using telemetry function.
            if (!digitalTouch.getState()) {
                telemetry.addData("Beam", "Broken (v4)");
            } else {
                telemetry.addData("Beam", "Not Broken (v4)");
            }

            telemetry.update();
        }
    }
}