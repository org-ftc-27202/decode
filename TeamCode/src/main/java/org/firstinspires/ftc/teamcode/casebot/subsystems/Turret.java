package org.firstinspires.ftc.teamcode.casebot.subsystems;

import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.d_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.d_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.f_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.f_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.i_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.i_right;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.p_left;
import static org.firstinspires.ftc.teamcode.casebot.subsystems.TurretPIDFConstants.p_right;
import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class Turret extends Subsystem {
    private final static double DEGREES_TO_SERVO = 1.0 / 320.0;

    private final static double TICKS_TO_ROTATION = 1.5 / 7.0;
    private final static double VELOCITY_TOLERANCE = 41.0;
    private final static double YAW_SERVO_DEGREE_RANGE = 330.0;
    private final static double YAW_GEAR_RATIO = 1.167;
    private final static double DEGREES_TO_POS = (YAW_GEAR_RATIO/YAW_SERVO_DEGREE_RANGE);
    private final static double YAW_SERVO_MID = 0.82;

    private double velocity = 0.0;

    private StellarServo turretYawServo;
    private StellarDcMotor leftTurretMotor;
    private StellarDcMotor rightTurretMotor;
    private StellarServo turretPitchServo;
    private WebcamName webcamName;

    private double PIDFScale;
    private boolean needsToStart = true;

    @Override
    public void init(HardwareMap hardwareMap) {
        PIDFScale = 1.0;
        needsToStart = true;
        turretYawServo = new StellarServo(hardwareMap, "turretServo");
        turretPitchServo = new StellarServo(hardwareMap, "turretHoodServo");
        leftTurretMotor = new StellarDcMotor(hardwareMap, "leftTurretMotor" );
        rightTurretMotor = new StellarDcMotor(hardwareMap, "rightTurretMotor");

        webcamName = hardwareMap.get(WebcamName.class, "camera");
        //turretYawServo.setPosition(YAW_SERVO_MID);
        leftTurretMotor.setDirection(DcMotorEx.Direction.REVERSE);
        rightTurretMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftTurretMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightTurretMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftTurretMotor.setVelocityPIDFCoefficents(p_left, i_left, d_left, f_left);
        rightTurretMotor.setVelocityPIDFCoefficents(p_right, i_right, d_right, f_right);


    }
    //:todo add on start
    @Override
    public void update() {

    //leftTurretMotor.setVelocityPIDFCoefficents(p_left*PIDFScale, i_left*PIDFScale, d_left*PIDFScale, f_left*PIDFScale);
        //rightTurretMotor.setVelocityPIDFCoefficents(p_right*PIDFScale, i_right*PIDFScale, d_right*PIDFScale, f_right*PIDFScale);
    }
    public boolean getNeedsToStart(){
        return needsToStart;
    }
    public StellarServo getTurretServo() {
        return turretYawServo;
    }

    public StellarServo getTurretHoodServo(){
        return turretPitchServo;
    }

    public StellarDcMotor getLeftTurretMotor() {
        return leftTurretMotor;
    }

    public StellarDcMotor getRightTurretMotor() {
        return rightTurretMotor;
    }

    public WebcamName getWebcamName() {
        return this.webcamName;
    }

    public void setTurretVelocity(double velocity) {
        this.velocity = velocity;

        leftTurretMotor.setTargetVelocity(velocity);
        rightTurretMotor.setTargetVelocity(velocity);
    }

    public void setPIDFScale(double scale){
        PIDFScale = scale;
        leftTurretMotor.setVelocityPIDFCoefficents(p_left * PIDFScale, i_left * PIDFScale, d_left * PIDFScale, f_left * PIDFScale);
        rightTurretMotor.setVelocityPIDFCoefficents(p_right * PIDFScale, i_right * PIDFScale, d_right * PIDFScale, f_right * PIDFScale);
    }

    public double getVelocityOffOfTarget() {
        return Math.abs(leftTurretMotor.getVelocity() - velocity);
    }

    public double getRealVelocityOffOfTarget(){
        return leftTurretMotor.getVelocity() - velocity;
    }

    public boolean velocityWithinTolerance() {
        return getVelocityOffOfTarget() < VELOCITY_TOLERANCE;
    }
    public double getTurretYawAngleTarget() {
        PedroDrivebase pedroDrivebase = subsystem(PedroDrivebase.class);
        double launchYaw = pedroDrivebase.getRealLaunchYaw();
        // Assuming PedroDrivebase returns degrees. If it's radians, use Math.toDegrees first.
        double robotHeading = Math.toDegrees(pedroDrivebase.getFollower().getHeading());

        // This forces the result to stay between -180 and 180
        return AngleUnit.normalizeDegrees(robotHeading - launchYaw);
    }

    public double getBoundedTurretYawAngleTarget() {
        double targetAngle = getTurretYawAngleTarget();
        double boundedTargetAngle;
        if ((targetAngle<-13) && (targetAngle>-90)){
            boundedTargetAngle = -13;
        }
        else if (targetAngle < -90.0){
            boundedTargetAngle= -30.0;
        } else if (targetAngle > 30.0){
            boundedTargetAngle = 30.0;
        } else{
            boundedTargetAngle = targetAngle;
        }
        return boundedTargetAngle;
    }

    public void updateTurretYawServo() {
        double targetAngle = getBoundedTurretYawAngleTarget();

        double servoDegreesOffset = (targetAngle * YAW_GEAR_RATIO);

        double servoPosOffset = servoDegreesOffset / YAW_SERVO_DEGREE_RANGE;


        double finalServoPos = YAW_SERVO_MID - servoPosOffset;
        turretYawServo.setPosition(finalServoPos);

    }
    public void setTurretToForward(){
        turretYawServo.setPosition(YAW_SERVO_MID);
    }


    @NonNull
    @Override
    public String debugTelemetry() {
        return String.format(
                "Turret Servo Pos: %f\n" +
                    "   Hood Pos: %f\n" +
                    "   Turret Left/Right Motor Vel: %f, %f\n" +
                "Turret Target Velocity: %f\n" +
                    "   TurretAtTargetVelocity?: %b\n"+
                "Bounded Turret Yaw Target Angle: %f",
                turretYawServo.getPosition(),
                turretPitchServo.getPosition(),
                leftTurretMotor.getVelocity(),
                rightTurretMotor.getVelocity(),
                velocity,
                velocityWithinTolerance(),
                getBoundedTurretYawAngleTarget()

        );
    }
}