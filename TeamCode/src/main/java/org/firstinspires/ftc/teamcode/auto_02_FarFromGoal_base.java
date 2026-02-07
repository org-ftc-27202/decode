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

public abstract class auto_02_FarFromGoal_base extends NextFTCOpMode {
    public auto_02_FarFromGoal_base() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Intake.INSTANCE, Catapult.INSTANCE, Camera.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Timer opModeTimer;
    private boolean telemetryOnFlag;
    private PathChain driveToGetPattern, driveToLeave;
    private Pose startPose;
    public void buildPaths() {
        Pose getPatternPose, LeavePose;

        startPose = new Pose(86, 6, Math.toRadians(90));
        getPatternPose = new Pose(88, 54, Math.toRadians(95));
        LeavePose = new Pose(106, 8, Math.toRadians(90));

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            startPose = startPose.mirror();
            getPatternPose = getPatternPose.mirror();
            LeavePose = LeavePose.mirror();
        }

        driveToGetPattern = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .build();

        driveToLeave = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(getPatternPose, LeavePose))
                .setLinearHeadingInterpolation(getPatternPose.getHeading(), LeavePose.getHeading())
                .build();
    }
    private Command autonomousRoutine() {
        return new SequentialGroup(
                new FollowPath(driveToGetPattern),
                new Delay(0.1),
                Camera.INSTANCE.capturePattern,
                new FollowPath(driveToLeave, true)
        );
    }
    @Override
    public void onInit() {
        opModeTimer = new Timer();
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        buildPaths();
        PedroComponent.follower().setPose(startPose);

//        telemetryOnFlag = true;
        telemetryOnFlag = false;
    }

    @Override
    public void onStartButtonPressed() {
        opModeTimer.resetTimer();
        autonomousRoutine().schedule();
    }

    @Override
    public void onUpdate() {
        Config.autoEndPose = PedroComponent.follower().getPose();

        if (telemetryOnFlag) {
            telemetry.addData("run #", 1);
            telemetry.addData("alliance", Config.allianceColor.toString());
            telemetry.addData("pattern", Config.motifPattern.toString());
            telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
            telemetry.addData("intake (power)", "%.0f", Intake.INSTANCE.getPower());
            telemetry.addData("balls", "%d", Intake.INSTANCE.ballCounter);
            telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
            telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
            telemetry.addData("Timer", "%.1f", opModeTimer.getElapsedTimeSeconds());
            telemetry.update();
        }
    }
}