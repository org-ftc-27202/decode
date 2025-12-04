package org.firstinspires.ftc.teamcode.casebot.subsystems;

import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.d_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.d_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.f_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.f_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.i_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.i_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.p_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.p_right;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.TurretStartup;
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

    private final static double TICKS_TO_ROTATION = 1.5 / 7.0;
    private final static double VELOCITY_TOLERANCE = 60.0;

    private double velocity = 0.0;

    private StellarServo turretServo;
    private StellarDcMotor leftTurretMotor;
    private StellarDcMotor rightTurretMotor;
    private StellarServo turretHoodServo;

    private double PIDFScale;
    private boolean needsToStart = true;


    @Override
    public void init(HardwareMap hardwareMap) {
        PIDFScale = 1;
        needsToStart = true;
        turretServo = new StellarServo(hardwareMap, "turretServo");
        turretHoodServo = new StellarServo(hardwareMap, "turretHoodServo");
        leftTurretMotor = new StellarDcMotor(hardwareMap, "leftTurretMotor" );
        rightTurretMotor = new StellarDcMotor(hardwareMap, "rightTurretMotor");

        leftTurretMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightTurretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftTurretMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightTurretMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftTurretMotor.setVelocityPIDFCoefficents(p_left, i_left, d_left, f_left);
        rightTurretMotor.setVelocityPIDFCoefficents(p_right, i_right, d_right, f_right);

    }

    @Override
    public void update() {
        if (needsToStart){
            new TurretStartup().schedule();
            needsToStart = false;
        }
    //leftTurretMotor.setVelocityPIDFCoefficents(p_left*PIDFScale, i_left*PIDFScale, d_left*PIDFScale, f_left*PIDFScale);
        //rightTurretMotor.setVelocityPIDFCoefficents(p_right*PIDFScale, i_right*PIDFScale, d_right*PIDFScale, f_right*PIDFScale);
    }

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
    public void setPIDFScale(double scale){
        PIDFScale = scale;
        leftTurretMotor.setVelocityPIDFCoefficents(p_left*PIDFScale, i_left*PIDFScale, d_left*PIDFScale, f_left*PIDFScale);
        rightTurretMotor.setVelocityPIDFCoefficents(p_right*PIDFScale, i_right*PIDFScale, d_right*PIDFScale, f_right*PIDFScale);
    }

    public double getVelocityOffOfTarget() {
        return Math.abs(leftTurretMotor.getVelocity() - velocity);
    }

    public boolean velocityWithinTolerance() {
        return getVelocityOffOfTarget() < VELOCITY_TOLERANCE;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "Turret Servo Pos: %f\n" +
                    "   Hood Pos: %f\n" +
                    "   Turret Left/Right Motor Vel: %f, %f\n" +
                "Turret Target Velocity: %f\n" +
                    "   TurretAtTargetVelocity?: %b",
                turretServo.getPosition(),
                turretHoodServo.getPosition(),
                leftTurretMotor.getVelocity(),
                rightTurretMotor.getVelocity(),
                velocity,
                velocityWithinTolerance()
        );
    }
}