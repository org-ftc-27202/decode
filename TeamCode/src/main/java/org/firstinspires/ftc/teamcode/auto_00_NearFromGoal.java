package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

public abstract class auto_00_NearFromGoal extends OpMode {
    private final double CENTER_X = 72.0;
    public enum AllianceColors {RED, BLUE};
    public static AllianceColors allianceColor = AllianceColors.RED;
    public static botVisionFront.PatternOptions patternMatch = botVisionFront.PatternOptions.GPP;
    public void setAllianceColor(AllianceColors inAllianceColor) {
        allianceColor = inAllianceColor;
    }
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        // Start POSITION_END Position
        // Drive
        // Shoot
        DRIVE_STARTPOS_TO_GETPATTERNPOS, GETPATTERN, LAUNCHPOS1
    }

    PathState pathState;

    private Pose startPose = new Pose(112, 135.5, Math.toRadians(90));
    private Pose getPatternPose = new Pose(92, 92, Math.toRadians(90));
    private Pose Launch1Pose = new Pose(92, 92, Math.toRadians(45));

    private PathChain driveFromStartToGetPattern;

    public void buildPaths() {
        if (allianceColor == AllianceColors.BLUE) {
            // mirror the x coordinate and heading
            startPose = new Pose(CENTER_X - (startPose.getX() - CENTER_X), startPose.getY(), Math.toRadians(90));
            getPatternPose = new Pose(CENTER_X - (getPatternPose.getX() - CENTER_X), getPatternPose.getY(), Math.toRadians(90));
        };

        driveFromStartToGetPattern = follower.pathBuilder()
                .addPath(new BezierLine(startPose, getPatternPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), getPatternPose.getHeading())
                .setLinearHeadingInterpolation(getPatternPose.getHeading(), Launch1Pose.getHeading())
                .build();
    }

    public void statePathUpdate() {
        switch (pathState) {
            case DRIVE_STARTPOS_TO_GETPATTERNPOS:
                follower.followPath(driveFromStartToGetPattern, true);
                setPathState(PathState.GETPATTERN);
                break;
            case GETPATTERN:
                if (!follower.isBusy()) {
                    telemetry.addData("Done", pathState.toString());
                }
                break;
            case LAUNCHPOS1:
                if (!follower.isBusy()) {
                    telemetry.addData("Done", pathState.toString());
                }
                break;
            default:
                telemetry.addLine("No State Commanded");
                break;
        }

    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_STARTPOS_TO_GETPATTERNPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        // ToDo: other init tasks

        buildPaths();
        follower.setPose(startPose);
    }

    @Override
    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Alliance", allianceColor.toString());
        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("path time", pathTimer.getElapsedTimeSeconds());
    }}