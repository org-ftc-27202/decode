package org.firstinspires.ftc.robotcontroller;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Spindexer implements Subsystem {
    public static final Spindexer INSTANCE = new Spindexer();
    private Spindexer(){}


    ServoEx lever = new ServoEx("leverTransfer");
    ServoEx spindexerRotate = new ServoEx("spindexer");


    public Command leverUp = new SetPosition(lever, 0.0).requires(this);
    public Command leverDown = new SetPosition(lever, 0.2).requires(this);

    public Command intake0 = new SetPosition(spindexerRotate, (double)0).requires(this);
    public Command intake1 = new SetPosition(spindexerRotate, (double) 240 /355).requires(this);
    public Command intake2 = new SetPosition(spindexerRotate, (double) 120 /355).requires(this);

    public Command lever0 = new SetPosition(spindexerRotate, (double) 180 /355).requires(this);
    public Command lever1 = new SetPosition(spindexerRotate, (double) 60 /1355).requires(this);
    public Command lever2 = new SetPosition(spindexerRotate, (double) 300 /355).requires(this);

    @Override
    public void initialize() {
        // initialization logic (runs on init)
        leverDown.schedule();
        intake0.schedule();
    }

    @Override
    public void periodic() {


    }
}