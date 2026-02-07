package org.firstinspires.ftc.teamcode;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }
    private enum IntakeMode {INWARDS, OUTWARDS, STOP}
    private IntakeMode intakeMode = IntakeMode.STOP;
    private final MotorEx intakeMotor = new MotorEx("intake");
    private final double wiperIntakePosition = 0.46;
    private final double wiperLaunchPosition = 0.88;
    private final ServoEx wiperServo = new ServoEx("wiper");
    private final double stopperOpenPosition = 0.72;
    private final double stopperClosePosition = 0.90;
    private final ServoEx stopperServo = new ServoEx("stopper");
    public int ballCounter = 0;
    private DigitalChannel ballCounterSwitch;
    private Timer ballSensorTimer = new Timer();
    private Timer autoStopIntakeTimer = new Timer();
    private boolean autoStopIntakeFlag = false;

    @Override
    public void initialize() {
        intakeMotor.getMotor().setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.getMotor().setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        intakeMotor.getMotor().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMode = IntakeMode.STOP;
        ballCounter = 0;
        autoStopIntakeFlag = false;
    }
    public Command wiperToIntakePosition = new SetPosition(wiperServo, wiperIntakePosition).requires(this);
    public Command wiperToLaunchPosition = new SetPosition(wiperServo, wiperLaunchPosition).requires(this);
    public void mapIntakeStopperHardware(HardwareMap hardwareMap) {
        ballCounterSwitch = hardwareMap.get(DigitalChannel.class, "ballCounterSensor");
        ballCounterSwitch.setMode(DigitalChannel.Mode.INPUT);
    }
    public Command stopperToOpenPosition = new SetPosition(stopperServo, stopperOpenPosition).requires(this);
    public Command stopperToClosePosition = new SetPosition(stopperServo, stopperClosePosition).requires(this);
    public Command initIntakeStopper = new LambdaCommand("initIntakeStopper")
            .setStart(() -> {
                ballCounter = 0;
                autoStopIntakeFlag = false;
            })
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
            } else if (ballSensorTimer.getElapsedTime() > 200) {  // milliseconds in between sensor reading
                ballSensorTimer.resetTimer();
                ballCounter += 1;
                if (ballCounter >= 3) {
                    autoStopIntakeTimer.resetTimer();
                    autoStopIntakeFlag = false;
                    intakeMode = IntakeMode.STOP;
                }
            }
        }
        if (ballCounter >= 3) {
            if (autoStopIntakeTimer.getElapsedTime() > 10) { // milliseconds to close stopper
                stopperServo.setPosition(stopperClosePosition);
            }
            if (autoStopIntakeTimer.getElapsedTime() > 1500 && !autoStopIntakeFlag) { // milliseconds to auto stop intake
                    autoStopIntakeFlag = true;
                    wiperServo.setPosition(wiperLaunchPosition);
                    intakeMotor.getMotor().setPower(0);
            }
        }
    }
    public double getPower() {
        return intakeMotor.getMotor().getPower();
    }
    public Command Inwards = new LambdaCommand("Inwards")
            .setStart(() -> {
                intakeMode = IntakeMode.INWARDS;
                wiperServo.setPosition(wiperIntakePosition);
                stopperServo.setPosition(stopperOpenPosition);
                intakeMotor.getMotor().setPower(0.80);
            })
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);

    public Command Stop = new LambdaCommand("Stop")
            .setStart(() -> {
                intakeMode = IntakeMode.STOP;
                wiperServo.setPosition(wiperLaunchPosition);
                stopperServo.setPosition(stopperClosePosition);
                intakeMotor.getMotor().setPower(0);
            })
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);

    public Command Outwards = new LambdaCommand("Outwards")
            .setStart(() -> {
                intakeMode = IntakeMode.OUTWARDS;
                wiperServo.setPosition(wiperIntakePosition);
                stopperServo.setPosition(stopperOpenPosition);
                intakeMotor.getMotor().setPower(-0.70);
            })
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);
}