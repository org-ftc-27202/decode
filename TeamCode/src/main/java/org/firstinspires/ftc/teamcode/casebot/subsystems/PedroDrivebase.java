package org.firstinspires.ftc.teamcode.casebot.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarLight;

//todo: make init auto and init tele
public final class PedroDrivebase extends Subsystem {
    private static Follower follower;
    public final static double CARDINAL_SPEED = 1.00;
    public final static double TURN_SPEED = 0.70;
    public final static double FAR_LAUNCH_FACTOR = 0.2;
    public final static double NEAR_LAUNCH_FACTOR = 0.4;
    public final static double LAUNCH_ZONE_SCALE = 6.2;
    private double GOAL_X, GOAL_Y, REAL_GOAL_X, REAL_GOAL_Y;
    private opModeType opMode;
    private boolean isEndgame = false;

    public enum opModeType {
        AUTO,
        TELEOP
    }

    public void setOpMode(@NonNull opModeType opMode) {
        this.opMode = opMode;
    }

    public double distanceFromGoal;

    public double speedScale = 1.0;
    private boolean needsToStart = true;
    private static boolean firstInit = true;

    private DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    private StellarLight lightLeft, lightRight;


    @Override
    public void init(HardwareMap hardwareMap) {
        double STARTING_X, STARTING_Y;
        // BLUE LAUNCH
        // GOAL X 8.0
        // GOAL Y 143.0

        // RED LAUNCH
        // GOAL X 136.0
        // GOAL Y 143.0

        StellarBot.AllianceColor allianceColor = StellarBot.getInstance().getAllianceColor();
        if (allianceColor == StellarBot.AllianceColor.BLUE) {
            GOAL_X = -18.0;
            GOAL_Y = 144.0;

            REAL_GOAL_X = 6.0;
            REAL_GOAL_Y = 144.0;

            STARTING_X = 55.25;
            STARTING_Y = 9.0;
        } else if (allianceColor == StellarBot.AllianceColor.RED) {
            GOAL_X = 162.0;
            GOAL_Y = 144.0;

            REAL_GOAL_X = 138.0;
            REAL_GOAL_Y = 144.0;

            STARTING_X = 88.75;
            STARTING_Y = 9.0;
        } else {
            throw new IllegalArgumentException("PedroDrivebase needs set alliance color");
        }

        needsToStart = true;
        if (follower == null || opMode == opModeType.AUTO) {
            follower = Constants.createFollower(hardwareMap);
        }

        if (opMode == opModeType.AUTO) {
            follower.setStartingPose(new Pose(STARTING_X, STARTING_Y, Math.toRadians(90)));
            firstInit = false;
        } else if (opMode == opModeType.TELEOP && firstInit){
            follower.setStartingPose(new Pose(STARTING_X, STARTING_Y, Math.toRadians(90)));
            firstInit = false;
        }


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

        lightLeft.setPosition(0.555);
        lightRight.setPosition(0.555);
    }


    @Override
    public void update() {
        if (needsToStart){
            follower.startTeleopDrive(true);
            needsToStart = false;
        }
        follower.update();
        distanceFromGoal = Math.sqrt(Math.pow(follower.getPose().getX() - GOAL_X, 2) + Math.pow(follower.getPose().getY() - GOAL_Y, 2));
    }

    public Follower getFollower(){
        return follower;
    }

    public void setEndgame(){
        this.isEndgame = true;
    }


    public boolean inWrongSideEndgame() {
        StellarBot.AllianceColor allianceColor = StellarBot.getInstance().getAllianceColor();
        if (isEndgame) {
            double x = follower.getPose().getX();
            if (allianceColor == StellarBot.AllianceColor.BLUE) {
                return x < 72.0;
            }
            if (allianceColor == StellarBot.AllianceColor.RED) {
                return x > 72.0;
            }

        }
        return false;
    }

    public boolean checkForLaunchPose() {
            double x = follower.getPose().getX();
            double y = follower.getPose().getY();

            if (y > 48.0) {
                if (x <= 72.0) {
                    return y >= (-x + 138.0 - LAUNCH_ZONE_SCALE);
                } else if (x > 72.0) {
                    return y >= (x - 6.0 - LAUNCH_ZONE_SCALE);
                }
            } else if (y <= 48.0) {
                if (x <= 72.0) {
                    return y <= (x - 42.0 + LAUNCH_ZONE_SCALE);
                } else if (x > 72.0) {
                    return y <= (-x + 102.0 + LAUNCH_ZONE_SCALE);
                }
            }
            return false;

    }
    public double getLaunchYaw() {
        double x = GOAL_X - follower.getPose().getX();
        double y = GOAL_Y - follower.getPose().getY();

        // Use atan2(y, x) to get the correct angle in radians (-PI to +PI)
        double angleRadians = Math.atan2(y, x);

        // Convert to degrees (-180 to +180)
        return Math.toDegrees(angleRadians);
    }
    public double getRealLaunchYaw(){

        double x = REAL_GOAL_X - follower.getPose().getX();
        double y = REAL_GOAL_Y - follower.getPose().getY();

        // Use atan2(y, x) to get the correct angle in radians (-PI to +PI)
        double angleRadians = Math.atan2(y, x);

        // Convert to degrees (-180 to +180)
        return Math.toDegrees(angleRadians);

    }
    public StellarLight getLeftLight() {
        return this.lightLeft;
    }

    public StellarLight getRightLight() {
        return this.lightRight;
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
    public String debugTelemetry() {
        return String.format(
                "Left Front: %.2f\n" +
                "Right Front: %.2f\n" +
                "Left Back: %.2f\n" +
                "Right Back: %.2f\n" +
                "X: %.3f\n" +
                "Y: %.3f\n" +
                "Heading(degrees): %.2f\n" +
                "Distance From Goal: %.3f\n"+
                "Launch Yaw: %.3f\n"+
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
                getLaunchYaw(),
                checkForLaunchPose(),
                lightLeft.getPosition(),
                lightRight.getPosition()
          );
    }
}
