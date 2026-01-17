package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
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
                new SubsystemComponent(Intake.INSTANCE, Catapult.INSTANCE, Camera.INSTANCE, Wiper.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Timer opModeTimer;
    private PathChain driveToGetPattern, driveToLaunch0,
            driveToSpike1Start, driveToSpike1End, driveToLaunch1,
            driveToSpike2Start, driveToSpike2End, driveToLaunch2,
            driveToSpike3Start, driveToSpike3End, driveToLaunch3,
            driveToLeave
            ;
    private Pose startPose;

    public void buildPaths() {
        Pose getPatternPose, launchNear1Pose, spike1StartPose, spike1EndPose, LeavePose, spike2StartPose, spike2EndPose, spike3StartPose, spike3EndPose;

        startPose = new Pose(113, 129.5, Math.toRadians(90));
        getPatternPose = new Pose(92, 92, Math.toRadians(110));
        launchNear1Pose = new Pose(100, 88, Math.toRadians(52));

        spike1StartPose = new Pose(100, 80, Math.toRadians(0));
        spike1EndPose = new Pose(130, 72, Math.toRadians(0));
        spike2StartPose = new Pose(100, 56, Math.toRadians(0));
        spike2EndPose = new Pose(136,56, Math.toRadians(0));
        spike3StartPose = new Pose(100, 32, Math.toRadians(0));
        spike3EndPose = new Pose(136,32, Math.toRadians(0));

        LeavePose = new Pose(120, 66, Math.toRadians(0));  // Near Gate to Open for TeleOp

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            startPose = startPose.mirror();
            getPatternPose = getPatternPose.mirror();
            launchNear1Pose = launchNear1Pose.mirror();
            spike1StartPose = spike1StartPose.mirror();
            spike1EndPose = spike1EndPose.mirror();
            spike2StartPose = spike2StartPose.mirror();
            spike2EndPose = spike2EndPose.mirror();
            spike3StartPose = spike3StartPose.mirror();
            spike3EndPose = spike3EndPose.mirror();
            LeavePose = LeavePose.mirror();
        }

        driveToGetPattern = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .build();

        driveToLaunch0 = PedroComponent.follower().pathBuilder()
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

        driveToLaunch1 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike1EndPose, launchNear1Pose))
                .setLinearHeadingInterpolation(spike1EndPose.getHeading(), launchNear1Pose.getHeading())
                .build();

        driveToSpike2Start = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(launchNear1Pose, spike2StartPose))
                .setLinearHeadingInterpolation(launchNear1Pose.getHeading(), spike2StartPose.getHeading())
                .build();

        driveToSpike2End = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike2StartPose, spike2EndPose))
                .setLinearHeadingInterpolation(spike2StartPose.getHeading(), spike2EndPose.getHeading())
                .build();

        driveToLaunch2 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike2EndPose, launchNear1Pose))
                .setLinearHeadingInterpolation(spike2EndPose.getHeading(), launchNear1Pose.getHeading())
                .build();

        driveToSpike3Start = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(launchNear1Pose, spike3StartPose))
                .setLinearHeadingInterpolation(launchNear1Pose.getHeading(), spike3StartPose.getHeading())
                .build();

        driveToSpike3End = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike3StartPose, spike3EndPose))
                .setLinearHeadingInterpolation(spike3StartPose.getHeading(), spike3EndPose.getHeading())
                .build();

        driveToLaunch3 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(spike3EndPose, launchNear1Pose))
                .setLinearHeadingInterpolation(spike3EndPose.getHeading(), launchNear1Pose.getHeading())
                .build();

        driveToLeave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(launchNear1Pose, LeavePose))
                .setLinearHeadingInterpolation(launchNear1Pose.getHeading(), LeavePose.getHeading())
                .build();
    }
    private Command autonomousRoutine() {
        return new SequentialGroup(
                // Get Motif and Launch Preloads
                Wiper.INSTANCE.toLaunchPosition,
                new FollowPath(driveToGetPattern),
                new Delay(0.1),
                Camera.INSTANCE.capturePattern,
                new FollowPath(driveToLaunch0, true),
                new Delay(0.75),
                Catapult.INSTANCE.LaunchInParallel,

                // Grab balls and launch #1
                new FollowPath(driveToSpike1Start),
                Wiper.INSTANCE.toIntakePosition,
                Intake.INSTANCE.Inwards,
                new FollowPath(driveToSpike1End),
                new ParallelGroup(
                    new SequentialGroup(new Delay(1.0), Wiper.INSTANCE.toLaunchPosition),
                    new FollowPath(driveToLaunch1, true)),
                Intake.INSTANCE.Stop,
                Catapult.INSTANCE.LaunchByPattern,

                // Grab balls and launch #2
                Wiper.INSTANCE.toIntakePosition,
                Intake.INSTANCE.Inwards,
                new FollowPath(driveToSpike2Start, true),
                new FollowPath(driveToSpike2End),
                new ParallelGroup(
                        new SequentialGroup(new Delay(1.0), Wiper.INSTANCE.toLaunchPosition),
                        new FollowPath(driveToLaunch2, true)),
                Intake.INSTANCE.Stop,
                Catapult.INSTANCE.LaunchByPattern,

                // Grab balls and launch #3
                Wiper.INSTANCE.toIntakePosition,
                Intake.INSTANCE.Inwards,
                new FollowPath(driveToSpike3Start, true),
                new FollowPath(driveToSpike3End),
                new ParallelGroup(
                        new SequentialGroup(new Delay(1.0), Wiper.INSTANCE.toLaunchPosition),
                        new FollowPath(driveToLaunch3, true)),
                Intake.INSTANCE.Stop,
                Catapult.INSTANCE.LaunchByPattern,

                // Go to Leave Pose
                new FollowPath(driveToLeave)
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