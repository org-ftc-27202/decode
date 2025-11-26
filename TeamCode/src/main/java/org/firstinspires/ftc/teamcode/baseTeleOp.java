package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import java.util.ArrayList;
import java.util.List;

public abstract class baseTeleOp extends LinearOpMode {
    private String allianceColor;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    public DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;

    private DigitalChannel switchDown02;
    public void setAllianceColor(String inAllianceColor) {
        allianceColor = inAllianceColor;
    }
    @Override
    public void runOpMode() {
        TelemetryPacket packet = new TelemetryPacket();

        double speedAdjustment = 0.60;
        double turnSpeedAdjustment = 0.40;
        double max, axial, lateral, yaw;
        double leftFrontPower, rightFrontPower, leftBackPower, rightBackPower;

        // Define and Initialize Drive Motors
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorEx.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorEx.Direction.FORWARD);

        leftFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        botIntake intake = new botIntake(hardwareMap);
//        botHolder holder02 = new botHolder(hardwareMap, "holder02");
        botCatapult catapult02 = new botCatapult(hardwareMap, "catapult02", "switchUp02");
        switchDown02 = hardwareMap.get(DigitalChannel.class, "switchDown02");
        switchDown02.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

//        runningActions.add(new SequentialAction(
//                holder02.ReleasePosition()
//        ));
//
        while (opModeIsActive()) {
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            axial = -gamepad1.left_stick_y * speedAdjustment;
            lateral = gamepad1.left_stick_x * speedAdjustment;
            yaw = (gamepad1.right_stick_x * turnSpeedAdjustment);

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            leftFrontPower = axial + lateral + yaw;
            rightFrontPower = axial - lateral - yaw;
            leftBackPower = axial - lateral + yaw;
            rightBackPower = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            // intake
            if (gamepad1.x) {
                runningActions.add(new SequentialAction(
                        intake.RotateInwards()
                ));
            } else if (gamepad1.b) {
                runningActions.add(new SequentialAction(
                        intake.RotateOutwards()
                ));
            } else if (gamepad1.y) {
                runningActions.add(new SequentialAction(
                        intake.Stop()
                ));
            };

            if (gamepad1.a) {
                // Launch
                runningActions.add(new SequentialAction(
//                        catapult02.PreLaunch(),
//                        new SleepAction(0.1),
                        catapult02.Launch()
                ));
            }

            if (!switchDown02.getState()) {
                runningActions.add(new SequentialAction(
                        catapult02.StopCatapult()
                ));
            }

            // update running actions
            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }
            runningActions = newActions;

            dash.sendTelemetryPacket(packet);

 //            telemetry.addData("packet", packet);
            telemetry.addData("runningActions.size()", runningActions.size());
            telemetry.addData("axial",  "%.2f", axial);
            telemetry.addData("lateral", "%.2f", lateral);
            telemetry.addData("yaw", "%.2f", yaw);
            telemetry.addData("catapult02",  "%.2f", catapult02.getPower());
//            telemetry.addData("holder02",  "%.2f", holder02.getPosition());
            if (switchDown02.getState()) {
                telemetry.addData("switchDown02", "Off");
            } else {
                telemetry.addData("switchDown02", "On");
            }
//            if (switchUp02.getState()) {
//                telemetry.addData("switchUp02", "Off");
//            } else {
//                telemetry.addData("switchUp02", "On");
//            }
            telemetry.update();
        }
    }
}