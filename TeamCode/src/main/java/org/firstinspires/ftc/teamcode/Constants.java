package org.firstinspires.ftc.teamcode;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(14.6)
            .forwardZeroPowerAcceleration(-54.226777043124066)
            .lateralZeroPowerAcceleration(-167.19912852848407)
//            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0.01, 0))
//            .headingPIDFCoefficients(new PIDFCoefficients(1.0, 0, 0.01, 0.024))
//            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.008,0.0,0.001,0.6,0.025))
//            .centripetalScaling(0.00065)
            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99,
        5,
        1.6,
        1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftRearMotorName("leftRear")
            .leftFrontMotorName("leftFront")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .useBrakeModeInTeleOp(true)
            .xVelocity(107.08984066233836)
            .yVelocity(107.87492885849001)
            ;

//    public static PinpointConstants localizerConstants = new PinpointConstants()
//            .forwardPodY(24/2.54)
//            .strafePodX(-24/2.54)
//            .distanceUnit(DistanceUnit.MM)
//            .hardwareMapName("pinpoint")
//            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
//            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
//            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftRearMotorName("leftRear")
            .leftFrontMotorName("leftFront")
            .leftFrontEncoderDirection(Encoder.FORWARD)
            .leftRearEncoderDirection(Encoder.FORWARD)
            .rightFrontEncoderDirection(Encoder.REVERSE)
            .rightRearEncoderDirection(Encoder.REVERSE)
            .robotWidth(15.354)
            .robotLength(12.284)
            .forwardTicksToInches(0.010289649839655704)
            .strafeTicksToInches(0.012533626888163188)
            .turnTicksToInches(0.012084528567837781);
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
//                .pinpointLocalizer(localizerConstants)
                .driveEncoderLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}