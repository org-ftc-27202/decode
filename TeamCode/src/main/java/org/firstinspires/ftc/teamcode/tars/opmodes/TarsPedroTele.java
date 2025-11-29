package org.firstinspires.ftc.teamcode.tars.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultLeverTransfer;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultSpindexer;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultTurret;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.PedroDefaultDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.bootscreen.BootScreen;
import org.firstinspires.ftc.teamcode.util.TrajectoryCalculator;
import org.firstinspires.ftc.teamcode.util.bootscreen.TerminalVelocityLogo;

@TeleOp(name = "-TARS +Pedro", group = "Robot")
    public final class TarsPedroTele extends LinearOpMode {
        private final PedroDrivebase pedroDrivebase = PedroDrivebase.getInstance();
        private final Intake intake = Intake.getInstance();
        private final LeverTransfer leverTransfer = LeverTransfer.getInstance();
        private final Spindexer spindexer = Spindexer.getInstance();

        private final Turret turret = Turret.getInstance();

        private final StellarBot tars = new StellarBot(
                pedroDrivebase,
                intake,
                leverTransfer,
                spindexer,
                turret
        );

        @Override
        public void runOpMode() {
            // set up subsystems
            tars.init(hardwareMap);

            // set up default directives
            pedroDrivebase.setDefaultDirective(new PedroDefaultDrivebase(gamepad1));
            intake.setDefaultDirective(new DefaultIntake(gamepad1));
            leverTransfer.setDefaultDirective(new DefaultLeverTransfer(gamepad1));
            spindexer.setDefaultDirective(new DefaultSpindexer(gamepad1));
            turret.setDefaultDirective(new DefaultTurret(gamepad1, gamepad2));

            // print telemetry
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
                if (gamepad1.left_bumper && gamepad1.right_bumper) {
                    tars.cancelAll();
                }

                // run scheduler and subsystems logic
                tars.update();

                //print telemetry
                try {
                    telemetry.addLine(tars.toString());
                } catch (Exception e) {
                    telemetry.addData("telemetry didn't work", e);
                }

                telemetry.addLine();
                telemetry.addLine("Honesty setting: 90%");
                telemetry.addLine("Humor setting: 75%");

                telemetry.update();
            }

            // cancel triggers and runnables
            tars.cancelAll();
        }
}
