package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.auto_00_NearFromGoal.patternMatch;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.List;

public abstract class TeleOp_00_base extends LinearOpMode {
    public enum AllianceColors {RED, BLUE};
    private AllianceColors allianceColor = AllianceColors.RED;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    public DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;

    public void setAllianceColor(AllianceColors inAllianceColor) {
        allianceColor = inAllianceColor;
    }
    @Override
    public void runOpMode() {
        TelemetryPacket packet = new TelemetryPacket();

        double speedAdjustment = 1.0;
        double turnSpeedAdjustment = 0.80;
        double max, axial, lateral, yaw;
        double leftFrontPower, rightFrontPower, leftBackPower, rightBackPower;

        // Define and Initialize Drive Motors
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        botIntake intake = new botIntake(hardwareMap);
        botCatapult catapult01 = new botCatapult(hardwareMap, "catapult01");
        botCatapult catapult02 = new botCatapult(hardwareMap, "catapult02");
        botCatapult catapult03 = new botCatapult(hardwareMap, "catapult03");
        botVisionFront visionFront = new botVisionFront(hardwareMap, allianceColor);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

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

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // intake
            if (gamepad1.a) {
                runningActions.add(new SequentialAction(
                        intake.RotateInwards()
                ));
            }
            else if (gamepad1.left_trigger > 0.1) {
                runningActions.add(new SequentialAction(
                        intake.Stop()
                ));
            }
            else if (gamepad1.left_bumper) {
                runningActions.add(new SequentialAction(
                        intake.RotateOutwards()
                ));
            };

            // Catapults
            if (gamepad1.right_trigger > 0) {
                // Launch
                runningActions.add(new SequentialAction(
                        intake.Stop(),
                        new ParallelAction(
                            catapult01.Launch(),
                            catapult02.Launch(),
                            catapult03.Launch())
                ));
            }
            else if (gamepad1.right_bumper) {
                runningActions.add(new SequentialAction(
                        intake.Stop(),
                        new SequentialAction(
                                catapult01.Launch(),
                                catapult02.Launch(),
                                catapult03.Launch())
                ));
            }

            if (gamepad1.x) {
                // Launch
                runningActions.add(new SequentialAction(
                        intake.Stop(),
                        catapult01.Launch()
                ));
            }
            else if (gamepad1.y) {
                // Launch
                runningActions.add(new SequentialAction(
                        intake.Stop(),
                        catapult02.Launch()
                ));
            }
            else if (gamepad1.b) {
                // Launch
                runningActions.add(new SequentialAction(
                        intake.Stop(),
                        catapult03.Launch()
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

            if (allianceColor == AllianceColors.RED) {
                telemetry.addData("alliance: ", "%s", "RED");
            }
            else if (allianceColor == AllianceColors.BLUE) {
                telemetry.addData("alliance: ", "%s", "BLUE");
            }
            telemetry.addData("runningActions.size()", runningActions.size());
            if (patternMatch == botVisionFront.PatternOptions.GPP) {
                telemetry.addData("pattern: ", "%s", "GPP");
            }
            else if (patternMatch == botVisionFront.PatternOptions.PGP) {
                telemetry.addData("pattern: ", "%s", "PGP");
            }
            else if (patternMatch == botVisionFront.PatternOptions.PPG) {
                telemetry.addData("pattern: ", "%s", "PPG");
            }
            telemetry.addData("axial",  "%.2f", axial);
            telemetry.addData("lateral", "%.2f", lateral);
            telemetry.addData("yaw", "%.2f", yaw);
            if (intake.intakeMode == botIntake.intakeModes.INWARDS) {
                telemetry.addData("intake: ", "%s", "Inwards");
            }
            else if (intake.intakeMode == botIntake.intakeModes.OUTWARDS) {
                telemetry.addData("intake: ", "%s", "Outwards");
            }
            else if (intake.intakeMode == botIntake.intakeModes.STOP) {
                telemetry.addData("intake: ", "%s", "Stop");
            }
            telemetry.addData("catapult 01", "%.0f", catapult01.pos);
            telemetry.addData("catapult 02", "%.0f", catapult02.pos);
            telemetry.addData("catapult 03", "%.0f", catapult03.pos);

            telemetry.addData("visionFront status", visionFront.flagStatus);
            telemetry.addData("visionFront block length", visionFront.blockLength);
            telemetry.update();
        }
    }
}