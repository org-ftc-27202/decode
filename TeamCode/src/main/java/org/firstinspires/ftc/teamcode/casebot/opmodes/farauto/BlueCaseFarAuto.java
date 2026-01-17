package org.firstinspires.ftc.teamcode.casebot.opmodes.farauto;// make sure this aligns with class location

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.GetMotif;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarMotifLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntakeColor;
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
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Race;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPos;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@Autonomous(name = "BLUECase Auto Pedro", group = "Auto")
public final class BlueCaseFarAuto extends OpMode {

    private final double PRE_MATCH_DELAY = 0.0;

    private final StellarBot caseBot = StellarBot.getInstance();
    private final PedroDrivebase pedroDrivebase = new PedroDrivebase();
    private final Intake intake = new Intake();
    private final LeverTransfer leverTransfer = new LeverTransfer();
    private final Spindexer spindexer = new Spindexer();
    private final Turret turret = new Turret();
    private final Camera camera = new Camera();

    private Follower follower;
    private boolean HasMotifPattern = false;

    private final Pose startPose = new Pose(55.3,7, Math.toRadians(90));
    private final Pose cameraPose = new Pose(61, 24, Math.toRadians(90));
    private final Pose launchControlPose = new Pose(61, 24, Math.toRadians(90));
    private final Pose launchPose = new Pose(61, 24, Math.toRadians(85));

    private final Pose spike1Control = new Pose(43,35.5, Math.toRadians(180));
    private final Pose spike1Start = new Pose(33,35.5, Math.toRadians(180));
    private final Pose spike1End = new Pose(17,35.5, Math.toRadians(180));

    private final Pose spike2Control = new Pose(43,59, Math.toRadians(180));
    private final Pose spike2Start = new Pose(34,59, Math.toRadians(180));
    private final Pose spike2End = new Pose(17,59, Math.toRadians(180));
    private final Pose gate = new Pose(28,72, Math.toRadians(180));
    
  //  private final Pose collect1Pose = new Pose(19, 35.5);

  //  private final Pose collect1Control = new Pose(56,35.5);
  //  private final Pose launchFarPose = new Pose(60,21);
    private PathChain driveToGetMotif, driveToSpike1Control, driveToSpike1Start, driveToSpike1End, driveToLaunch1, driveToSpike2Control, driveToSpike2Start, driveToSpike2End, driveToLaunch2pt1, driveToLaunch2pt2, driveToLever;

    public void buildPaths() {
        driveToGetMotif = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, cameraPose)
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), cameraPose.getHeading())
                .build();
        driveToSpike1Control = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(launchPose, spike1Control)
                )
                .setLinearHeadingInterpolation(launchPose.getHeading(), spike1Control.getHeading())
                .build();
        driveToSpike1Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike1Control, spike1Start)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        driveToSpike1End = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike1Start, spike1End)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        driveToLaunch1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike1End, launchControlPose)
                )
                .setLinearHeadingInterpolation(spike1End.getHeading(), launchControlPose.getHeading())
                .build();
        driveToSpike2Control = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(launchControlPose, spike2Control)
                )
                .setLinearHeadingInterpolation(launchControlPose.getHeading(), spike2Control.getHeading())
                .build();
        driveToSpike2Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2Control, spike2Start)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToSpike2End = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2Start, spike2End)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToLaunch2pt1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2End, spike2Start)
                )
                .setLinearHeadingInterpolation(spike2End.getHeading(), Math.toRadians(135))
                .build();
        driveToLaunch2pt2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2Start, launchControlPose)
                )
                .setLinearHeadingInterpolation(Math.toRadians(135), launchControlPose.getHeading())
                .build();
        driveToLever = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(launchControlPose, gate)
                )
                .setLinearHeadingInterpolation(Math.toRadians(95), Math.toRadians(180))
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
        caseBot.setPrintDebug(false);
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
                new InstantlyDo(()-> HasMotifPattern = false),
                new SetPos(leverTransfer.getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
                new InstantlyDo(() -> intake.setIntakeSpeed(1.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new Parallel(
                        "hmm",
                        new TurretStartup(),
                        new Sleep(PRE_MATCH_DELAY),// This runs in the background
                        new Procedure(
                                "lock in", // While this part continues forward
                                new Race("get Motif",
                                        new GetMotif(),
                                        new Sleep(3.0)),
                                new InstantlyDo(()-> HasMotifPattern = true),
                                new FollowPath(driveToGetMotif, follower, cameraPose, true, 1.0)
                        )),
                new FarMotifLaunch(),
                new InstantlyDo(()-> intake.setIntakeSpeed(1.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new FollowPath(driveToSpike1Control, follower, spike1Control, true, 1.0),
                new Race(
                        "pickup1Race",
                        new Parallel("pickup1",
                                new FullIntake(),
                                new Procedure ("spike1pickup",
                                        new FollowPath(driveToSpike1Start, follower, spike1Start, true, 0.4),
                                        new Sleep(0.3),
                                        new FollowPath(driveToSpike1End, follower, spike1End, true, 0.4)
                                )
                        ),
                        new Sleep(4.5)
                ),
                new InstantlyDo(()-> {
                    spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.GREEN);
                    spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.PURPLE);
                }),

                new FollowPath(driveToLaunch1, follower, launchControlPose, true, 1.0),
                new FarMotifLaunch(),
                new FollowPath(driveToSpike2Control, follower, spike2Control, true, 1.0),
                new Race(
                    "pickup2Race",
                    new Parallel("pickup2",
                        new FullIntake(),
                        new Procedure ("spike2pickup",
                            new FollowPath(driveToSpike2Start, follower, spike2Start, true, 0.4),
                            new Sleep(0.3),
                            new FollowPath(driveToSpike2End, follower, spike2End, true, 0.4)
                        )
                    ),
                    new Sleep(4.5)
                ),
                new InstantlyDo(()-> {
                    spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.GREEN);
                    spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.PURPLE);
                }),
                new FollowPath(driveToLaunch2pt1, follower, spike2Start, true,1.0),
                new FollowPath(driveToLaunch2pt2, follower, launchControlPose, true, 1.0),
                new FarMotifLaunch(),
                new FollowPath(driveToLever, follower, gate, true, 1.0)
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
        if (HasMotifPattern) {
            turret.updateTurretYawServo();
        } else {
            turret.setTurretToForward();
        }
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