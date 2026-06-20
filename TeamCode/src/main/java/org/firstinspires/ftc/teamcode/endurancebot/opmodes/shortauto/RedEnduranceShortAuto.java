package org.firstinspires.ftc.teamcode.endurancebot.opmodes.shortauto;// make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.endurancebot.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Race;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "RED SHORT Auto ", group = "Auto")
public final class RedEnduranceShortAuto extends OpMode {

    private final double PRE_MATCH_DELAY = 0.0;

    private final StellarBot enduranceBot = StellarBot.getInstance();
    private final PedroDrivebase pedroDrivebase = new PedroDrivebase();
    private final Intake intake = new Intake();
    private final Transfer transfer = new Transfer();
    //private final Spindexer spindexer = new Spindexer();
    private final Turret turret = new Turret();
    private final Camera camera = new Camera();

    private Follower follower;
    private boolean turretStarted = false;

    private long lastCycleTime = 0;

    private final Pose startPose = new Pose(36.0,137.0, Math.toRadians(0)).mirror();
    private final Pose cameraPose = new Pose(53.5, 80.0, Math.toRadians(60));
    private final Pose firstLaunchPose = new Pose(50.0, 82.5, Math.toRadians(180)).mirror();
    private final Pose launchControlPose = new Pose(53.5, 80.0, Math.toRadians(135));
    private final Pose spike1Control = new Pose(43,35.5, Math.toRadians(180));
    private final Pose spike1Start = new Pose(33,35.5, Math.toRadians(180));
    private final Pose spike1End = new Pose(12 ,35.5, Math.toRadians(180));

    private final Pose spike2Control = new Pose(43,59, Math.toRadians(180)).mirror();
    private final Pose spike2Start = new Pose(34,59, Math.toRadians(180));
    private final Pose spike2End = new Pose(17,59, Math.toRadians(180)).mirror();
    private final Pose spike3Control = new Pose(43,82.5, Math.toRadians(180)).mirror();
    private final Pose spike3Start = new Pose(34,82.5, Math.toRadians(180));
    private final Pose spike3End = new Pose(17,82.5, Math.toRadians(180)).mirror();
    private final Pose gateApr = new Pose(26,76, Math.toRadians(180)).mirror();
    private final Pose gateHold = new Pose(15, 76, Math.toRadians(180)).mirror();

    //  private final Pose collect1Pose = new Pose(19, 35.5);

    //  private final Pose collect1Control = new Pose(56,35.5);
    //  private final Pose launchFarPose = new Pose(60,21);
    private PathChain driveToSpike2Start, driveToThirdLaunch, driveToSpike2Collect, driveToApr2Gate, driveToFirstLaunch, driveToLeave, driveToSpike3Start, driveToSpike3Collect, driveToAprGate, driveToOpenGate, driveToSecondLaunch;

