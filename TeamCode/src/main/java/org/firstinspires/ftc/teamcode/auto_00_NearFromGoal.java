package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class auto_00_NearFromGoal extends LinearOpMode {
    public enum AllianceColors {RED, BLUE};
    private TeleOp_00_base.AllianceColors allianceColor = TeleOp_00_base.AllianceColors.RED;
    public static botVisionFront.PatternOptions patternMatch = botVisionFront.PatternOptions.GPP;
    private TrajectoryActionBuilder trajDriveToSeePatternPositionNear, trajDriveToLaunchPositionNear, trajDriveToSpike1_Start, trajDriveToSpike1_End;

    private Pose2d initialPose = null;

    public void setAllianceColor(TeleOp_00_base.AllianceColors inAllianceColor) {
        allianceColor = inAllianceColor;
    }

    @Override
    public void runOpMode() {
        TelemetryPacket packet = new TelemetryPacket();

        if (allianceColor == TeleOp_00_base.AllianceColors.RED) {
            initialPose = new Pose2d(39, 61, Math.toRadians(90));
        }
        else if (allianceColor == TeleOp_00_base.AllianceColors.BLUE) {
            // ToDo
            initialPose = new Pose2d(-14, 61, Math.toRadians(90));
        }

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        botIntake intake = new botIntake(hardwareMap);
        botCatapult catapult01 = new botCatapult(hardwareMap, "catapult01");
        botCatapult catapult02 = new botCatapult(hardwareMap, "catapult02");
        botCatapult catapult03 = new botCatapult(hardwareMap, "catapult03");
        botVisionFront visionFront = new botVisionFront(hardwareMap, allianceColor);

        if (allianceColor == TeleOp_00_base.AllianceColors.RED) {
            trajDriveToSeePatternPositionNear = drive.actionBuilder(initialPose)
                    .strafeToConstantHeading(new Vector2d(8, 36));
            trajDriveToLaunchPositionNear = trajDriveToSeePatternPositionNear.endTrajectory().fresh()
                    .strafeToSplineHeading(new Vector2d(30, 34), Math.toRadians(40));
            trajDriveToSpike1_Start = trajDriveToLaunchPositionNear.endTrajectory().fresh()
                    .strafeToSplineHeading(new Vector2d(24, 11), Math.toRadians(0));
            trajDriveToSpike1_End = trajDriveToSpike1_Start.endTrajectory().fresh()
                    .strafeToConstantHeading(new Vector2d(54, 11));
        }
//        else if (allianceColor == TeleOp_00_base.AllianceColors.BLUE) {
//            // ToDo
//        }

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.update();
        }

        Action actDriveToSeePatternNear = trajDriveToSeePatternPositionNear.build();
        Action actDriveToLaunchPositionNear = trajDriveToLaunchPositionNear.build();
        Action actDriveToSpike1_Start = trajDriveToSpike1_Start.build();
        Action actDriveToSpike1_End = trajDriveToSpike1_End.build();

        waitForStart();

        this.resetRuntime();

        if (isStopRequested()) return;

        Actions.runBlocking(
            new SequentialAction(
                    actDriveToSeePatternNear,
                    visionFront.capturePattern(),
                    actDriveToLaunchPositionNear,
//                    new ParallelAction(
//                            catapult01.Launch(),
//                            catapult02.Launch(),
//                            catapult03.Launch()),
                    new ParallelAction(
                            actDriveToSpike1_Start,
                            intake.RotateInwards()),
                    actDriveToSpike1_End,
                    new SleepAction(3)
        ));

        patternMatch = visionFront.pattern;

        telemetry.addData("Duration", this.getRuntime());
        if (patternMatch == botVisionFront.PatternOptions.GPP) {
            telemetry.addData("pattern: ", "%s", "GPP");
        }
        else if (patternMatch == botVisionFront.PatternOptions.PGP) {
            telemetry.addData("pattern: ", "%s", "PGP");
        }
        else if (patternMatch == botVisionFront.PatternOptions.PPG) {
            telemetry.addData("pattern: ", "%s", "PPG");
        }
        telemetry.update();

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(5)
                        ));
    }
}