package org.firstinspires.ftc.robotcontroller;


import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.MotorEx;

@Autonomous(name = "Test auto")
public class TestAuto extends NextFTCOpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public TestAuto() {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    private final Pose startPose = new Pose(144, 72, Math.toRadians(0));
    private final Pose pickup1Pose = new Pose(96, 36 , Math.toRadians(90));
    Path BaseMovePath;
    FollowPath baseMove;
    private PathChain grabPickup1, backStart1;

    public void buildPaths() {
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, pickup1Pose))
                .setLinearHeadingInterpolation(startPose.getHeading(), pickup1Pose.getHeading())
                .build();

        backStart1 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, startPose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), startPose.getHeading())
                .build();
    }

    @Override
    public void onUpdate() {
        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
    @Override
    public void onStartButtonPressed() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
    }
}