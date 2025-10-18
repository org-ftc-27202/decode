package org.firstinspires.ftc.robotcontroller;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake1 implements Subsystem {
    public static final Intake1 INSTANCE = new Intake1();
    private Intake1(){}

    private MotorEx motor = new MotorEx("intake");
    private ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.005, 0, 0)
            .build();

    public Command stopMotor = new SetPower(motor, 0).requires(this);
    public Command runMotor = new SetPower(motor, 500).requires(this);
    @Override
    public void initialize() {
        // initialization logic (runs on init)

    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)

    }
}