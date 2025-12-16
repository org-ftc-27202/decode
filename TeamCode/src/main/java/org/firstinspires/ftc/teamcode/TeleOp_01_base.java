package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
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
    private Pose relocalizePose;
    private PathChain driveToBaseEndGame, driveToLoadingZone, driveToGate, driveToLaunch1Pose, driveToLaunch2Pose;
    public void buildPaths() {
        Pose baseEndGamePose, loadingZonePose, loadingZoneControlPose, gatePose, gateControlPose, launch1Pose, launch1ControlPose, launch2Pose, launch2ControlPose;
        // Robot Length: 17"; Robot Width: 17.5"
        // Base on Red Alliance
        relocalizePose = new Pose(8.75, 8.5, Math.toRadians(0));
        baseEndGamePose = new Pose(38, 32, Math.toRadians(0));
        loadingZonePose = new Pose(20, 26, Math.toRadians(45));
        loadingZoneControlPose = new Pose(20, 36, Math.toRadians(45));
        if (Config.goalOption == Config.GoalOptions.FAR) {
            launch1Pose = new Pose(68, 30, Math.toRadians(57));
            launch1ControlPose = new Pose(60, 30, Math.toRadians(57));
            launch2Pose = new Pose(86, 18, Math.toRadians(67));  // works for TeleOp and auto
            launch2ControlPose = new Pose(72, 28, Math.toRadians(67));
        } else { // NEAR
            launch1Pose = new Pose(86, 74, Math.toRadians(48));  // works for TeleOp and auto
            launch1ControlPose = launch1Pose;
            launch2Pose = new Pose(72, 102, Math.toRadians(30));
            launch2ControlPose = launch2Pose;
        }

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            gatePose = new Pose(16, 80, Math.toRadians(180));
            gateControlPose = new Pose(44, 72, Math.toRadians(180));
            relocalizePose = relocalizePose.mirror();
            baseEndGamePose = baseEndGamePose.mirror();
            loadingZonePose = loadingZonePose.mirror();
            loadingZoneControlPose =loadingZoneControlPose.mirror();
            launch1Pose = launch1Pose.mirror();
            launch1ControlPose = launch1ControlPose.mirror();
            launch2Pose = launch2Pose.mirror();
            launch2ControlPose = launch2ControlPose.mirror();
        }  else {  // RED
            gatePose = new Pose(128, 62, Math.toRadians(0));
            gateControlPose= new Pose(100, 72, Math.toRadians(0));
        }
        driveToBaseEndGame = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), baseEndGamePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), baseEndGamePose.getHeading())
                .build();

        driveToGate = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), gateControlPose, gatePose))
                .setLinearHeadingInterpolation(gateControlPose.getHeading(), gatePose.getHeading())
                .build();

        driveToLoadingZone = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), loadingZoneControlPose, loadingZonePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), loadingZonePose.getHeading())
                .build();

        driveToLaunch1Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), launch1ControlPose, launch1Pose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), launch1Pose.getHeading())
                .build();

        driveToLaunch2Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), launch2ControlPose, launch2Pose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), launch2Pose.getHeading())
                .build();
    }
    @Override
    public void onInit() {
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX().negate(),
            Gamepads.gamepad1().rightStickX().negate().map(x -> x * 0.5));  // Scalar to reduce turn power
        PedroComponent.follower().setStartingPose(Config.autoEndPose == null ? new Pose() : Config.autoEndPose);
        driverControlled.schedule();

        // Intake
        Gamepads.gamepad1().leftBumper().toggleOnBecomesTrue().whenBecomesTrue(Intake.INSTANCE.Inwards);
        Gamepads.gamepad1().leftBumper().toggleOnBecomesFalse().whenBecomesTrue(Intake.INSTANCE.Stop);
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Outwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);

        // Catapults
        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch01));
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch02));
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch03));
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
               new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                        Catapult.INSTANCE.LaunchInParallel));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                        Catapult.INSTANCE.LaunchByPattern));

        // Manual Settings
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().setPose(relocalizePose)));
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().y()).whenBecomesTrue(Camera.INSTANCE.capturePattern);

        // Auto-Drive
        // Cancel automated driving and restart back to TeleOp drive
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().startTeleopDrive()));
        // Drive to Loading Zone
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().a()).whenBecomesTrue(new FollowPath(driveToLoadingZone,true, 1.0));
        // Drive to Launch 1
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().b()).whenBecomesTrue(new FollowPath(driveToLaunch1Pose,true, 1.0));
        // Drive to Gate
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().y()).whenBecomesTrue(new FollowPath(driveToLaunch2Pose,true, 1.0));
        // Drive to Base (parking)
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
        telemetry.addLine("Intake: leftBumper=In/Off leftTrigger=Out/Off");
        telemetry.addLine("Launch: rightBumper=Parallel rightTrigger=Pattern");
        telemetry.addLine("Launch: dpadLeft=01 dpadUp=02 dpadRight=03");
        telemetry.addLine("Drive: a=Load b=Launch1 y=Launch2 x=Cancel");
        telemetry.addLine("dpadDown+: a=Gate b=End x=Relocalize y=Motif");
        telemetry.update();
    }
}
