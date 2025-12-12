package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Catapult implements Subsystem {
    final double CATAPULT_LAUNCH_POWER = 0.8;  // for 3 springs -> near launch zone
    //    final double CATAPULT_LAUNCH_POWER = 1.0;  // for 4 springs -> far launch zone
    final int HALF_ROTATION = 700;  // smaller slip gear

    public static final Catapult INSTANCE = new Catapult();
    private Catapult() { }

    private final MotorEx catapult01Motor = new MotorEx("catapult01");
    private final MotorEx catapult02Motor = new MotorEx("catapult02");
    private final MotorEx catapult03Motor = new MotorEx("catapult03");


    private final ControlSystem controlSystem = ControlSystem.builder()
            .build();

    public Command Launch01 = new LambdaCommand("Launch")
        .setStart(() -> {
            catapult01Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER);})
        .setUpdate(() -> {
            catapult01Motor.getMotor().setTargetPosition(HALF_ROTATION);})
        .setIsDone(() -> {
            return catapult01Motor.getMotor().getCurrentPosition() >= HALF_ROTATION;})
        .setStop(interrupted -> {
            catapult01Motor.getMotor().setPower(0);
            catapult01Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            catapult01Motor.getMotor().setTargetPosition(0);
            catapult01Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
        .setInterruptible(false)
        .requires(this);

    public Command Launch02 = new LambdaCommand("Launch")
            .setStart(() -> {
                catapult02Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER);})
            .setUpdate(() -> {
                catapult02Motor.getMotor().setTargetPosition(HALF_ROTATION);})
            .setIsDone(() -> {
                return catapult02Motor.getMotor().getCurrentPosition() >= HALF_ROTATION;})
            .setStop(interrupted -> {
                catapult02Motor.getMotor().setPower(0);
                catapult02Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult02Motor.getMotor().setTargetPosition(0);
                catapult02Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
            .setInterruptible(false)
            .requires(this);

    public Command Launch03 = new LambdaCommand("Launch")
            .setStart(() -> {
                catapult03Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER);})
            .setUpdate(() -> {
                catapult03Motor.getMotor().setTargetPosition(HALF_ROTATION);})
            .setIsDone(() -> {
                return catapult03Motor.getMotor().getCurrentPosition() >= HALF_ROTATION;})
            .setStop(interrupted -> {
                catapult03Motor.getMotor().setPower(0);
                catapult03Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult03Motor.getMotor().setTargetPosition(0);
                catapult03Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
            .setInterruptible(false)
            .requires(this);

    public Command LaunchAllInParallel = new ParallelGroup(
            Launch01, Launch02, Launch03);
    public Command LaunchAllInPattern = new SequentialGroup(
            Launch01, Launch02, Launch03);
    public double getPosition01() {
        return catapult01Motor.getMotor().getCurrentPosition();
    }
    public double getPosition02() {
        return catapult02Motor.getMotor().getCurrentPosition();
    }
    public double getPosition03() {
        return catapult03Motor.getMotor().getCurrentPosition();
    }

    @Override
    public void initialize() {
        catapult01Motor.getMotor().setDirection(DcMotorEx.Direction.REVERSE);
        catapult01Motor.getMotor().setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult01Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult01Motor.getMotor().setTargetPosition(0);
        catapult01Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult02Motor.getMotor().setDirection(DcMotorEx.Direction.REVERSE);
        catapult02Motor.getMotor().setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult02Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult02Motor.getMotor().setTargetPosition(0);
        catapult02Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult03Motor.getMotor().setDirection(DcMotorEx.Direction.REVERSE);
        catapult03Motor.getMotor().setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult03Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult03Motor.getMotor().setTargetPosition(0);
        catapult03Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}