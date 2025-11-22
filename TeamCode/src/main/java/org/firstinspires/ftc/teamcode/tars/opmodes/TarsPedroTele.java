package org.firstinspires.ftc.teamcode.tars.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives.DefaultDrivebase;
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

@TeleOp(name = "TARS +Pedro", group = "Robot")
    public class TarsPedroTele extends LinearOpMode {
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
            try {
                telemetry.addLine(
                        "\n                                 █\n" +
                        "                                 █              █\n" +
                        "                                 █              █              █\n" +
                        "                                                  █              █\n" +
                        "                       ▄███▄                             █\n" +
                        "                    ▐█████▌██████▄\n" +
                        "        ▌            ▀███▀▄███████▄                  ▄▄\n" +
                        "        █                  ▄███████████▌     ▄▄███▀\n" +
                        "        ▐▄    ▐██████▀      ▀▀▀▀████████▀\n" +
                        "              ▀▀▀▀                                          ▀▀"
                );
                //telemetry.addLine(tars.getScheduler().toString());
            } catch (Exception e) {
                telemetry.addData("telemetry didn't work", e);
            }
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
