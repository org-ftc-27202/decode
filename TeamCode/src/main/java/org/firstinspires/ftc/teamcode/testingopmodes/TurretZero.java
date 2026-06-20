package org.firstinspires.ftc.teamcode.testingopmodes;

import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret.PULLEY_RATIO;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret.turretZeroPos;
import static org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret.voltageToDegrees;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

@TeleOp(name = "TurretZero", group = "Robot")
public class TurretZero extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        double externalEncoderRawDegrees;

        AnalogInput externalEncoder = hardwareMap.get(AnalogInput.class, "externalEncoder");
        double turretPosition = 0;
        boolean hasSetPos = false;

        while (!isStopRequested()) {
            if (hasSetPos) {
                telemetry.addLine(String.format("Changed turret position by %f to %f !", turretPosition, turretZeroPos));
                return;
            }

            externalEncoderRawDegrees = voltageToDegrees(externalEncoder.getVoltage());

            turretPosition = (externalEncoderRawDegrees * PULLEY_RATIO) - turretZeroPos;
            telemetry.addData("Turret Position Difference", turretPosition);
            telemetry.update();

            if (gamepad1.a) {
                hasSetPos = true;
                turretZeroPos += turretPosition;
            }
        }
    }
}