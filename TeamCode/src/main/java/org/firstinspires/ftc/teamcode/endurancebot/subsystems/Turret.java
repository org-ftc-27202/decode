package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.*;


import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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

    private final static double COVER_OPEN = 0.95;
    private final static double COVER_CLOSED = 0.90;

    private double velocity = 0.0;

    double Kp = 0.0;
    double Ki = 0.0;
    double Kd = 0.0;

    double integralSum = 0;
    double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    private double TICKS_FULL_ROTATION;


    private StellarServo turretPitch, cover, turretYaw;
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

        //turretYaw.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        turretTop.setVelocityPIDFCoefficents(p_red, i_red, d_red, f_red);
        turretBottom.setVelocityPIDFCoefficents(p_blue, i_blue, d_blue, f_blue);

        cover.setPosition(COVER_CLOSED);
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
        //setTurretVelocity(parameters.getVelocity());
        setTurretVelocity(1570);
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
    public double calculateTurretYawPower(double error) {
        // 1. Get the time elapsed since the last loop and reset the timer
        double seconds = timer.seconds();
        timer.reset();

        // 2. Proportional
        double proportional = error * Kp;

        // 3. Integral
        // Add the error multiplied by the time step to our running total
        integralSum += (error * seconds);

        // Optional: Anti-windup cap for the integral sum
        // Limits how huge the integral can get so the turret doesn't go crazy
        double maxIntegralSum = 0.5; // Adjust as needed
        integralSum = Range.clip(integralSum, -maxIntegralSum, maxIntegralSum);

        double integral = integralSum * Ki;

        // 4. Derivative
        // Calculate how fast the error is changing
        double derivative = 0;
        if (seconds > 0) { // Prevent divide-by-zero on the very first loop
            derivative = ((error - lastError) / seconds) * Kd;
        }

        // 5. Update lastError for the next loop
        lastError = error;

        // 6. Calculate total power and clamp it between -1.0 and 1.0
        double totalPower = proportional + integral + derivative;
        return Range.clip(totalPower, -1.0, 1.0);
    }
    public void updateTurretYawServo() {
        double targetAngle = getBoundedTurretYawAngleTarget();

        double servoDegreesOffset = (targetAngle * YAW_GEAR_RATIO);

        double servoPosOffset = servoDegreesOffset / YAW_SERVO_DEGREE_RANGE;


        double finalServoPos = YAW_SERVO_MID - servoPosOffset;
        // turretYawServo.setPosition(finalServoPos);

    }
    public void  updateTurretYawMotor(){
        double targetAngle = Math.toRadians(getTurretYawAngleTarget());
        if (Math.abs(targetAngle) >= TICKS_FULL_ROTATION){
            targetAngle = TICKS_FULL_ROTATION;
        }
        //turretYaw.setPower(targetAngle);


    }
    public void setTurretToForward(){
    //    turretYawServo.setPosition(YAW_SERVO_MID);
    }

    public void setCoverOpen() {
       cover.setPosition(COVER_OPEN);
    }
    public void setCoverClosed() {
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
                "Bounded Turret Yaw Target Angle: %f\n"+
                            /*" YawPower: %f\n"+
                "YawPosition?: %f\n",*/
                //turretYawServo.getPosition(),
                turretPitch.getPosition(),
                turretTop.getVelocity(),
                turretBottom.getVelocity(),
                velocity,
                velocityWithinTolerance(),
                getBoundedTurretYawAngleTarget()
                /*turretYaw.getPower(),
                turretYaw.getCurrentPosition()*/
        );
    }
}