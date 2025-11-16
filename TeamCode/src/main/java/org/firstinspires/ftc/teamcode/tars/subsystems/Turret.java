package org.firstinspires.ftc.teamcode.tars.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class Turret extends Subsystem {
    private static final Turret turret = new Turret();

    public static Turret getInstance() {
        return turret;
    }

    private Turret() {}

    private final static double DEGREES_TO_SERVO = 1.0 / 320.0;
    private final static double VELOCITY_TOLERANCE = 10;

    private double velocity = 0;

    private StellarServo turretServo;
    private StellarDcMotor leftTurretMotor;
    private StellarDcMotor rightTurretMotor;
    private StellarServo turretHoodServo;


    @Override
    public void init(HardwareMap hardwareMap) {
        turretServo= new StellarServo(hardwareMap, "turretServo");
        turretHoodServo = new StellarServo(hardwareMap, "turretHoodServo");
        leftTurretMotor = new StellarDcMotor(hardwareMap, "leftTurretMotor" );
        rightTurretMotor = new StellarDcMotor(hardwareMap, "rightTurretMotor");
        leftTurretMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightTurretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftTurretMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightTurretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void update() {}

    public StellarServo getTurretServo() {
        return turretServo;
    }

    public StellarServo getTurretHoodServo(){
        return turretHoodServo;
    }

    public StellarDcMotor getLeftTurretMotor() {
        return leftTurretMotor;
    }

    public StellarDcMotor getRightTurretMotor() {
        return rightTurretMotor;
    }

    public void setTurretVelocity(double velocity) {
        this.velocity = velocity;
        leftTurretMotor.setTargetVelocity(velocity);
        rightTurretMotor.setTargetVelocity(velocity);
    }

    public boolean checkCurrentVelocity() {
        return (Math.abs(leftTurretMotor.getVelocity())) < (velocity + VELOCITY_TOLERANCE);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "Turret Servo Pos: %f\n" +
                        "Hood Pos: %f\n" +
                        "Turret Motor Vel: %f\n"+
                "Turret Target Velocity: %f\n"+
                        "TurretAtTargetVelocity?: %b",
                turretServo.getPosition(),
                turretHoodServo.getPosition(),
                leftTurretMotor.getVelocity(), velocity, checkCurrentVelocity()
        );
    }
}