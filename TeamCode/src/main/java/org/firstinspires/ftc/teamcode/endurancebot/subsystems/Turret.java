package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.d_blue;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.d_red;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.f_blue;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.f_red;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.i_blue;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.i_red;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.p_blue;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.TurretPIDFConstants.p_red;
import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
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
    private final static double MIN_TURRET_POWER = .006;
    private final static double TURRET_DEGREES_TOLERANCE = 1.0;
    private final static double TURRET_ZERO_POS = 189.0;

    private final static double MIN_VOLTS = 0.015;
    private final static double MAX_VOLTS = 3.230;

    private final static double COVER_OPEN = 0.0;
    private final static double COVER_CLOSED = 0.12;

    private final static double HOOD_FACTOR = 0.002;

    private final static double YAW_MOTOR_TICKS_HALF = 3000.0;
    private final static double DEGREES_TO_TICKS = YAW_MOTOR_TICKS_HALF / 180.0;

    private final static double PULLEY_RATIO = 60 /109.0;
    private double velocity = 0.0;
    private double error;
    private double externalEncoderVoltage;
    private double lastDegrees;
    private double turretPosition;
    private double externalEncoderRawDegrees;
    private double externalEncoderTotalDegrees;
    private double maxExternalEncoderVoltage;

    private int totalRevolutions = totalCarryoverRevoltions;
    private static int  totalCarryoverRevoltions =  0;

    private boolean launchMode = false;

    double Kp = 0.02;
    double Ki = 0.008;
    double Kd = 0.002;

    double integralSum = 0;
    double lastError = 0;

    ElapsedTime timer = new ElapsedTime();
    private CRServo turretYaw;
    private StellarServo turretPitch, cover;
    private StellarDcMotor turretTop, turretBottom;

    private AnalogInput externalEncoder;

    private WebcamName webcamName;

    private double PIDFScale;
    private boolean needsToStart = true;

    @Override
    public void init(HardwareMap hardwareMap) {
        PIDFScale = 1.0;
        needsToStart = true;
        turretYaw = hardwareMap.get(CRServo.class, "turretRotation");
        turretPitch = new StellarServo(hardwareMap, "turretPitch");
        turretTop = new StellarDcMotor(hardwareMap, "turretTop" );
        turretBottom = new StellarDcMotor(hardwareMap, "turretBottom");
        cover = new StellarServo(hardwareMap, "coverServo");

        externalEncoder = hardwareMap.get(AnalogInput.class, "externalEncoder");

        webcamName = hardwareMap.get(WebcamName.class, "camera");
        //turretYawServo.setPosition(YAW_SERVO_MID);
        turretTop.setDirection(DcMotorEx.Direction.REVERSE);
        turretBottom.setDirection(DcMotorEx.Direction.REVERSE);

        turretTop.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        turretBottom.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);



        turretTop.setVelocityPIDFCoefficents(p_red, i_red, d_red, f_red);
        turretBottom.setVelocityPIDFCoefficents(p_blue, i_blue, d_blue, f_blue);

        cover.setPosition(COVER_CLOSED);

        maxExternalEncoderVoltage = externalEncoder.getMaxVoltage();

        lastDegrees = voltageToDegrees(externalEncoder.getVoltage());
        error = 0;
    }
    //:todo add on start
    @Override
    public void update() {
        updateExternalEncoder();
    //leftTurretMotor.setVelocityPIDFCoefficents(p_left*PIDFScale, i_left*PIDFScale, d_left*PIDFScale, f_left*PIDFScale);
        //rightTurretMotor.setVelocityPIDFCoefficents(p_right*PIDFScale, i_right*PIDFScale, d_right*PIDFScale, f_right*PIDFScale);
    }
    public boolean getNeedsToStart(){
        return needsToStart;
    }
    //public StellarServo getTurretServo() {
    // return turretYawServo;
    //}
    public void setLaunchMode(boolean mode){
        launchMode = mode;
    }
    public void setTotalCarryoverRevoltions(int carryoverRevoltions){
        totalCarryoverRevoltions = carryoverRevoltions;
    }
    public int getTotalRevoltions(){
        return totalRevolutions;
    }
    public void updateExternalEncoder(){
        externalEncoderRawDegrees = voltageToDegrees(externalEncoder.getVoltage());
        double delta = externalEncoderRawDegrees-lastDegrees;
        if (delta< -180.0){
            totalRevolutions++;
        }
        if (delta> 180.0){
            totalRevolutions--;
        }
        externalEncoderTotalDegrees = externalEncoderRawDegrees + ((totalRevolutions)*360.0);
        turretPosition = (externalEncoderTotalDegrees*PULLEY_RATIO)-TURRET_ZERO_POS;
        lastDegrees = externalEncoderRawDegrees;


    }
    public double voltageToDegrees(double voltage){
        return ((voltage-MIN_VOLTS)/(MAX_VOLTS-MIN_VOLTS)) * 360.0;
    }

    public StellarServo getTurretPitchServo(){
        return turretPitch;
    }

    public StellarServo getTurretCoverServo(){return cover;}

    public CRServo getTurretYawServo(){return turretYaw;}

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

    public double getRealBoundedVelocityOffOfTarget() {
        if (getVelocityOffOfTarget() > 140.0){
            if (getRealVelocityOffOfTarget() > 0.0){
                return 140.0;
            } else {
                return -140.0;
            }
        }
        if (getVelocityOffOfTarget() < 20.0){
            return 0.0;
        } else return getRealVelocityOffOfTarget();
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

        double pitchAdjusted;
        if (launchMode){
            pitchAdjusted = getRealBoundedVelocityOffOfTarget() * HOOD_FACTOR + parameters.getAngle();
        }else {
            pitchAdjusted = parameters.getAngle();
        }
        turretPitch.setPosition(pitchAdjusted);
    }

    public double getBoundedTurretYawAngleTarget() {
        double targetAngle = getTurretYawAngleTarget();
        double boundedTargetAngle;
        if ((targetAngle < -160)){
            boundedTargetAngle = -160.0;
        } else if (targetAngle > 160.0){
            boundedTargetAngle = 160.0;
        } else{
            boundedTargetAngle = targetAngle;
        }
        return boundedTargetAngle;
    }
    public void updateTurretYawCRServo(){
        double error = turretPosition-getBoundedTurretYawAngleTarget();
        turretYaw.setPower(calculateTurretYawPower(error));
    }
    public double calculateTurretYawPower(double error) {
        // 1. Get the time elapsed since the last loop and reset the timer
        this.error = error;
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
        if (Math.abs(error) > TURRET_DEGREES_TOLERANCE){
            if(totalPower>0 && totalPower < MIN_TURRET_POWER){
                totalPower = MIN_TURRET_POWER;
            } else if (totalPower<0 && totalPower>-MIN_TURRET_POWER){
                totalPower =-MIN_TURRET_POWER;
            }
        } else{
            totalPower = 0.0;
        }
        return Range.clip(totalPower, -1.0, 1.0);
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
                "   Hood Pos: %f\n" +
                        "   Turret Top/Bottom Motor Vel: %f, %f\n" +
                        "Turret Target Velocity: %f\n" +
                        "Cover Position: %f\n" +
                        "Yaw Power: %f\n" +
                        "   TurretAtTargetVelocity?: %b\n" +
                        "Bounded Turret Yaw Target Angle: %f\n"+
                        "External Encoder Voltage:  %f\n"+
                        "External Encoder Total Degrees: %f\n" +
                        "Turret Current Degrees: %f\n"+
                        "error: %f"+
                        "totalRevolotions: %o"
                ,// Cleaned up comments and fixed comma placement
                turretPitch.getPosition(),
                turretTop.getVelocity(),
                turretBottom.getVelocity(),
                velocity,
                cover.getPosition(),
                turretYaw.getPower(),
                velocityWithinTolerance(), // Perfectly matches %b
                getBoundedTurretYawAngleTarget(),
                externalEncoderVoltage,
                externalEncoderTotalDegrees,
                turretPosition,
                error,
                totalRevolutions
        );
    }
}