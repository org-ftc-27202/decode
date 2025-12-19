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

public abstract class auto_01_NearFromGoal_base extends NextFTCOpMode {
    public auto_01_NearFromGoal_base() {
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
    private PathChain driveToGetPattern, driveToLaunch1, driveToSpike1Start, driveToSpike1End, driveToLaunch1_2, driveToLeave;
    private Pose startPose;

    public void buildPaths() {
        Pose getPatternPose, launchNear1Pose, spike1StartPose, spike1EndPose, LeavePose;

        startPose = new Pose(112, 128, Math.toRadians(90));
        getPatternPose = new Pose(92, 92, Math.toRadians(110));
        launchNear1Pose = new Pose(94, 82, Math.toRadians(50));
        spike1StartPose = new Pose(100, 78, Math.toRadians(0));
        spike1EndPose = new Pose(130, 78, Math.toRadians(0));
        LeavePose = new Pose(100, 60, Math.toRadians(0));

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            startPose = startPose.mirror();
            getPatternPose = getPatternPose.mirror();
            launchNear1Pose = launchNear1Pose.mirror();
            spike1StartPose = spike1StartPose.mirror();
            spike1EndPose = spike1EndPose.mirror();
            LeavePose = LeavePose.mirror();
        }

        driveToGetPattern = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .build();

        driveToLaunch1 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(getPatternPose, launchNear1Pose))
                .setLinearHeadingInterpolation(getPatternPose.getHeading(), launchNear1Pose.getHeading())
                .build();

        driveToSpike1Start = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(launchNear1Pose, spike1StartPose))
                .setLinearHeadingInterpolation(launchNear1Pose.getHeading(), spike1StartPose.getHeading())
                .build();

        driveToSpike1End = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike1StartPose, spike1EndPose))
                .setLinearHeadingInterpolation(spike1StartPose.getHeading(), spike1EndPose.getHeading())
                .build();

        driveToLaunch1_2 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike1EndPose, launchNear1Pose))
                .setLinearHeadingInterpolation(spike1EndPose.getHeading(), launchNear1Pose.getHeading())
                .build();

        driveToLeave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(launchNear1Pose, LeavePose))
                .setLinearHeadingInterpolation(launchNear1Pose.getHeading(), LeavePose.getHeading())
                .build();
    }
    private Command autonomousRoutine() {
        return new SequentialGroup(
                new FollowPath(driveToGetPattern),
                new Delay(0.1),
                Camera.INSTANCE.capturePattern,
                new FollowPath(driveToLaunch1, true),
                Catapult.INSTANCE.LaunchByPattern,
                new FollowPath(driveToSpike1Start),
                Intake.INSTANCE.Inwards,
                new FollowPath(driveToSpike1End),
                new FollowPath(driveToLaunch1_2, true),
                Intake.INSTANCE.Stop,
                Catapult.INSTANCE.LaunchByPattern,
                new FollowPath(driveToLeave, true)
        );
    }
    @Override
    public void onInit() {
        opModeTimer = new Timer();
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
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
        telemetry.addData("pattern", Config.motifPattern.toString());
        telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake (power)", "%.0f", Intake.INSTANCE.getPower());
        telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
        telemetry.addData("Timer", "%.1f", opModeTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}