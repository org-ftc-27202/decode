package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

public abstract class TeleOp_01_base extends NextFTCOpMode {
    public TeleOp_01_base() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Intake.INSTANCE, Catapult.INSTANCE, Camera.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Timer opModeTimer;
    private Pose relocalizePose;
    private PathChain driveToBaseEndGame, driveToLoadingZone, driveToGate, driveToLaunch1Pose, driveToLaunch2Pose;
    public void buildPaths() {
        Pose baseEndGamePose, loadingZonePose, gatePose, launch1Pose, launch2Pose;
        // Robot Length: 17"; Robot Width: 17.5"
        // Base on Red Alliance
        relocalizePose = new Pose(8.75, 8.5, Math.toRadians(0));
        baseEndGamePose = new Pose(38, 32, Math.toRadians(0));
        loadingZonePose = new Pose(20, 26, Math.toRadians(45));
        gatePose = new Pose(128, 66, Math.toRadians(0));
        if (Config.goalOption == Config.GoalOptions.FAR) {
            launch1Pose = new Pose(65.5, 26.5, Math.toRadians(58));
            launch2Pose = new Pose(87, 18, Math.toRadians(67));  // works for TeleOp and auto
        } else { // NEAR
            launch1Pose = new Pose(92, 80, Math.toRadians(50));  // works for TeleOp and auto
            launch2Pose = new Pose(76, 114, Math.toRadians(12));
        }

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            relocalizePose = relocalizePose.mirror();
            baseEndGamePose = baseEndGamePose.mirror();
            loadingZonePose = loadingZonePose.mirror();
            launch1Pose = launch1Pose.mirror();
            launch2Pose = launch2Pose.mirror();
            gatePose = gatePose.mirror();
        }

        driveToBaseEndGame = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), baseEndGamePose))
                .setConstantHeadingInterpolation(baseEndGamePose.getHeading())
                .build();

        driveToGate = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), gatePose))
                .setConstantHeadingInterpolation(gatePose.getHeading())
                .build();

        driveToLoadingZone = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), loadingZonePose))
                .setConstantHeadingInterpolation(loadingZonePose.getHeading())
                .build();

        driveToLaunch1Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), launch1Pose))
                .setConstantHeadingInterpolation(launch1Pose.getHeading())
                .build();

        driveToLaunch2Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), launch2Pose))
                .setConstantHeadingInterpolation(launch2Pose.getHeading())
                .build();
    }
    @Override
    public void onInit() {
        opModeTimer = new Timer();
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        opModeTimer.resetTimer();
        DriverControlledCommand driverControlled = new PedroDriverControlled(
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX().negate(),
            Gamepads.gamepad1().rightStickX().negate().map(x -> x * 0.5));  // Scalar to reduce turn power
        PedroComponent.follower().setStartingPose(Config.autoEndPose == null ? new Pose() : Config.autoEndPose);
        driverControlled.schedule();

        // Intake
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Inwards,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Outwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);

        // Catapults
        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch01),
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch02),
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch03),
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
               new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                        Catapult.INSTANCE.LaunchInParallel,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                        Catapult.INSTANCE.LaunchByPattern,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));

        // Manual Settings
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().setPose(relocalizePose)));
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().y()).whenBecomesTrue(Camera.INSTANCE.capturePattern);

        // Auto Mode
        // Cancel automated driving and restart back to TeleOp drive
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().startTeleopDrive()));
        // Drive to Loading Zone
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().a()).whenBecomesTrue(new FollowPath(driveToLoadingZone, true, 1.0));
        // Drive to Launch 1
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().b()).whenBecomesTrue(new FollowPath(driveToLaunch1Pose, true, 1.0));
        // Drive to Launch 2
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().y()).whenBecomesTrue(new FollowPath(driveToLaunch2Pose, true, 1.0));
        // Drive to Gate
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().a()).whenBecomesTrue(new FollowPath(driveToGate,true, 1.0));
        // Drive to Base (parking)
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().b()).whenBecomesTrue(new FollowPath(driveToBaseEndGame,true, 1.0));
    }
    @Override
    public void onUpdate() {
        telemetry.addData("run #", 1);
        telemetry.addData("alliance", Config.allianceColor.toString());
        telemetry.addData("pattern", Config.motifPattern.toString());
        telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake (power)", "%.0f", Intake.INSTANCE.getPower());
        telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
        telemetry.addData("Timer", "%.1f", opModeTimer.getElapsedTimeSeconds());
        telemetry.addLine("--------------------");
        telemetry.addLine("Intake: leftBumper=In/Off leftTrigger=Out/Off");
        telemetry.addLine("Drive To: a=Load b=Launch1 y=Launch2 x=Cancel");
        telemetry.addLine("dpadDown+: a=Gate b=End x=Relocalize y=Motif");
        telemetry.addLine("Launch: rightBumper=Parallel rightTrigger=Pattern");
        telemetry.addLine("Launch: dpadLeft=01 dpadUp=02 dpadRight=03");
        telemetry.update();
    }
}
