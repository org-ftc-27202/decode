package org.firstinspires.ftc.teamcode.endurancebot.opmodes.farauto;// make sure this aligns with class location

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
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "blue FAR Auto ", group = "Auto")
public final class BLUEFARauto extends OpMode {

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

    private final Pose startPose = new Pose(88.75,9.0, Math.toRadians(90)).mirror();
    private final Pose leavePose = new Pose(100.0, 15.0, Math.toRadians(90)).mirror();
    private final Pose cameraPose = new Pose(53.5, 80.0, Math.toRadians(60));
    private final Pose firstLaunchPose = new Pose(90.0, 14.0, Math.toRadians(90)).mirror();
    private final Pose launchControlPose = new Pose(53.5, 80.0, Math.toRadians(135));
    private final Pose launchPose = new Pose(53.5, 80.0, Math.toRadians(135));
    private final Pose spike1Control = new Pose(43,35.5, Math.toRadians(180));
    private final Pose spike1Start = new Pose(33,35.5, Math.toRadians(180));
    private final Pose spike1End = new Pose(12 ,35.5, Math.toRadians(180));

    private final Pose spike2Control = new Pose(43,59, Math.toRadians(180));
    private final Pose spike2Start = new Pose(34,59, Math.toRadians(180));
    private final Pose spike2End = new Pose(17,59, Math.toRadians(180));
    private final Pose spike3Control = new Pose(43,82.5, Math.toRadians(180));
    private final Pose spike3Start = new Pose(34,82.5, Math.toRadians(180));
    private final Pose spike3End = new Pose(17,82.5, Math.toRadians(180));
    private final Pose gateApr = new Pose(26,76, Math.toRadians(180));
    private final Pose gateHold = new Pose(15, 76, Math.toRadians(180));
    private final Pose loadingZone= new Pose(8.0, 8.0, Math.toRadians(180));


    //  private final Pose collect1Pose = new Pose(19, 35.5);

    //  private final Pose collect1Control = new Pose(56,35.5);
    //  private final Pose launchFarPose = new Pose(60,21);
    private PathChain driveToLoadingZoneCollect, driveToSpike1Start, driveToSpike1Collect, driveToSpike2Start, driveToThirdLaunch, driveToSpike2Collect, driveToApr2Gate, driveToFirstLaunch, driveToLeave, driveToSpike3Start, driveToSpike3Collect, driveToAprGate, driveToOpenGate, driveToSecondLaunch;

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
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToOpenGate = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateApr, gateHold)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToSecondLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3End, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(gateHold.getHeading(), Math.toRadians(90))
                .build();
        driveToSpike1Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose,spike1Control )
                )
                .setLinearHeadingInterpolation(firstLaunchPose.getHeading(), spike1Control.getHeading())
                .build();
        driveToSpike1Collect = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike1Control, spike1End)
                )
                .setLinearHeadingInterpolation(spike2Control.getHeading(), spike2End.getHeading())
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
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToLoadingZoneCollect = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose, loadingZone)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToThirdLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(loadingZone, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(loadingZone.getHeading(), Math.toRadians(180))
                .build();

        driveToLeave = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(firstLaunchPose, leavePose)
                )
                .setLinearHeadingInterpolation(firstLaunchPose.getHeading(), leavePose.getHeading())
                .build();


    }

    @Override
    public void init() {
        enduranceBot.setupBot(
                StellarBot.AllianceColor.BLUE,
                pedroDrivebase,
                intake,
                transfer,
                //spindexer,
                turret,
                camera
        );

        //this.follower = Constants.createFollower(hardwareMap);
        pedroDrivebase.setOpMode(PedroDrivebase.opModeType.AUTO);
        pedroDrivebase.setAutoSide(PedroDrivebase.AutoSide.FAR);
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
                        new FollowPath(driveToSpike1Start, follower, spike1Control, false, 1.0),
                        new Sleep(.7)
                ),
                new FollowPath(driveToSpike1Collect, follower, spike1End, true, 1.0),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(0.2);
                    intake.getIntakeMotor().setPower(0.0);}),
                new FollowPath(driveToSecondLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(1.0);
                    intake.getIntakeMotor().setPower(1.0);}),
                new Parallel("parar",
                        new Race("a",
                            new Sleep(3.0),
                            new Procedure("a",
                                new WaitUntil(()-> transfer.getTurretFull()),
                                new InstantlyDo(()-> {
                            transfer.setTransferPower(0.2);
                            intake.getIntakeMotor().setPower(-1.0);}))
                        ),
                    new Race("race",
                        new FollowPath(driveToLoadingZoneCollect, follower, loadingZone, true, 1.0),
                        new Sleep(3.0))
                ),
                new FollowPath(driveToThirdLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new InstantlyDo(()-> {
                    transfer.setTransferPower(1.0);
                    intake.getIntakeMotor().setPower(1.0);}),
                new Parallel("parar",
                        new Race("a",
                                new Sleep(3.0),
                                new Procedure("a",
                                        new WaitUntil(()-> transfer. getTurretFull()),
                                        new InstantlyDo(()-> {
                                            transfer.setTransferPower(0.2);
                                            intake.getIntakeMotor().setPower(-1.0);}))
                        ),
                        new Race("race",
                                new FollowPath(driveToLoadingZoneCollect, follower, loadingZone, true, 1.0),
                                new Sleep(3.0))
                ),
                new FollowPath(driveToThirdLaunch, follower, firstLaunchPose, true, 1.0),
                new FullOuttake(),
                new FollowPath(driveToLeave, follower, leavePose, true, 1.0)
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