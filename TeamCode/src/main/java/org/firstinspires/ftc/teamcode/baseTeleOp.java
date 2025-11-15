package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import java.util.ArrayList;
import java.util.List;

public abstract class baseTeleOp extends LinearOpMode {
    private String allianceColor;
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    public void setAllianceColor(String inAllianceColor) {
        allianceColor = inAllianceColor;
    }
    @Override
    public void runOpMode() {
        TelemetryPacket packet = new TelemetryPacket();
        botIntake intake = new botIntake(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // intake
            if (gamepad1.left_trigger > 0.2) {
                runningActions.add(new SequentialAction(
                        intake.RotateInwards()
                ));
            } else if (gamepad1.left_bumper) {
                runningActions.add(new SequentialAction(
                        intake.RotateOutwards()
                ));
            } else {
                runningActions.add(new SequentialAction(
                        intake.Stop()
                ));
            };

            // update running actions
            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }
            runningActions = newActions;

            dash.sendTelemetryPacket(packet);

            telemetry.addData("packet", packet);
            telemetry.addData("runningActions.size()", runningActions.size());
            telemetry.update();
        }
    }
}