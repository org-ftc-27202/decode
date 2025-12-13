package org.firstinspires.ftc.teamcode.casebot.opmodes.tele;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.DefaultTurret;
import org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives.PedroDefaultDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.util.TrajectoryCalculator;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@TeleOp(name = "-BLUECase +Pedro", group = "Robot")
    public final class BlueCaseTele extends LinearOpMode {
        private final PedroDrivebase pedroDrivebase = PedroDrivebase.getInstance();
        private final Intake intake = Intake.getInstance();
        private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
        private final Spindexer spindexer = Spindexer.getInstance();
        private final Camera camera = Camera.getInstance();

        private final Turret turret = Turret.getInstance();
    // Define these at the class level of your OpMode
        private boolean poseResetToggled = false; // Tracks if the reset command has been run for the current press
        private boolean currentResetState = false; // Tracks the state of the toggle (Optional: for alternate poses)

    private long lastCycleTime = 0;

    private final StellarBot caseBot = new StellarBot(
            pedroDrivebase,
            intake,
            leverTransfer,
            spindexer,
            turret,
            camera
    );

        @Override
        public void runOpMode() {
            PedroDrivebase.getInstance().setAllianceColor(PedroDrivebase.AllianceColor.BLUE);

            // set up subsystems
            caseBot.init(hardwareMap);

            // set up default directives
            pedroDrivebase.setDefaultDirective(new PedroDefaultDrivebase(gamepad1, gamepad2));
            intake.setDefaultDirective(new DefaultIntake(gamepad1, gamepad2));
            leverTransfer.setDefaultDirective(new DefaultLeverTransfer(gamepad1));
            spindexer.setDefaultDirective(new DefaultSpindexer(gamepad1, gamepad2));
            turret.setDefaultDirective(new DefaultTurret(gamepad1, gamepad2));

            // print telemetry
            BootScreen bootScreen = new BootScreen(telemetry, new TerminalVelocityLogo(), true);
            try {
                bootScreen.updateBootScreen();
                sleep(bootScreen.getMillisBetweenFrames());
            } catch (Exception e) {
                telemetry.addData("telemetry didn't work", e);
            }

            TrajectoryCalculator trajectoryCalculator = new TrajectoryCalculator();
            double[] radiansAndVelocity = trajectoryCalculator.calculateTrajectory(0.1, 3.6576, 0.9906, 20.0, 1.0, 2.7);
            telemetry.addLine(String.format("Radians: %f, Velocity: %f", radiansAndVelocity[0], radiansAndVelocity[1]));

            telemetry.update();

            waitForStart();

            if (isStopRequested()) return;

            while (opModeIsActive()) {
                // panic: cancels all runnables
                if (gamepad2.left_bumper && gamepad2.right_bumper) {
                    caseBot.cancelAll();
                }
                // Define inputs and the single reset pose
                boolean triggersDown = (gamepad2.left_trigger > 0.05) && (gamepad2.right_trigger > 0.05);
                Pose resetPose = new Pose(56.75, 7, Math.toRadians(180.0));

// --- RISING EDGE / ONE-SHOT LOGIC ---
// This block runs ONLY on the moment the triggers are pressed (rising edge)
                if (triggersDown && !poseResetToggled) {
                    // 1. Execute the reset
                    PedroDrivebase.getInstance().getFollower().setPose(resetPose);
                    // 2. Set the one-shot flag (prevents repeat execution while held)
                    poseResetToggled = true;
                } else if (!triggersDown) {
                    // 3. Reset the flag when triggers are released, readying for the next press
                    poseResetToggled = false;
                }

                // run scheduler and subsystems logic
                caseBot.update();

                // print telemetry
                try {
                    telemetry.addLine(caseBot.toString());

                    telemetry.addLine(
                            String.format("Cycle Time: %d", System.currentTimeMillis() - lastCycleTime)
                    );
                    lastCycleTime = System.currentTimeMillis();
                } catch (Exception e) {
                    telemetry.addData("telemetry didn't work", e);
                }

                telemetry.addLine();
                telemetry.addLine("Honesty setting: 90%");
                telemetry.addLine("Humor setting: 75%");

                telemetry.update();
            }

            // cancel triggers and runnables
            caseBot.cancelAll();
        }
}
