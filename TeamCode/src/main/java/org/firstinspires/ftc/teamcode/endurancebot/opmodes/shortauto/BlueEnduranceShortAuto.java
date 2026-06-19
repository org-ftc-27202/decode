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
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "BLUE SHORT Auto ", group = "Auto")
public final class BlueEnduranceShortAuto extends OpMode {

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

    private final Pose startPose = new Pose(36.0,135.0, Math.toRadians(0));
    private final Pose cameraPose = new Pose(53.5, 80.0, Math.toRadians(60));
    private final Pose firstLaunchPose = new Pose(60.0, 120, Math.toRadians(0));
    private final Pose launchControlPose = new Pose(53.5, 80.0, Math.toRadians(135));
    private final Pose launchPose = new Pose(53.5, 80.0, Math.toRadians(135));
    private final Pose spike1Control = new Pose(43,35.5, Math.toRadians(180));
    private final Pose spike1Start = new Pose(33,35.5, Math.toRadians(180));
    private final Pose spike1End = new Pose(12 ,35.5, Math.toRadians(180));

    private final Pose spike2Control = new Pose(43,59, Math.toRadians(180));
    private final Pose spike2Start = new Pose(34,59, Math.toRadians(180));
    private final Pose spike2End = new Pose(12,59, Math.toRadians(180));
    private final Pose spike3Control = new Pose(43,82.5, Math.toRadians(180));
    private final Pose spike3Start = new Pose(34,82.5, Math.toRadians(180));
    private final Pose spike3End = new Pose(12,82.5, Math.toRadians(180));
    private final Pose gateApr = new Pose(20,76, Math.toRadians(180));
    private final Pose gateHold = new Pose(15, 76, Math.toRadians(180));

    //  private final Pose collect1Pose = new Pose(19, 35.5);

    //  private final Pose collect1Control = new Pose(56,35.5);
    //  private final Pose launchFarPose = new Pose(60,21);
    private PathChain driveToFirstLaunch, driveToLeave, driveToSpike1Control, driveToSpike1Start, driveToSpike1End, driveToLaunch1, driveToSpike2Control, driveToSpike2Start, driveToSpike2End, driveToLaunch2, driveToLeverApr, driveToLeverHold, driveToLaunch3, driveToSpike3Control, driveToSpike3Start, driveToSpike3End, driveToLaunchGate;

    public void buildPaths() {
        driveToFirstLaunch = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, firstLaunchPose)
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), firstLaunchPose.getHeading())
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
        pedroDrivebase.setAutoSide(PedroDrivebase.AutoSide.SHORT);
        enduranceBot.init(hardwareMap);
        enduranceBot.setPrintDebug(false);
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

}