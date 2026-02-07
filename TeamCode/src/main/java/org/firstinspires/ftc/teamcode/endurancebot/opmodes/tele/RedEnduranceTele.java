package org.firstinspires.ftc.teamcode.endurancebot.opmodes.tele;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives.DefaultTurret;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives.PedroDefaultDrivebase;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.TurretStartup;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@TeleOp(name = "-Red Endurance +Pedro", group = "Robot")
public final class RedEnduranceTele extends LinearOpMode {
    private final PedroDrivebase pedroDrivebase = new PedroDrivebase();
    private final Intake intake = new Intake();
    private final Transfer transfer = new Transfer();
    private final Camera camera = new Camera();
    private final Turret turret = new Turret();
    private boolean onStart;

    // define these at the class level of your OpMode
    private boolean poseResetToggled = false; // tracks if the reset command has been run for the current press

    private long lastCycleTime = 0;
    private final StellarBot enduranceBot = StellarBot.getInstance();

    @Override
    public void runOpMode() {
        enduranceBot.setupBot(
                StellarBot.AllianceColor.RED,
                pedroDrivebase,
                intake,
                transfer,
                turret,
                camera
        );

        StellarBot.getInstance().getSubsystem(PedroDrivebase.class);

        enduranceBot.setPrintDebug(false);

        pedroDrivebase.setOpMode(PedroDrivebase.opModeType.TELEOP);

        // set up subsystems
        enduranceBot.init(hardwareMap);
        pedroDrivebase.getFollower().setMaxPower(1.0);

        onStart = true;

        // set up default directives
        pedroDrivebase.setDefaultDirective(new PedroDefaultDrivebase(gamepad1, gamepad2));
        intake.setDefaultDirective(new DefaultIntake(gamepad1, gamepad2));
        transfer.setDefaultDirective(new DefaultLeverTransfer(gamepad1));
        turret.setDefaultDirective(new DefaultTurret(gamepad1, gamepad2));

        // print telemetry
        BootScreen bootScreen = new BootScreen(telemetry, new TerminalVelocityLogo(), true);
        try {
            bootScreen.updateBootScreen();
            sleep(bootScreen.getMillisBetweenFrames());
        } catch (Exception e) {
            telemetry.addData("telemetry didn't work", e);
        }

        //double[] radiansAndVelocity = TrajectoryCalculator.calculateTrajectory(0.1, 3.6576, 1.0, 2.7);
        //telemetry.addLine(String.format("Radians: %f, Velocity: %f", radiansAndVelocity[0], radiansAndVelocity[1]));

        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // panic: reset bot
            if (onStart){
                new TurretStartup().schedule();
                onStart = false;
            }
            if (gamepad2.left_bumper && gamepad2.right_bumper) {
                enduranceBot.deactivateBot();
            }
            // Define inputs and the single reset pose
            boolean triggersDown = (gamepad2.left_trigger > 0.05) && (gamepad2.right_trigger > 0.05);
            Pose resetPose = new Pose(87.25, 7, Math.toRadians(90.0));

// --- RISING EDGE / ONE-SHOT LOGIC ---
// This block runs ONLY on the moment the triggers are pressed (rising edge)
            if (triggersDown && !poseResetToggled) {
                // 1. Execute the reset
                pedroDrivebase.getFollower().setPose(resetPose);
                // 2. Set the one-shot flag (prevents repeat execution while held)
                poseResetToggled = true;
            } else if (!triggersDown) {
                // 3. Reset the flag when triggers are released, readying for the next press
                poseResetToggled = false;
            }

            // run scheduler and subsystems logic
            enduranceBot.update();
            turret.updateTurretYawServo();

            // print telemetry
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

        // cancel triggers and runnables
        enduranceBot.deactivateBot();
    }
}
