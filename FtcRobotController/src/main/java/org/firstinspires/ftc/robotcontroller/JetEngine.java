package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;


@TeleOp(name = "NextFTC TeleOp Program Java")
public class JetEngine extends NextFTCOpMode {
    public JetEngine() {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    DriverControlledCommand driverControlled = new PedroDriverControlled(
            Gamepads.gamepad1().leftStickY(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX(),
            false
    );
    @Override public void onInit(){
        if (driverControlled == null) {
            telemetry.addData("DriverControlledCommand", "Null");
        } else {
            telemetry.addData("DriverControlledCommand", "Initialized");
        }
        telemetry.update();

    }
    @Override public void onUpdate() {
        driverControlled.schedule();

    }
}
