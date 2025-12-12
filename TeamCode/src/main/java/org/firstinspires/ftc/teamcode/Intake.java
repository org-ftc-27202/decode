package org.firstinspires.ftc.teamcode;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToState;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    public final MotorEx intakeMotor = new MotorEx("intake");

    private final ControlSystem controlSystem = ControlSystem.builder()
            .velPid(0.001, 0.0, 0.0)
            .build();

    public Command Inwards = new RunToVelocity(controlSystem, 2000.0).requires(this);
    public Command Stop = new RunToVelocity(controlSystem, 0).requires(this);
    public Command Outwards = new RunToVelocity(controlSystem, -750.0).requires(this);

    @Override
    public void periodic() {
        intakeMotor.setPower(controlSystem.calculate());
    }
}