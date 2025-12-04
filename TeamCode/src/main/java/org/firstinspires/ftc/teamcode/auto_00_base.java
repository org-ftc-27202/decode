package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class auto_00_base extends LinearOpMode {
    public enum AllianceColors {RED, BLUE};
    private TeleOp_00_base.AllianceColors allianceColor = TeleOp_00_base.AllianceColors.RED;
    public static botVisionFront.PatternOptions patternMatch = botVisionFront.PatternOptions.GPP;

    public void setAllianceColor(TeleOp_00_base.AllianceColors inAllianceColor) {
        allianceColor = inAllianceColor;
    }

    @Override
    public void runOpMode() {
        TelemetryPacket packet = new TelemetryPacket();
        botVisionFront visionFront = new botVisionFront(hardwareMap, allianceColor);

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.update();
        }

        waitForStart();

        this.resetRuntime();

        if (isStopRequested()) return;

        Actions.runBlocking(
            new SequentialAction(
                visionFront.getPattern()
        ));

        patternMatch = visionFront.pattern;

        telemetry.addData("Duration", this.getRuntime());
        if (patternMatch == botVisionFront.PatternOptions.GPP) {
            telemetry.addData("pattern: ", "%s", "GPP");
        }
        else if (patternMatch == botVisionFront.PatternOptions.PGP) {
            telemetry.addData("pattern: ", "%s", "PGP");
        }
        else if (patternMatch == botVisionFront.PatternOptions.PPG) {
            telemetry.addData("pattern: ", "%s", "PPG");
        }
        telemetry.update();
        telemetry.update();
    }
}