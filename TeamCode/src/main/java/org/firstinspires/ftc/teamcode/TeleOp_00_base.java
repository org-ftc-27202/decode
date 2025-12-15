package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
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

public abstract class TeleOp_00_base extends NextFTCOpMode {
    public TeleOp_00_base() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(Intake.INSTANCE, Catapult.INSTANCE, Camera.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private Pose relocalizePose;
    private PathChain driveToBaseEndGame, driveToLoadingZone, driveToGate, driveToLaunchNear1Pose;
    public void buildPaths() {
        Pose baseEndGamePose, loadingZonePose, loadingZonePose2, gatePose, launch1Pose;
        // Robot Length: 17"; Robot Width: 17.5"
        relocalizePose = new Pose(8.75, 8.5, Math.toRadians(0));

        baseEndGamePose = new Pose(38, 32, Math.toRadians(0));
        gatePose = new Pose(128, 60, Math.toRadians(0));
        loadingZonePose = new Pose(20, 20, Math.toRadians(45));
        loadingZonePose2 = new Pose(24, 16, Math.toRadians(45));
        if (Config.goalOption == Config.GoalOptions.FAR) {
//            launch1Pose = new Pose(84, 20, Math.toRadians(65));  // good for auto
            launch1Pose = new Pose(68, 30, Math.toRadians(57));
        } else { // NEAR
            launch1Pose = new Pose(92, 102, Math.toRadians(42));  //ToDo
        }

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            relocalizePose = relocalizePose.mirror();
            baseEndGamePose = baseEndGamePose.mirror();
            gatePose = gatePose.mirror();
            launch1Pose = launch1Pose.mirror();
            loadingZonePose = loadingZonePose.mirror();
            loadingZonePose2 = loadingZonePose2.mirror();
        }

        driveToBaseEndGame = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), baseEndGamePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), baseEndGamePose.getHeading(), 0.5)
                .build();

        driveToGate = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), gatePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), gatePose.getHeading(), 0.5)
                .build();

        driveToLoadingZone = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), loadingZonePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), loadingZonePose.getHeading(), 0.5)
                .build();

        driveToLaunchNear1Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), loadingZonePose2))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), loadingZonePose2.getHeading(), 0.5)
                .addPath(new BezierLine(loadingZonePose2, launch1Pose))
                .setLinearHeadingInterpolation(loadingZonePose2.getHeading(), launch1Pose.getHeading(), 0.5)
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
            Gamepads.gamepad1().rightStickX().negate());
        PedroComponent.follower().setStartingPose(Config.autoEndPose == null ? new Pose() : Config.autoEndPose);
        driverControlled.schedule();

        // Intake
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(Intake.INSTANCE.Inwards);
        Gamepads.gamepad1().leftBumper().whenBecomesFalse(Intake.INSTANCE.Stop);
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Outwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);

        // Catapults
        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch01));
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch02));
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.Launch03));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(
                new SequentialGroup(
                    new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                    Catapult.INSTANCE.LaunchInParallel));
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                        Catapult.INSTANCE.LaunchByPattern));

        // Manual Settings
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().b()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().setPose(relocalizePose)));
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().y()).whenBecomesTrue(Camera.INSTANCE.capturePattern);

        // Auto-Drive
        // Cancel automated driving and restart back to TeleOp drive
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().x()).whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().startTeleopDrive()));
        // Drive to Loading Zone
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().a()).whenBecomesTrue(new FollowPath(driveToLoadingZone,true, 1.0));
        // Drive to Launch 1
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().b()).whenBecomesTrue(new FollowPath(driveToLaunchNear1Pose,true, 1.0));
        // Drive to Gate
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().y()).whenBecomesTrue(new FollowPath(driveToGate,true, 1.0));
        // Drive to Base (parking)
        Gamepads.gamepad1().dpadDown().and(Gamepads.gamepad1().a()).whenBecomesTrue(new FollowPath(driveToBaseEndGame,true, 1.0));
    }
    @Override
    public void onUpdate() {
        telemetry.addData("run #", 1);
        telemetry.addData("alliance", Config.allianceColor.toString());
        telemetry.addData("pattern", Config.motifPattern.toString());
        telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake", "%.0f", Intake.INSTANCE.intakeMotor.getPower());
        telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
        telemetry.addLine("Auto-Drive: a=Load | b=Launch | y=Gate | x=Cancel");
        telemetry.addLine("dpadDown+: a=Base | b=Relocalize | y=GetMotif");
        telemetry.update();
    }
}
