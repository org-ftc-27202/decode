package org.firstinspires.ftc.teamcode;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class IntakeStopper implements Subsystem {
    public static final IntakeStopper INSTANCE = new IntakeStopper();
    private IntakeStopper() {}
    private final ServoEx stopperServo = new ServoEx("stopper");
    private final double openPosition = 0.72;
    private final double closePosition = 0.90;
    public int ballCounter = 0;
    private DigitalChannel ballCounterSwitch;
    private Timer ballSensorTimer = new Timer();
    public void mapIntakeStopperHardware(HardwareMap hardwareMap) {
        ballCounterSwitch = hardwareMap.get(DigitalChannel.class, "ballCounterSwitch");
        ballCounterSwitch.setMode(DigitalChannel.Mode.INPUT);
        ballCounter = 0;
    }
    public Command toOpenPosition = new SetPosition(stopperServo, openPosition).requires(this);
    public Command toClosePosition = new SetPosition(stopperServo, closePosition).requires(this);

    public Command initIntakeStopper = new LambdaCommand("initIntakeStopper")
            .setStart(() -> {
                ballCounter = 0;
                new SequentialGroup(toOpenPosition);})
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(false)
            .requires(this);

    public Command ballCountTo2 = new LambdaCommand("ballCountTo2")
            .setStart(() -> {
                ballCounter = 2;
                new SequentialGroup(toOpenPosition);})
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(false)
            .requires(this);
    public void CountBalls() {
        if (!ballCounterSwitch.getState()) {
            if (ballCounter == 0) {
                ballSensorTimer.resetTimer();
                ballCounter = 1;
            } else if (ballSensorTimer.getElapsedTime() > 200) {  // milliseconds in between reading
                ballSensorTimer.resetTimer();
                ballCounter += 1;
            }
            if (ballCounter >= 3) {
                stopperServo.setPosition(closePosition);
            }
        }
    }
}