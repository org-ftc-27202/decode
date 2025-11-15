package org.firstinspires.ftc.teamcode.tars.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarLight;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetLight;

import java.util.function.BooleanSupplier;

//todo: make init auto and init tele
public final class PedroDrivebase extends Subsystem {
    private static final PedroDrivebase drivebase = new PedroDrivebase();

    private Follower follower;


    public static PedroDrivebase getInstance() {
        return drivebase;
    }

    private PedroDrivebase() {}

    public final static double CARDINAL_SPEED = 0.70;
    public final static double TURN_SPEED = 0.55;
    public final static double FAR_LAUNCH_FACTOR = .2;
    public final static double NEAR_LAUNCH_FACTOR = .4;
    public final static double LAUNCH_ZONE_SCALE = 6.2;

    public double speedScale = 1;

    private DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    private StellarLight light;

    @Override
    public void init(HardwareMap hardwareMap) {
        this.follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(56.75,7, Math.toRadians(180)));
        light = new StellarLight(hardwareMap, "light");
        //todo: make omni wheel directive
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightBack");

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorEx.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorEx.Direction.FORWARD);

        leftFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        new SetLight(light, "AZURE");
    }

    @Override
    public void update() {follower.update();}


    public boolean checkForLaunchPose() {

            double x = follower.getPose().getX();
            double y = follower.getPose().getY();

            if (y > 48) {
                if (x <= 72) {
                    return y >= (-x + 138-LAUNCH_ZONE_SCALE);
                } else if (x > 72) {
                    return y >= (x - 6-LAUNCH_ZONE_SCALE);
                }
            } else if (y <= 48) {
                if (x <= 72) {
                    return y <= (x - 42+LAUNCH_ZONE_SCALE);
                } else if (x > 72) {
                    return y <= (-x + 102+LAUNCH_ZONE_SCALE);
                }
            }
            return false;

    }
    public StellarLight getLight(){return light;}

    public void setSpeedScale(double speedScale){
        this.speedScale = speedScale;
    }

    public void setDrivePower(double leftFrontPower, double rightFrontPower, double leftBackPower, double rightBackPower) {
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }

	@NonNull
    @Override
    public String toString() {
        return String.format(
                        "Left Front: %.2f\n" +
                        "Right Front: %.2f\n" +
                        "Left Back: %.2f\n" +
                        "Right Back: %.2f\n" +
                        "X: %.3f\n" +
                        "Y: %.3f\n" +
                        "Heading(degrees): %.2f\n" +
                        "In Launch Zone: %b\n "+
                        "Light Color: %f",
                leftFrontDrive.getPower(),
                rightFrontDrive.getPower(),
                leftBackDrive.getPower(),
                rightBackDrive.getPower(),
                follower.getPose().getX(),
                follower.getPose().getY(),
                Math.toDegrees(follower.getPose().getHeading()),
                checkForLaunchPose(),
                light.getPosition());
    }
}