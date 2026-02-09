package org.firstinspires.ftc.teamcode.casebot.opmodes.farauto;// make sure this aligns with class location

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.FollowPath;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.GetMotif;
import org.firstinspires.ftc.teamcode.casebot.runnables.directives.TurnTo;
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

@Autonomous(name = "BLUE sHORT Auto ", group = "Auto")
public final class BlueCaseShortAuto extends OpMode {

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
    private boolean turretStarted = false;

    private long lastCycleTime = 0;

    private final Pose startPose = new Pose(36.0,135.0, Math.toRadians(0));
    private final Pose cameraPose = new Pose(53.5, 82.0, Math.toRadians(75));
    private final Pose launchControlPose = new Pose(53.5, 82.0, Math.toRadians(135));
    private final Pose launchPose = new Pose(53.5, 82.0, Math.toRadians(135));
    private final Pose spike1Control = new Pose(52,35.5, Math.toRadians(180));
    private final Pose spike1Start = new Pose(38,35.5, Math.toRadians(180));
    private final Pose spike1End = new Pose(12 ,35.5, Math.toRadians(180));

    private final Pose spike2Control = new Pose(50,58, Math.toRadians(180));
    private final Pose spike2Start = new Pose(42,58, Math.toRadians(180));
    private final Pose spike2End = new Pose(10,58, Math.toRadians(180));
    private final Pose spike3Control = new Pose(46,82.5, Math.toRadians(180));
    private final Pose spike3Start = new Pose(34,82.5, Math.toRadians(180));
    private final Pose spike3End = new Pose(16,82.5, Math.toRadians(180));
    private final Pose gateApr = new Pose(20,76, Math.toRadians(180));
    private final Pose gateHold = new Pose(15, 76, Math.toRadians(180));
    private final Pose leavePose = new Pose(51, 79, Math.toRadians(135));

    //  private final Pose collect1Pose = new Pose(19, 35.5);

    //  private final Pose collect1Control = new Pose(56,35.5);
    //  private final Pose launchFarPose = new Pose(60,21);
    private PathChain driveToGetMotif, leave, driveToSpike1Control, driveToSpike1Start, driveToSpike1End, driveToLaunch1, driveToSpike2Control, driveToSpike2Start, driveToSpike2End, driveToLaunch2, driveToLeverApr, driveToLeverHold, driveToLaunch3, driveToSpike3Control, driveToSpike3Start, driveToSpike3End, driveToLaunchGate;

