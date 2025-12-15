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
    private Pose BasePose, launchNear1Pose;
    private PathChain driveLatestToBase, driveToLaunchNear1Pose;
    public void buildPaths() {
//        BasePose = new Pose(38, 33, Math.toRadians(0));;
        BasePose = new Pose(112, 120, Math.toRadians(90));;
        launchNear1Pose = new Pose(92, 102, Math.toRadians(42));

        if (Config.allianceColor == Config.AllianceColors.BLUE) {
            BasePose = BasePose.mirror();
            launchNear1Pose = launchNear1Pose.mirror();
        }

        driveLatestToBase = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), BasePose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), BasePose.getHeading())
                .build();

        driveToLaunchNear1Pose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), launchNear1Pose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), launchNear1Pose.getHeading())
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
//        PedroComponent.follower().setStartingPose(Config.autoEndPose == null ? new Pose() : Config.autoEndPose);
        PedroComponent.follower().setStartingPose(new Pose(112, 135.5, Math.toRadians(90)));
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
                new ParallelGroup(Intake.INSTANCE.Stop, Catapult.INSTANCE.LaunchInParallel));
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
                new SequentialGroup(
                    new ParallelGroup(Intake.INSTANCE.Stop, Camera.INSTANCE.getCatapultArtifactColors),
                    Catapult.INSTANCE.LaunchByPattern));
//        Gamepads.gamepad1().a().whenBecomesTrue(Camera.INSTANCE.getCatapultArtifactColors);

        // Automated Driving Towards Target Pose
        // cancel automated driving and restart back to TeleOp drive
        Gamepads.gamepad1().x().whenBecomesTrue(
                new InstantCommand(() -> {
                    PedroComponent.follower().startTeleopDrive();}));
        // Drive to Base (parking)
        Gamepads.gamepad1().y().whenBecomesTrue(
                new SequentialGroup(
                        new FollowPath(driveLatestToBase,true, 1.0)
                ));
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
        telemetry.update();
    }
}
