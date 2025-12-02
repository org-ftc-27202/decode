package org.firstinspires.ftc.teamcode.casebot.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.SetLight;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarLight;

//todo: make init auto and init tele
public final class PedroDrivebase extends Subsystem {
    private static final PedroDrivebase drivebase = new PedroDrivebase();
    public static PedroDrivebase getInstance() {
        return drivebase;
    }
    private PedroDrivebase() {}

    private Follower follower;
    public final static double CARDINAL_SPEED = 1.00;
    public final static double TURN_SPEED = 0.70;
    public final static double FAR_LAUNCH_FACTOR = 0.2;
    public final static double NEAR_LAUNCH_FACTOR = 0.4;
    public final static double LAUNCH_ZONE_SCALE = 6.2;
    public final static double GOAL_X = 11.0;
    public final static double GOAL_Y = 141.0;



    public double distanceFromGoal;

    public double speedScale = 1.0;

    private DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    private StellarLight lightLeft, lightRight;

    @Override
    public void init(HardwareMap hardwareMap) {
        this.follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(56.75,7, Math.toRadians(180)));
        lightLeft = new StellarLight(hardwareMap, "lightLeft");
        lightRight = new StellarLight(hardwareMap, "lightRight");
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

        new SetLight(lightLeft, "AZURE").schedule();
        new SetLight(lightRight, "AZURE").schedule();
    }

    @Override
    public void update() {
        follower.update();
        distanceFromGoal = Math.sqrt(Math.pow(follower.getPose().getX()-GOAL_X, 2)+Math.pow(follower.getPose().getY()-GOAL_Y,2));
    }
    public Follower getFollower(){
        return follower;
    }


    public boolean checkForLaunchPose() {

            double x = follower.getPose().getX();
            double y = follower.getPose().getY();

            if (y > 48) {
                if (x <= 72) {
                    return y >= (-x + 138 - LAUNCH_ZONE_SCALE);
                } else if (x > 72) {
                    return y >= (x - 6 - LAUNCH_ZONE_SCALE);
                }
            } else if (y <= 48) {
                if (x <= 72) {
                    return y <= (x - 42 + LAUNCH_ZONE_SCALE);
                } else if (x > 72) {
                    return y <= (-x + 102 + LAUNCH_ZONE_SCALE);
                }
            }
            return false;

    }
    public StellarLight getLeftLight() {
        return lightLeft;
    }

    public StellarLight getRightLight() {
        return lightRight;
    }

    public void setSpeedScale(double speedScale){
        this.speedScale = speedScale;
    }

    public void setDrivePower(double leftFrontPower, double rightFrontPower, double leftBackPower, double rightBackPower) {
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }
    public double getDistanceFromGoal(){
        return distanceFromGoal;
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
                                "Distance From Goal: %.3f\n"+
                        "In Launch Zone: %b\n "+
                        "Left Light Color: %f" +
                        "Right Light Color: %f",
                leftFrontDrive.getPower(),
                rightFrontDrive.getPower(),
                leftBackDrive.getPower(),
                rightBackDrive.getPower(),
                follower.getPose().getX(),
                follower.getPose().getY(),
                Math.toDegrees(follower.getPose().getHeading()),
                distanceFromGoal,
                checkForLaunchPose(),
                lightLeft.getPosition(),
                lightRight.getPosition());
    }
}