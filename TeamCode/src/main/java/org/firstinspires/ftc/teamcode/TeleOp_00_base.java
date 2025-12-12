package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;


@Disabled
@TeleOp
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

    private auto_00_NearFromGoal.AllianceColors allianceColor = auto_00_NearFromGoal.AllianceColors.RED;
    public void setAllianceColor(auto_00_NearFromGoal.AllianceColors inAllianceColor) {
        allianceColor = inAllianceColor;
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
        driverControlled.schedule();

        // Intake
        Gamepads.gamepad1().leftTrigger().greaterThan(0.2).whenBecomesTrue(Intake.INSTANCE.Inwards);
        Gamepads.gamepad1().leftTrigger().lessThan(0.2).whenBecomesTrue(Intake.INSTANCE.Stop);
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(Intake.INSTANCE.Outwards);

        // Catapults
        Gamepads.gamepad1().x().whenBecomesTrue(Catapult.INSTANCE.Launch01);
        Gamepads.gamepad1().y().whenBecomesTrue(Catapult.INSTANCE.Launch02);
        Gamepads.gamepad1().b().whenBecomesTrue(Catapult.INSTANCE.Launch03);
        Gamepads.gamepad1().rightTrigger().greaterThan(0.2).whenBecomesTrue(Catapult.INSTANCE.LaunchAllInParallel);
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(Catapult.INSTANCE.LaunchAllInPattern);
    }

    @Override
    public void onUpdate() {
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Alliance", allianceColor.toString());
        telemetry.addData("Pattern", "ToDo GPP");
        telemetry.addData("run #", 1);
        telemetry.addData("pos", "x: %.0f y: %.0f heading: %.0f", PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getX(), Math.toDegrees(PedroComponent.follower().getPose().getHeading()));
        telemetry.addData("intake", Intake.INSTANCE.intakeMotor.getPower());
        telemetry.addData("catapult01", Catapult.INSTANCE.getPosition01());
        telemetry.addData("catapult02", Catapult.INSTANCE.getPosition02());
        telemetry.addData("catapult03", Catapult.INSTANCE.getPosition03());
        telemetry.update();
    }
}
