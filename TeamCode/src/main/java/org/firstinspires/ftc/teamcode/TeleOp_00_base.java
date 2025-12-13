package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
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
                new SubsystemComponent(Intake.INSTANCE),
                new SubsystemComponent(Catapult.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate()
        );
        PedroComponent.follower().setStartingPose(Config.autoEndPose == null ? new Pose() : Config.autoEndPose);
        driverControlled.schedule();

        // Intake
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Inwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(Intake.INSTANCE.Outwards);

        // Catapults
        Gamepads.gamepad1().x().whenBecomesTrue(new ParallelGroup(
                Intake.INSTANCE.Stop
                , Catapult.INSTANCE.Launch01));
        Gamepads.gamepad1().y().whenBecomesTrue(new ParallelGroup(
                Intake.INSTANCE.Stop
                , Catapult.INSTANCE.Launch02));
        Gamepads.gamepad1().b().whenBecomesTrue(new ParallelGroup(
                Intake.INSTANCE.Stop
                , Catapult.INSTANCE.Launch03));
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(new ParallelGroup(
                Intake.INSTANCE.Stop
                , Catapult.INSTANCE.LaunchAllInParallel));
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(new ParallelGroup(
                Intake.INSTANCE.Stop
                , Catapult.INSTANCE.LaunchAllInPattern));
    }

    @Override
    public void onUpdate() {
        telemetry.addData("run #", 1);
        telemetry.addData("alliance", Config.allianceColor.toString());
        telemetry.addData("pattern", Config.pattern.toString());
        telemetry.addData("pos", "x: %.0f | y: %.0f | heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getX(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake", "%.0f", Intake.INSTANCE.intakeMotor.getPower());
        telemetry.addData("catapults", "01: %.0f | 02: %.0f | 03: %.0f", Catapult.INSTANCE.getPosition01(), Catapult.INSTANCE.getPosition02(), Catapult.INSTANCE.getPosition03());
        telemetry.update();
    }
}
