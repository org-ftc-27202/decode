package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.WGIfElseCommand;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Catapult implements Subsystem {
    private double CATAPULT_LAUNCH_POWER = 1.0;
    private int HALF_ROTATION;
    final double LAUNCHING_IN_PARALLEL_DELAY_IN_SECONDS = 0.10;  // delay in between catapult launches
    final double LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS = 0.50;  // delay in between catapult launches

    public static final Catapult INSTANCE = new Catapult();
    private Catapult() { }

    private final MotorEx catapult01Motor = new MotorEx("catapult01");
    private final MotorEx catapult02Motor = new MotorEx("catapult02");
    private final MotorEx catapult03Motor = new MotorEx("catapult03");

    @Override
    public void initialize() {
        if (Config.goalOption == Config.GoalOptions.FAR) {
            CATAPULT_LAUNCH_POWER = 1.0;
            HALF_ROTATION = 710;
        } else {    // NEAR
            CATAPULT_LAUNCH_POWER = 1.0;
            HALF_ROTATION = 710;
        }

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
    public double getPosition01() {
        return catapult01Motor.getMotor().getCurrentPosition();
    }
    public double getPosition02() {
        return catapult02Motor.getMotor().getCurrentPosition();
    }
    public double getPosition03() {
        return catapult03Motor.getMotor().getCurrentPosition();
    }
    public Command Launch01 = new LambdaCommand("Launch 01")
        .setStart(() -> catapult01Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER))
        .setUpdate(() -> catapult01Motor.getMotor().setTargetPosition(HALF_ROTATION))
        .setIsDone(() -> catapult01Motor.getMotor().getCurrentPosition() >= HALF_ROTATION)
        .setStop(interrupted -> {
            catapult01Motor.getMotor().setPower(0);
            catapult01Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            catapult01Motor.getMotor().setTargetPosition(0);
            catapult01Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
        .setInterruptible(false)
        .requires(this);

    public Command Launch02 = new LambdaCommand("Launch 02")
            .setStart(() -> catapult02Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER))
            .setUpdate(() -> catapult02Motor.getMotor().setTargetPosition(HALF_ROTATION))
            .setIsDone(() -> catapult02Motor.getMotor().getCurrentPosition() >= HALF_ROTATION)
            .setStop(interrupted -> {
                catapult02Motor.getMotor().setPower(0);
                catapult02Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult02Motor.getMotor().setTargetPosition(0);
                catapult02Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
            .setInterruptible(false)
            .requires(this);

    public Command Launch03 = new LambdaCommand("Launch 03")
            .setStart(() -> catapult03Motor.getMotor().setPower(CATAPULT_LAUNCH_POWER))
            .setUpdate(() -> catapult03Motor.getMotor().setTargetPosition(HALF_ROTATION))
            .setIsDone(() -> catapult03Motor.getMotor().getCurrentPosition() >= HALF_ROTATION)
            .setStop(interrupted -> {
                catapult03Motor.getMotor().setPower(0);
                catapult03Motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult03Motor.getMotor().setTargetPosition(0);
                catapult03Motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);})
            .setInterruptible(false)
            .requires(this);

    public Command LaunchInParallel = new ParallelGroup(
            Launch01,
            Launch03,
            new SequentialGroup(new Delay(LAUNCHING_IN_PARALLEL_DELAY_IN_SECONDS), Launch02))
            .requires(this);
    public Command Launch123 = new ParallelGroup(
            Launch01,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch02),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS * 2), Launch03))
            .requires(this);
    public Command Launch213 = new ParallelGroup(
            Launch02,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch01),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS * 2), Launch03))
            .requires(this);
    public Command Launch132 = new ParallelGroup(
            Launch01,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch03),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS * 2), Launch02))
            .requires(this);
    public Command Launch1Then23 = new ParallelGroup(
            Launch01,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch02),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch03))
            .requires(this);
    public Command Launch2Then13 = new ParallelGroup(
            Launch02,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch01),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch03))
            .requires(this);
    public Command Launch3Then12 = new ParallelGroup(
            Launch03,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch01),
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch02))
            .requires(this);
    public Command Launch12Then3 = new ParallelGroup(
            Launch01,
            Launch02,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch03))
            .requires(this);
    public Command Launch13Then2 = new ParallelGroup(
            Launch01,
            Launch03,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch02))
            .requires(this);
    public Command Launch23Then1 = new ParallelGroup(
            Launch02,
            Launch03,
            new SequentialGroup(new Delay(LAUNCHING_BY_PATTERN_DELAY_IN_SECONDS), Launch01))
            .requires(this);
    public Command LaunchByPattern =
            new WGIfElseCommand(() -> Config.motifPattern == Config.MotifPatterns.GPP,
                    new WGIfElseCommand(() -> Config.catapult01Color == Config.Colors.GREEN,
                            Launch1Then23,
                            new WGIfElseCommand(() -> Config.catapult02Color == Config.Colors.GREEN,
                                    Launch2Then13,
                                    Launch3Then12
                            )),
                    new WGIfElseCommand(() -> Config.motifPattern == Config.MotifPatterns.PGP,
                            new WGIfElseCommand(() -> Config.catapult01Color == Config.Colors.GREEN,
                                    Launch213,
                                    new WGIfElseCommand(() -> Config.catapult02Color == Config.Colors.GREEN,
                                            Launch123,
                                            Launch132
                                    )),
                            new WGIfElseCommand(() -> Config.motifPattern == Config.MotifPatterns.PPG,
                                    new WGIfElseCommand(() -> Config.catapult01Color == Config.Colors.GREEN,
                                            Launch23Then1,
                                            new WGIfElseCommand(() -> Config.catapult02Color == Config.Colors.GREEN,
                                                    Launch13Then2,
                                                    Launch12Then3
                                            ))
                            )))
                    .requires(this);
}