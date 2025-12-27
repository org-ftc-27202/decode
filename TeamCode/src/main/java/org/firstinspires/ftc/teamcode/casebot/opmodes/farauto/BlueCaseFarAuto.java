package org.firstinspires.ftc.teamcode.casebot.opmodes.farauto;// make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.GetMotifSequence;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarMotifLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntakeWaitForColor;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.TurretStartup;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "BLUECase Auto Pedro", group = "Auto")
public final class BlueCaseFarAuto extends OpMode {
    private final double FLYWHEEL_LAUNCH = 1080;
    private final double TURRET_LAUNCH = 0;
    private final double HOOD_LAUNCH = 0;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private final StellarBot caseBot = StellarBot.getInstance();
    private final PedroDrivebase pedroDrivebase = new PedroDrivebase();
    private final Intake intake = new Intake();
    private final LeverTransfer leverTransfer = new LeverTransfer();
    private final Spindexer spindexer = new Spindexer();
    private final Turret turret = new Turret();
    private final Camera camera = new Camera();

    private Follower follower;

    private final Pose startPose = new Pose(56.75,7, Math.toRadians(180));
    private final Pose collect1Pose = new Pose(19, 35.5);
    private final Pose collect1Control = new Pose(56,35.5);
    private final Pose launchFarPose = new Pose(60,21);
    private PathChain path1, path2, path3, path4, path5, path6;

    public void buildPaths() {
        path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(55.300, 7.000), new Pose(61.000, 24.000))
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                .build();
        path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(61.000, 24.000), new Pose(43.000, 35.500))
                )
                .setLinearHeadingInterpolation(Math.toRadians(85), Math.toRadians(180))
                .build();
        path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(43.000, 35.500), new Pose(27.000, 35.500))
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(27.000, 35.500), new Pose(11.000, 35.500))
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        path5 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(11.000, 35.500), new Pose(30.000, 30.000))
                )
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();
        path6 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(61.000, 24.000), new Pose(61.000, 44.000))
                )
                .setTangentHeadingInterpolation()
                .build();
    }

    @Override
    public void init() {
        caseBot.setupBot(
            StellarBot.AllianceColor.BLUE,
            pedroDrivebase,
            intake,
            leverTransfer,
            spindexer,
            turret,
            camera
        );

        //this.follower = Constants.createFollower(hardwareMap);
        pedroDrivebase.setOpMode(PedroDrivebase.opModeType.AUTO);
        caseBot.init(hardwareMap);
        caseBot.setPrintDebug(true);
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
                new SetPosition(leverTransfer.getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
                new InstantlyDo(() -> intake.setIntakeSpeed(0.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new Parallel(
                        "hmm",
                        new TurretStartup(), // This runs in the background
                        new Procedure(
                                "lock in",// While this part continues forward
                                new GetMotifSequence(),
                                new FollowPath(path1, follower, new Pose(61.000, 24.000), true)
                        )),
                new FarMotifLaunch(),
                new InstantlyDo(()-> intake.setIntakeSpeed(1.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new FollowPath(path2, follower, new Pose(43.000, 35.500), true),
                new Parallel("pickup1",
                        new FullIntakeWaitForColor(),
                        new Procedure ("pickup",
                                new FollowPath(path3, follower, new Pose(27.000, 35.500), true),
                                new Sleep(0.5),
                                new FollowPath(path4, follower, new Pose(11.000, 35.500), true),
                                new Sleep(0.5)
                        )
                ),
                new FollowPath(path5, follower, new Pose(30.000, 30.000), true)
                //new FarMotifLaunch(),
                //new FollowPath(path6, follower, new Pose(61.000, 44.000), true)

                //new FollowPath(cameraPath, follower, new Pose(56.750, 80.000), true),
                //                new Sleep(2.0),
                //new FullIntake(),
                //new InstantlyDo(()-> Turret.getInstance().getTurretServo().setPosition(TURRET_LAUNCH)),
                //new InstantlyDo(()-> Turret.getInstance().getTurretHoodServo().setPosition(HOOD_LAUNCH)),
                //new FullPattern+Outtake()
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
        telemetry.addLine();
        telemetry.addLine(caseBot.toString());
        telemetry.update();
    }
}