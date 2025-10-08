package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;
import static dev.nextftc.bindings.Bindings.*;

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
    private MotorEx frontLeftMotor = new MotorEx("lf").reversed();
    private MotorEx frontRightMotor = new MotorEx("rf").reversed();
    private MotorEx backLeftMotor = new MotorEx("lr").reversed();
    private MotorEx backRightMotor = new MotorEx("rr").reversed();

    MecanumDriverControlled drivercontrolled = new MecanumDriverControlled(
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor,
            Gamepads.gamepad1().leftStickY(),
    Gamepads.gamepad1().leftStickX().negate(),
    Gamepads.gamepad1().rightStickX());
    Button myButton = button(() -> gamepad1.a);

    @Override public void onInit(){


    }
    @Override public void onStartButtonPressed(){
        drivercontrolled.schedule();
    }

    @Override public void onUpdate() {
        myButton.whenBecomesTrue(() -> drivercontrolled.stop(true));
        myButton.whenBecomesFalse(() -> drivercontrolled.schedule());
        BindingManager.update();
        telemetry.addData("position", follower().getPose());
        telemetry.addData("heading", follower().getHeading());
        telemetry.update();


    }
    @Override public void onStop(){

        BindingManager.reset();
    }






}
