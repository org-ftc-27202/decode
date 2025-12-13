package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

public abstract class auto_00A_NearFromGoal extends NextFTCOpMode {
    public auto_00A_NearFromGoal() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Intake.INSTANCE),
                new SubsystemComponent(Catapult.INSTANCE),
                new SubsystemComponent(Camera.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Timer opModeTimer;
    private Pose startPose = new Pose(112, 135.5, Math.toRadians(90));
    private Pose getPatternPose = new Pose(92, 92, Math.toRadians(110));
    private Pose Launch1Pose = new Pose(100, 100, Math.toRadians(45));

    private PathChain driveFromStartToGetPattern, driveFromGetPatternToLaunch1;

    public void buildPaths() {
        final double CENTER_X = 72.0;

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            // mirror the x coordinate and heading
            startPose = new Pose(CENTER_X - (startPose.getX() - CENTER_X), startPose.getY(), Math.toRadians(90));
            getPatternPose = new Pose(CENTER_X - (getPatternPose.getX() - CENTER_X), getPatternPose.getY(), Math.toRadians(110));
            Launch1Pose = new Pose(CENTER_X - (Launch1Pose.getX() - CENTER_X), Launch1Pose.getY(), Math.toRadians(45));
        }

        driveFromStartToGetPattern = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .build();

        driveFromGetPatternToLaunch1 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(getPatternPose, Launch1Pose))
                .setLinearHeadingInterpolation(getPatternPose.getHeading(), Launch1Pose.getHeading())
                .build();
    }
    private Command autonomousRoutine() {
        return new SequentialGroup(
                new FollowPath(driveFromStartToGetPattern, true),
                new Delay(0.1),
                Camera.INSTANCE.capturePattern,
                new FollowPath(driveFromGetPatternToLaunch1, true)
//                ,
//                Catapult.INSTANCE.LaunchAllInParallel
        );
    }
    @Override
    public void onInit() {
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        opModeTimer = new Timer();

        buildPaths();
        PedroComponent.follower().setPose(startPose);
    }

    @Override
    public void onStartButtonPressed() {
        opModeTimer.resetTimer();
        autonomousRoutine().schedule();
    }

    @Override
    public void onUpdate() {
        Config.autoEndPose = PedroComponent.follower().getPose();
        telemetry.addData("run #", 1);
        telemetry.addData("alliance", Config.allianceColor.toString());
        telemetry.addData("pattern", Config.pattern.toString());
        telemetry.addData("pos", "x: %.0f | y: %.0f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getX(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake", "%.0f", Intake.INSTANCE.intakeMotor.getPower());
        telemetry.addData("catapults", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("camera (front)", Camera.INSTANCE.flagCaptured);
        telemetry.addData("Timer", "%.2f", opModeTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}