package org.firstinspires.ftc.robotcontroller;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import java.time.Duration;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Spindexer implements Subsystem {
    public static final Spindexer INSTANCE = new Spindexer();
    private Spindexer(){}

    private DigitalChannel beamBreak = hardwareMap.get(DigitalChannel.class, "beamBreak");
    ServoEx lever = new ServoEx("leverTransfer");
    ServoEx spindexerRotate = new ServoEx("spindexer");

    public Command leverUp = new SetPosition(lever, 0.0).requires(this);
    public Command leverDown = new SetPosition(lever, 0.28).requires(this);

    public Command intake0 = new SetPosition(spindexerRotate, (double)0).requires(this);
    public Command intake1 = new SetPosition(spindexerRotate, (double) 240 /360).requires(this);
    public Command intake2 = new SetPosition(spindexerRotate, (double) 120 /360).requires(this);

    public Command lever0 = new SetPosition(spindexerRotate, (double) 180 /360).requires(this);
    public Command lever1 = new SetPosition(spindexerRotate, (double) 60 /360).requires(this);
    public Command lever2 = new SetPosition(spindexerRotate, (double) 300 /360).requires(this);

    public SequentialGroup ballThru = new SequentialGroup(
            new Delay(.5),
            lever0,
            leverUp,
            new Delay(1),
            leverDown,
            intake0

    );

    public boolean beamBreakCheck() {
        return beamBreak.getState();
    }
    @Override
    public void initialize() {
        // initialization logic (runs on init)
        leverDown.schedule();
        intake0.schedule();
    }

    @Override
    public void periodic() {
        telemetry.addData("beamBroke", beamBreakCheck());

    }
}