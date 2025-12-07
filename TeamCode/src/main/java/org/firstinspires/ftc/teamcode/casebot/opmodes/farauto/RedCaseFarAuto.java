package org.firstinspires.ftc.teamcode.casebot.opmodes.farauto;// make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.GetMotifSequence;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.ShortColorLaunch;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "-Case Auto Pedro", group = "Auto")
public final class RedCaseFarAuto extends OpMode {
    private final double FLYWHEEL_LAUNCH = 1080;
    private final double TURRET_LAUNCH = 0;
    private final double HOOD_LAUNCH = 0;
    private Timer pathTimer, actionTimer, opmodeTimer;

    //DefaultDrivebase defaultDrivebase = new DefaultDrivebase(gamepad1);
    private DefaultIntake defaultIntake = new DefaultIntake(gamepad1, gamepad2);
    private DefaultLeverTransfer defaultLeverTransfer = new DefaultLeverTransfer(gamepad1);
    private DefaultSpindexer defaultSpindexer = new DefaultSpindexer(gamepad1, gamepad2);

    private final PedroDrivebase pedroDrivebase = PedroDrivebase.getInstance();
    private final Intake intake = Intake.getInstance();
    private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
    private final Spindexer spindexer = Spindexer.getInstance();
    private final Turret turret = Turret.getInstance();

    private final StellarBot caseBot = new StellarBot(
            PedroDrivebase.getInstance(),
            Camera.getInstance(),
            Intake.getInstance(),
            LeverTransfer.getInstance(),
            Spindexer.getInstance(),
            Turret.getInstance()
    );

    private Follower follower;

    private final Pose startPose = new Pose(56.75,7, Math.toRadians(180)).mirror();
    private final Pose collect1Pose = new Pose(19, 35.5).mirror();
    private final Pose collect1Control = new Pose(56,35.5).mirror();
    private final Pose launchFarPose = new Pose(60,21).mirror();
    private PathChain path1, path2, path3, path4, path5, cameraPath;

    public void buildPaths() {
        this.cameraPath= follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(56.750, 7.000).mirror(), new Pose(56.750, 80.000).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(82.5))
                .build();
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
        //this.follower = Constants.createFollower(hardwareMap);
        PedroDrivebase.getInstance().setAllianceColor(PedroDrivebase.AllianceColor.RED);
        caseBot.init(hardwareMap);
        follower = pedroDrivebase.getFollower();
        buildPaths();
        spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.PURPLE);
        spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.GREEN);
        spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.PURPLE);

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
                //new TurretStartup(),
                new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
                new FollowPath(cameraPath, follower, new Pose(56.750, 80.000), true),
                new GetMotifSequence(),
                new Sleep(2.0),
                new ShortColorLaunch()

                //new FullIntake(),
                //new InstantlyDo(()-> Turret.getInstance().getTurretServo().setPosition(TURRET_LAUNCH)),
                //new InstantlyDo(()-> Turret.getInstance().getTurretHoodServo().setPosition(HOOD_LAUNCH)),
                //new FullPatternOuttake()
                //new FollowPath(path1, follower, collect1Pose, true)
                        //new FullIntake(),
                        //new SetPower(Intake.getInstance().getIntakeMotor(), 0.5),
                //new FollowPath(path2, follower, launchFarPose, true),
                //new FullOuttake()
        ).schedule();
    }

    @Override
    public void loop() {
        caseBot.update();
        follower.update();
        /*telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("isBusy(): ", follower.isBusy());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("T: ", follower.getCurrentTValue());*/
        telemetry.addLine(caseBot.toString());
        telemetry.update();
    }
}