    public void buildPaths() {
        leave = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(launchPose, leavePose)
                )
                .setConstantHeadingInterpolation(leavePose.getHeading())
                .build();
        driveToGetMotif = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, cameraPose)
                )
                .setTangentHeadingInterpolation()
                .build();

        driveToSpike1Control = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(launchPose, spike1Control, spike1Start)
                )
                .setTangentHeadingInterpolation().build();
        driveToSpike1Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike1Control, spike1End)
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
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();
        driveToSpike2Control = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(launchControlPose, spike2Control, spike2Start)
                )
                .setLinearHeadingInterpolation(launchControlPose.getHeading(), spike1Start.getHeading())
                .build();
        driveToSpike2Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike2Start, spike2End)
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
        driveToLaunch2 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(spike2End, spike2Control, launchPose)
                )
                .setLinearHeadingInterpolation(spike2End.getHeading(), launchPose.getHeading())
                .build();

        driveToSpike3Control = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(launchControlPose, spike3Control)
                )
                .setLinearHeadingInterpolation(launchControlPose.getHeading(), spike2Control.getHeading())
                .build();
        driveToSpike3Start = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3Control, spike3End)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        driveToSpike3End = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3Start, spike3End)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        driveToLeverApr = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(spike3End, gateApr)
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(gateApr, gateHold)
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();
        driveToLeverHold = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateApr, gateHold)
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();
        driveToLaunchGate = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(gateHold, launchPose)
                )
                .setLinearHeadingInterpolation(gateHold.getHeading(), launchPose.getHeading())
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
        pedroDrivebase.setAutoSide(PedroDrivebase.AutoSide.SHORT);
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
                new InstantlyDo(()-> turretStarted = false),
                new SetPos(leverTransfer.getLeverTransferServo(), LeverTransfer.LEVER_DOWN_POSITION),
                new InstantlyDo(() -> intake.setIntakeSpeed(1.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new Parallel(
                        "hmm",
                        new Procedure(
                                "turret",
                                new TurretStartup(),
                                new InstantlyDo(()-> turretStarted = true)
                        ),
                        new Sleep(PRE_MATCH_DELAY),// This runs in the background
                        new Procedure(
                                "lock in",
                                // While this part continues forward
                                new Parallel( "hi",
                                        new Race("get Motif",
                                                new GetMotif(),
                                                new Sleep(3.0)),
                                new Procedure("huhhf",
                                        new FollowPath(driveToGetMotif, follower, cameraPose, true, 1.0),
                                        new TurnTo(cameraPose.getHeading(), follower),
                                        new Sleep(0.8)
                                )
                                ),
                                new InstantlyDo(()-> HasMotifPattern = true),
                                new TurnTo(Math.toRadians(135), follower),
                                new Sleep(0.2)

                        )),
                new FarMotifLaunch(),
                new InstantlyDo(()-> intake.setIntakeSpeed(1.0)),
                new InstantlyDo(intake::setMotorSpeed),
                new FollowPath(driveToSpike3Control, follower, spike3Control, false, 1.0),
                new Sleep(1.0),
                new Race(
                        "pickup3Race",
                        new Parallel("pickup3",
                                new FullIntake(),
                                new Procedure ("spike3pickup",
                                        new FollowPath(driveToSpike3Start, follower, spike3End, true, 0.4)

                                )
                        ),
                        new Sleep(2.5)
                ),
                new InstantlyDo(()-> {
                    spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.GREEN);
                }),

                new Race(
                        "hasdf",
                new FollowPath(driveToLeverApr, follower, gateHold, true, 1.0),
                        new Sleep(2.0)
                        ),
                new FollowPath(driveToLaunchGate, follower,launchPose, true, 1.0),
                new FarMotifLaunch(),
                new FollowPath(driveToSpike2Control, follower, spike2Start, true, 1.0),
                new Race(
                        "pickup2Race",
                        new Parallel("pickup2",
                                new FullIntake(),
                                new Procedure ("spike2pickup",
                                        new FollowPath(driveToSpike2Start, follower, spike2End, true, 0.32)

                                )
                        ),
                        new Sleep(2.5)
                ),
                new InstantlyDo(()-> {
                    spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.GREEN);
                    spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.PURPLE);
                }),
                new FollowPath(driveToLaunch2, follower, launchPose, true,1.0),
                new FarMotifLaunch(),
                new FollowPath(leave, follower, leavePose, false, 1.0)
                /*new FollowPath(driveToSpike1Control, follower, spike1Start, true, 1.0),
                new Race(
                        "picksu1Race",
                        new Parallel("pickup1",
                                new FullIntake(),
                                new Procedure ("spike1pickup",
                                        new FollowPath(driveToSpike1Start, follower, spike1End, true, 0.4)
                                )
                        ),
                        new Sleep(4.5)
                ),
                new InstantlyDo(()-> {
                    spindexer.setArtifactColorInSpindexer(0, DecodeDataTypes.ArtifactColor.GREEN);
                    spindexer.setArtifactColorInSpindexer(1, DecodeDataTypes.ArtifactColor.PURPLE);
                    spindexer.setArtifactColorInSpindexer(2, DecodeDataTypes.ArtifactColor.PURPLE);
                }),
                new FollowPath(driveToLaunch1, follower, launchPose, true, 1.0),
                new TurnTo(launchPose.getHeading(), follower),
                new Sleep(0.5),
                new FarMotifLaunch()*/

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
        if (turretStarted) {
            turret.updateTurretWithInterpolation(pedroDrivebase.getDistanceFromGoalFromPose(launchPose));
        }

        /*telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("isBusy(): ", follower.isBusy());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("T: ", follower.getCurrentTValue());*/
        try {
            telemetry.addLine(caseBot.toString());
            telemetry.addLine(
                    String.format("%s, %s, %s",
                            spindexer.getArtifactColorAt(0),
                            spindexer.getArtifactColorAt(1),
                            spindexer.getArtifactColorAt(2)
                    )
            );

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