package org.firstinspires.ftc.teamcode;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Wiper implements Subsystem {
    public static final Wiper INSTANCE = new Wiper();
    private Wiper() { }
    private final ServoEx wiperServo = new ServoEx("wiper");
    public Command toIntakePosition = new SetPosition(wiperServo, 0.48).requires(this);
    public Command toLaunchPosition = new SetPosition(wiperServo, 0.85).requires(this);
}