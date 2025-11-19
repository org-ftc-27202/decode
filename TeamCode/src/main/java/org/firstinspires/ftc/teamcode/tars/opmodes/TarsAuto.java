package org.firstinspires.ftc.teamcode.tars.opmodes;// make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.tars.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

@Autonomous(name = "Pedro-", group = "Auto")
public class TarsAuto extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    final StellarBot tars = new StellarBot(
            Drivebase.getInstance(),
            Intake.getInstance(),
            LeverTransfer.getInstance(),
            Spindexer.getInstance()
    );
    //DefaultDrivebase defaultDrivebase = new DefaultDrivebase(gamepad1);
    DefaultIntake defaultIntake = new DefaultIntake(gamepad1);
    DefaultLeverTransfer defaultLeverTransfer = new DefaultLeverTransfer(gamepad1);
    DefaultSpindexer defaultSpindexer = new DefaultSpindexer(gamepad1);

    private final Drivebase drivebase = Drivebase.getInstance();
    private final Intake intake = Intake.getInstance();
    private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
    private final Spindexer spindexer = Spindexer.getInstance();

    private final Pose startPose = new Pose(56.75,7, Math.toRadians(180));
    private final Pose collect1Pose = new Pose(19, 35.5);
    private final Pose collect1Control = new Pose(56,35.5);
    private final Pose launchFarPose = new Pose(60,21);
    private PathChain path1, path2, path3, path4, path5;

    public void buildPaths() {
        this.path1 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(startPose, collect1Control, collect1Pose)
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(180))
                .setBrakingStart(2)
                .setBrakingStrength(2)
                .build();
        this.path2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(collect1Pose, launchFarPose)
                )
                .setTangentHeadingInterpolation()
                .setBrakingStart(2)
                .setBrakingStrength(2)
                .setReversed()
                .build();
    }

    @Override
    public void init() {
        this.follower = Constants.createFollower(hardwareMap);
        tars.init(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        telemetry.addLine(
                "\n                                 █\n" +
                        "                                 █              █\n" +
                        "                                 █              █              █\n" +
                        "                                                  █              █\n" +
                        "                       ▄███▄                             █\n" +
                        "                    ▐█████▌██████▄\n" +
                        "        ▌            ▀███▀▄███████▄                  ▄▄\n" +
                        "        █                  ▄███████████▌     ▄▄███▀\n" +
                        "        ▐▄    ▐██████▀      ▀▀▀▀████████▀\n" +
                        "              ▀▀▀▀                                          ▀▀"
        );
    }

    @Override
    public void start() {
        new Procedure(
                "AutoDrive",
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
                new Sleep(0.03),

                        new FollowPath(path1, follower, collect1Pose, true),
                        //new FullIntake(),
                        //new SetPower(Intake.getInstance().getIntakeMotor(), 0.5),
                new FollowPath(path2, follower, launchFarPose, true),
                new FullOuttake()
        ).schedule();
    }

    @Override
    public void loop() {
        tars.update();
        follower.update();
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("isBusy(): ", follower.isBusy());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("T: ", follower.getCurrentTValue());
        telemetry.addLine(tars.toString());
        telemetry.update();
    }
}