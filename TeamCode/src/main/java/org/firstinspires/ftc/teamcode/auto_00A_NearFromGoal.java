package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
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
    private PathChain driveFromStartToGetPattern, driveFromGetPatternToLaunch1;
    private Pose startPose, getPatternPose, launchNear1Pose;

    public void buildPaths() {
        startPose = new Pose(112, 135.5, Math.toRadians(90));
        getPatternPose = new Pose(92, 92, Math.toRadians(110));
        launchNear1Pose = new Pose(92, 102, Math.toRadians(42));

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            startPose = startPose.mirror();
            getPatternPose = getPatternPose.mirror();
            launchNear1Pose = launchNear1Pose.mirror();
        }

        driveFromStartToGetPattern = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .build();

        driveFromGetPatternToLaunch1 = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(getPatternPose, launchNear1Pose))
                .setLinearHeadingInterpolation(getPatternPose.getHeading(), launchNear1Pose.getHeading())
                .build();
    }
    private Command autonomousRoutine() {
        return new SequentialGroup(
                new FollowPath(driveFromStartToGetPattern, true),
                new Delay(0.1),
                Camera.INSTANCE.capturePattern,
                new FollowPath(driveFromGetPatternToLaunch1, true),
                Catapult.INSTANCE.LaunchInParallel
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
        telemetry.addData("intake", "%.0f", Intake.INSTANCE.intakeMotor.getPower());
        telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
        telemetry.addData("Timer", "%.2f", opModeTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}