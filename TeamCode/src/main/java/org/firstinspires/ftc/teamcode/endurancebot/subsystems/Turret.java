package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.*;


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
import org.firstinspires.ftc.teamcode.util.LaunchInterpolator;
import org.firstinspires.ftc.teamcode.util.LaunchParameters;

public final class Turret extends Subsystem {
    private final static double VELOCITY_TOLERANCE = 41.0;
    private final static double YAW_SERVO_DEGREE_RANGE = 330.0;
    private final static double YAW_GEAR_RATIO = 1.167;
    private final static double YAW_SERVO_MID = 0.82;

    private final static double COVER_OPEN = 0.0;
    private final static double COVER_CLOSED = 0.25;

    private double velocity = 0.0;

    private StellarServo turretYaw, turretPitch, cover;
    private StellarDcMotor turretTop, turretBottom;
    private WebcamName webcamName;

    private double PIDFScale;
    private boolean needsToStart = true;

    @Override
    public void init(HardwareMap hardwareMap) {
        PIDFScale = 1.0;
        needsToStart = true;
        turretYaw = new StellarServo(hardwareMap, "turretYaw");
        turretPitch = new StellarServo(hardwareMap, "turretPitch");
        turretTop = new StellarDcMotor(hardwareMap, "turretTop" );
        turretBottom = new StellarDcMotor(hardwareMap, "turretBottom");
        cover = new StellarServo(hardwareMap, "coverServo");

        webcamName = hardwareMap.get(WebcamName.class, "camera");
        //turretYawServo.setPosition(YAW_SERVO_MID);
        turretTop.setDirection(DcMotorEx.Direction.REVERSE);
        turretBottom.setDirection(DcMotorEx.Direction.REVERSE);

        turretTop.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        turretBottom.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        turretTop.setVelocityPIDFCoefficents(p_red, i_red, d_red, f_red);
        turretBottom.setVelocityPIDFCoefficents(p_blue, i_blue, d_blue, f_blue);


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
    //public StellarServo getTurretServo() {
    // return turretYawServo;
    //}

    public StellarServo getTurretPitchServo(){
        return turretPitch;
    }

    public StellarServo getTurretCoverServo(){return cover;}

    public StellarServo getTurretYawServo(){return turretYaw;}

    public StellarDcMotor getTurretTop() {
        return turretTop;
    }

    public StellarDcMotor getTurretBottom() {
        return turretBottom;
    }

    public WebcamName getWebcamName() {
        return this.webcamName;
    }

    public void setTurretVelocity(double velocity) {
        this.velocity = velocity;

        turretTop.setTargetVelocity(velocity);
        turretBottom.setTargetVelocity(velocity);
    }

    public void setPIDFScale(double scale){
        PIDFScale = scale;
        turretTop.setVelocityPIDFCoefficents(p_red * PIDFScale, i_red * PIDFScale, d_red * PIDFScale, f_red * PIDFScale);
        turretBottom.setVelocityPIDFCoefficents(p_blue * PIDFScale, i_blue * PIDFScale, d_blue * PIDFScale, f_blue * PIDFScale);
    }

    public double getVelocityOffOfTarget() {
        return Math.abs(turretTop.getVelocity() - velocity);
    }

    public double getRealVelocityOffOfTarget(){
        return turretTop.getVelocity() - velocity;
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

    public void updateTurretWithInterpolation(double distance){
        LaunchParameters parameters = LaunchInterpolator.getEstimatedLaunchParameters(distance);
        setTurretVelocity(parameters.getVelocity());
        turretPitch.setPosition(parameters.getAngle());
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
        // turretYawServo.setPosition(finalServoPos);

    }
    public void setTurretToForward(){
    //    turretYawServo.setPosition(YAW_SERVO_MID);
    }

    public void setCoverOpen(){
       cover.setPosition(COVER_OPEN);
    }
    public void setCoverClosed(){
        cover.setPosition(COVER_CLOSED);
    }


    @NonNull
    @Override
    public String debugTelemetry() {
        return String.format(
                //"Turret Servo Pos: %f\n" +
                    "   Hood Pos: %f\n" +
                    "   Turret Left/Right Motor Vel: %f, %f\n" +
                "Turret Target Velocity: %f\n" +
                    "   TurretAtTargetVelocity?: %b\n"+
                "Bounded Turret Yaw Target Angle: %f",
                //turretYawServo.getPosition(),
                turretPitch.getPosition(),
                turretTop.getVelocity(),
                turretBottom.getVelocity(),
                velocity,
                velocityWithinTolerance(),
                getBoundedTurretYawAngleTarget()
        );
    }
}