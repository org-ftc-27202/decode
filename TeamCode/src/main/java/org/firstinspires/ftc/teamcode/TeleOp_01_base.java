package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import dev.nextftc.core.commands.delays.Delay;
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
                new SubsystemComponent(Intake.INSTANCE, Catapult.INSTANCE, Camera.INSTANCE, Wiper.INSTANCE, IntakeStopper.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Timer opModeTimer;
    private Pose relocalizePose;
    private PathChain driveToBaseEndGame, driveToBase2EndGame, driveToLoadingZone, driveToGate, driveToLaunch1Pose;
    public void buildPaths() {
        Pose baseEndGamePose, base2EndGamePose, loadingZonePose, gatePose, launch1Pose;
        // Robot Length: 17"; Robot Width: 17.5"
        // Base on Red Alliance
        relocalizePose = new Pose(8.75, 8.5, Math.toRadians(0));
        loadingZonePose = new Pose(21, 30, Math.toRadians(45));
        baseEndGamePose = new Pose(38, 32, Math.toRadians(0));
        base2EndGamePose = new Pose(38, 50, Math.toRadians(0));
        gatePose = new Pose(128, 66, Math.toRadians(0));
        if (Config.goalOption == Config.GoalOptions.FAR) {
            launch1Pose = new Pose(65.5, 26.5, Math.toRadians(58));
        } else { // NEAR
            launch1Pose = new Pose(100, 88, Math.toRadians(52));  // works for TeleOp and auto
        }

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            relocalizePose = relocalizePose.mirror();
            baseEndGamePose = baseEndGamePose.mirror();
            base2EndGamePose = base2EndGamePose.mirror();
            loadingZonePose = loadingZonePose.mirror();
            launch1Pose = launch1Pose.mirror();
            gatePose = gatePose.mirror();
        }

        driveToBaseEndGame = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), baseEndGamePose, baseEndGamePose))
                .setConstantHeadingInterpolation(baseEndGamePose.getHeading())
                .build();

        driveToBase2EndGame = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), base2EndGamePose, base2EndGamePose))
                .setConstantHeadingInterpolation(base2EndGamePose.getHeading())
                .build();

        driveToGate = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), gatePose, gatePose))
                .setConstantHeadingInterpolation(gatePose.getHeading())
                .build();

        driveToLoadingZone = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), loadingZonePose, loadingZonePose))
                .setConstantHeadingInterpolation(loadingZonePose.getHeading())
                .build();

        driveToLaunch1Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), launch1Pose, launch1Pose))
                .setConstantHeadingInterpolation(launch1Pose.getHeading())
                .build();
    }
    @Override
    public void onInit() {
        opModeTimer = new Timer();
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        IntakeStopper.INSTANCE.mapIntakeStopperHardware(hardwareMap);
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
                new ParallelGroup(
                        Wiper.INSTANCE.toIntakePosition,
                        Intake.INSTANCE.Inwards,
                        IntakeStopper.INSTANCE.toOpenPosition,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(
                new ParallelGroup(
                        Intake.INSTANCE.Outwards,
                        IntakeStopper.INSTANCE.toOpenPosition));
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);

        // Wiper
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                new ParallelGroup(
                        Intake.INSTANCE.Stop,
                        Wiper.INSTANCE.toLaunchPosition));
        // Catapults
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
               new SequentialGroup(
                        new ParallelGroup(Wiper.INSTANCE.toLaunchPosition,
                                Intake.INSTANCE.Stop,
                                Camera.INSTANCE.getCatapultArtifactColors,
                                IntakeStopper.INSTANCE.initIntakeStopper),
                        Catapult.INSTANCE.LaunchInParallel,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Wiper.INSTANCE.toLaunchPosition,
                                Intake.INSTANCE.Stop,
                                Camera.INSTANCE.getCatapultArtifactColors,
                                IntakeStopper.INSTANCE.initIntakeStopper),
                        Catapult.INSTANCE.LaunchByPattern,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));

        // Intake Stopper
        Gamepads.gamepad1().b().whenBecomesTrue(IntakeStopper.INSTANCE.ballCountTo2);

        // Motif
//        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().setPose(relocalizePose)));
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().y()).whenBecomesTrue(Camera.INSTANCE.capturePattern);

        // Auto-Drives
        // Cancel automated driving and restart back to TeleOp drive
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().x()).whenBecomesTrue(
                new ParallelGroup(
                        Intake.INSTANCE.Stop,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
//        // Drive to Loading Zone
//        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().a()).whenBecomesTrue(
//                new ParallelGroup(
//                        Wiper.INSTANCE.toLaunchPosition,
//                        Intake.INSTANCE.Stop,
//                        new FollowPath(driveToLoadingZone, true, 1.0)));

        // Move the bot center towards goal
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().a()).whenBecomesTrue(
                new SequentialGroup(
                        Camera.INSTANCE.captureGoalPosition,
                        new InstantCommand(() -> PedroComponent.follower().turn(Math.toRadians(Config.deltaToCenterAngleInDeg), false))));
        // Drive to Launch 1
//        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().b()).whenBecomesTrue(
//                new SequentialGroup(
//                    new ParallelGroup(
//                            new SequentialGroup(new Delay(1.0), Wiper.INSTANCE.toLaunchPosition, Intake.INSTANCE.Stop),
//                            new FollowPath(driveToLaunch1Pose, true, 1.0)),
//                    new InstantCommand(() -> PedroComponent.follower().turn(Math.toRadians(Config.deltaToCenterAngleInDeg), false))));
//        // Drive to driveToGate
//        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().y()).whenBecomesTrue(
//                new FollowPath(driveToGate, true, 1.0));
//        // Drive to Base 1 (parking)
//        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
//                new ParallelGroup(
//                        Intake.INSTANCE.Stop,
//                        new FollowPath(driveToBaseEndGame, true, 1.0)));
//        // Drive to Base 2 (parking)
//        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
//                new ParallelGroup(
//                        Intake.INSTANCE.Stop,
//                        new FollowPath(driveToBase2EndGame, true, 1.0)));
    }
    @Override
    public void onUpdate() {
        IntakeStopper.INSTANCE.CountBalls();

        telemetry.addData("run #", 1);
        telemetry.addData("alliance", Config.allianceColor.toString());
        telemetry.addData("pattern", Config.motifPattern.toString());
        telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake (power)", "%.0f", Intake.INSTANCE.getPower());
        telemetry.addData("balls", "%d", IntakeStopper.INSTANCE.ballCounter);
        telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
        telemetry.addData("goal", "cx: %.0f | cy: %.0f | cd: %.0f", Config.deltaToCenterX, Config.deltaToCenterY, Config.deltaToCenterAngleInDeg);
        telemetry.addData("Timer", "%.1f", opModeTimer.getElapsedTimeSeconds());
        telemetry.addLine("--------------------");
        telemetry.addLine("Intake: leftBumper=In/Off leftTrigger=Out/Off");
        telemetry.addLine("Wiper: dpadRight=Launch Pos");
        telemetry.addLine("Auto-Drive: a=Center to Goal x=Cancel");
        telemetry.addLine("Launch: rightBumper=Parallel rightTrigger=Pattern");
        telemetry.addLine("b=Ball Count to 2 (Open IntakeStopper)");
        telemetry.addLine("dpadDown+: y=Motif");
        telemetry.update();
    }
}
