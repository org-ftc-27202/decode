package org.firstinspires.ftc.robotcontroller.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.pedroPathing.Constants;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import static dev.nextftc.bindings.Bindings.*;

import java.util.ArrayList;
import java.util.List;
@Configurable
@TeleOp(name = "Tuning", group = "Pedro Pathing")

public class TestTeleOp extends NextFTCOpMode {
    {
        addComponents(
                new PedroComponent(Constants::createFollower)
        );
    }
    IntakeSubsystem intake;
    SubsystemComponent subsystems;
    DriverControlledCommand driverControlled;
    @Override
    public void onInit() {

        intake = new IntakeSubsystem();
        subsystems = new SubsystemComponent(
                intake
        );
        driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
    }

    @Override
    public void onStartButtonPressed() {
        super.onStartButtonPressed();

    }

    @Override
    public void onUpdate() {
        BindingManager.update();
        driverControlled.schedule();
        if (gamepad1.a) {
            intake.runIntake.schedule();
        }

    }
    @Override
    public void onStop(){
        BindingManager.reset();
    }
}
