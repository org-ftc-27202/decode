package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }
    private final MotorEx intakeMotor = new MotorEx("intake");

    @Override
    public void initialize() {
        intakeMotor.getMotor().setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.getMotor().setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        intakeMotor.getMotor().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getPower() {
        return intakeMotor.getMotor().getPower();
    }
    public Command Inwards = new LambdaCommand("Inwards")
            .setStart(() -> intakeMotor.getMotor().setPower(1.0))
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);

    public Command Stop = new LambdaCommand("Stop")
            .setStart(() -> intakeMotor.getMotor().setPower(0))
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);

    public Command Outwards = new LambdaCommand("Outwards")
            .setStart(() -> intakeMotor.getMotor().setPower(0.5))
            .setUpdate(() -> {})
            .setIsDone(() -> true)
            .setStop(interrupted -> {})
            .setInterruptible(true)
            .requires(this);
}