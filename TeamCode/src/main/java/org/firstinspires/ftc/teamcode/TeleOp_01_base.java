package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
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
    private boolean telemetryOnFlag;
    @Override
    public void onInit() {
        opModeTimer = new Timer();
        Camera.INSTANCE.mapCameraHardware(hardwareMap);
        Intake.INSTANCE.mapIntakeStopperHardware(hardwareMap);

        telemetry.addLine("--- Buttons ---");
        telemetry.addLine("Intake: leftBumper=In/Off leftTrigger=Out/Off");
        telemetry.addLine("Launch: rightBumper=Parallel rightTrigger=Pattern");
        telemetry.addLine("Auto-Drive: x=Cancel Auto-Drive and Ball Count to 0");
        telemetry.addLine("dpadRight=Wiper to Launch");
        telemetry.update();
//        telemetryOnFlag = true;
        telemetryOnFlag = false;
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
                        Intake.INSTANCE.Inwards,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Outwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(Intake.INSTANCE.wiperToLaunchPosition);

        // Catapults
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
               new SequentialGroup(
                       new ParallelGroup(Intake.INSTANCE.Stop,
                                Camera.INSTANCE.captureGoalPosition,
                                Intake.INSTANCE.initIntakeStopper),
                       new InstantCommand(() -> PedroComponent.follower().turn(Math.toRadians(Config.deltaToCenterAngleInDeg), false)),
                       Catapult.INSTANCE.LaunchInParallel,
                       new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(
                new SequentialGroup(
                        new ParallelGroup(Intake.INSTANCE.Stop,
                                Camera.INSTANCE.getCatapultArtifactColors,
                                Camera.INSTANCE.captureGoalPosition,
                                Intake.INSTANCE.initIntakeStopper),
                        new InstantCommand(() -> PedroComponent.follower().turn(Math.toRadians(Config.deltaToCenterAngleInDeg), false)),
                        Catapult.INSTANCE.LaunchByPattern,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));

        // Reset
        Gamepads.gamepad1().dpadDown().not().and(Gamepads.gamepad1().x()).whenBecomesTrue(
                new ParallelGroup(
                        Intake.INSTANCE.Stop,
                        Intake.INSTANCE.initIntakeStopper,
                        new InstantCommand(() -> PedroComponent.follower().startTeleopDrive())));
    }
    @Override
    public void onUpdate() {
        Intake.INSTANCE.CountBalls();

        if (telemetryOnFlag) {
            telemetry.addData("run #", 1);
            telemetry.addData("alliance", Config.allianceColor.toString());
            telemetry.addData("pattern", Config.motifPattern.toString());
            telemetry.addData("pos", "x: %.1f | y: %.1f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
            telemetry.addData("intake (power)", "%.0f", Intake.INSTANCE.getPower());
            telemetry.addData("balls", "%d", Intake.INSTANCE.ballCounter);
            telemetry.addData("catapults (pos)", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
            telemetry.addData("catapults (pattern)", "%s%s%s", Config.catapult01Color.toString().charAt(0), Config.catapult02Color.toString().charAt(0), Config.catapult03Color.toString().charAt(0));
            telemetry.addData("goal", "cx: %.0f | cy: %.0f | cd: %.0f", Config.deltaToCenterX, Config.deltaToCenterY, Config.deltaToCenterAngleInDeg);
            telemetry.addData("Timer", "%.1f", opModeTimer.getElapsedTimeSeconds());
            telemetry.update();
        }
    }
}