    public void buildPaths() {
        driveToFirstLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), firstLaunchPose.getHeading())
                .build();
        driveToSpike3Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose,spike3Control )
                )
                .setLinearHeadingInterpolation(firstLaunchPose.getHeading(), spike3Control.getHeading())
                .build();
        driveToSpike3Collect = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3Control, spike3End)
                )
                .setLinearHeadingInterpolation(spike3Control.getHeading(), spike3End.getHeading())
                .build();
        driveToAprGate = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3End, gateApr)
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        driveToOpenGate = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateApr, gateHold)
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        driveToSecondLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateHold, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(gateHold.getHeading(), Math.toRadians(0))
                .build();
        driveToSpike2Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose,spike2Control )
                )
                .setLinearHeadingInterpolation(firstLaunchPose.getHeading(), spike2Control.getHeading())
                .build();
        driveToSpike2Collect = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2Control, spike2End)
                )
                .setLinearHeadingInterpolation(spike2Control.getHeading(), spike2End.getHeading())
                .build();
        driveToApr2Gate = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3End, gateApr)
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
        driveToThirdLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateHold, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(gateHold.getHeading(), Math.toRadians(0))
                .build();

        driveToLeave = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose, gateApr)
                )
                .setLinearHeadingInterpolation(firstLaunchPose.getHeading(), gateApr.getHeading())
                .build();


    }

    @Override
    public void init() {
        enduranceBot.setupBot(
                StellarBot.AllianceColor.RED,
                pedroDrivebase,
                intake,
                transfer,
                //spindexer,
                turret,
                camera
        );

        //this.follower = Constants.createFollower(hardwareMap);
        pedroDrivebase.setOpMode(PedroDrivebase.opModeType.AUTO);
        pedroDrivebase.setAutoSide(PedroDrivebase.AutoSide.SHORT);
        enduranceBot.init(hardwareMap);
        enduranceBot.setPrintDebug(true);
        follower = pedroDrivebase.getFollower();
        buildPaths();




        // print telemetry
        BootScreen bootScreen = new BootScreen(telemetry, new TerminalVelocityLogo(), true);
        try {
            bootScreen.updateBootScreen();
        } catch (Exception e) {
            telemetry.addData("telemetry didn't work", e);
        }
        telemetry.update();
    }

    @Override
    public void start() {
        new Procedure(
                "AutoDrive",
                new FollowPath(driveToFirstLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(1.0);
                    intake.getIntakeMotor().setPower(1.0);}),
                new Race("race",
                        new FollowPath(driveToSpike3Start, follower, spike3Control, false, 1.0),
                        new Sleep(.7)
                        ),
                new FollowPath(driveToSpike3Collect, follower, spike3End, true, 1.0),
                new InstantlyDo(()-> {
            transfer.setTransferPower(0.2);
            intake.getIntakeMotor().setPower(0.0);}),
                new Race("race",
                        new FollowPath(driveToAprGate, follower, gateApr, false, 1.0),
                    new Sleep(.8)),
                new Parallel("race",
                new FollowPath(driveToOpenGate, follower, gateHold, true, 1.0),
                new Sleep(2.0)),
                new FollowPath(driveToSecondLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(1.0);
                    intake.getIntakeMotor().setPower(1.0);}),
                new Race("race",
                        new FollowPath(driveToSpike2Start, follower, spike2Control, false, 1.0),
                        new Sleep(1.2)
                ),
                new FollowPath(driveToSpike2Collect, follower, spike2End, true, 1.0),
                new Sleep(.5),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(0.0);
                    intake.getIntakeMotor().setPower(0.2);}),
                new Race("race",
                        new FollowPath(driveToApr2Gate, follower, gateApr, true, 1.0),
                        new Sleep(1.0)),
                new Parallel("race",
                        new FollowPath(driveToOpenGate, follower, gateHold, true, 1.0),
                        new Sleep(1.5)),
                new FollowPath(driveToThirdLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new FollowPath(driveToLeave, follower, gateApr, true, 1.0)


        ).schedule();
    }

    @Override
    public void loop() {

        enduranceBot.update();
        follower.update();
        turret.updateTurretYawCRServo();
        turret.updateTurretWithInterpolation(pedroDrivebase.getDistanceFromGoal());

        /*telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("isBusy(): ", follower.isBusy());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("T: ", follower.getCurrentTValue());*/
        try {
            telemetry.addLine(enduranceBot.toString());

            telemetry.addLine(
                    String.format("Cycle Time: %d", System.currentTimeMillis() - lastCycleTime)
            );
            lastCycleTime = System.currentTimeMillis();
        } catch (Exception e) {
            telemetry.addData("telemetry didn't work", e);
        }

        telemetry.addLine("\nHonesty setting: 90%");
        telemetry.addLine("Humor setting: 75%");

        telemetry.update();
    }

    @Override
    public void stop() {
        turret.setTotalCarryoverRevoltions(turret.getTotalRevoltions());
        super.stop();
    }
}