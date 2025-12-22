package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp(name = "Jet Engine Test", group = "Robot")
public class JetEngineTest extends OpMode {
    public DcMotor motor1;
    public DcMotor motor2;
    public DcMotor motor3;
    double motor1Coefficent = 0;
    double motor2Coefficent = 0;
    double motor3Coefficent = 0;

    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
    }

    public void loop() {
        double power = gamepad1.left_stick_y;
        motor1.setPower(motor1Coefficent * power);
        motor2.setPower(motor2Coefficent * power);
        motor3.setPower(motor3Coefficent * power);

        if (gamepad1.a) {
            motor1Coefficent += 0.0005;
        } else if (gamepad1.b) {
            motor2Coefficent += 0.0005;
        } else if (gamepad1.x) {
            motor3Coefficent += 0.0005;
        } else if (gamepad1.y) {
            motor1Coefficent = 0;
            motor2Coefficent = 0;
            motor3Coefficent = 0;
        }

        motor1Coefficent = Math.min(1.0, motor1Coefficent);
        motor2Coefficent = Math.min(1.0, motor2Coefficent);
        motor3Coefficent = Math.min(1.0, motor3Coefficent);

        telemetry.addLine("a = 1, b = 2, x = 3, y = reset");
        telemetry.addData("motor1Coefficent", motor1Coefficent);
        telemetry.addData("motor2Coefficent", motor2Coefficent);
        telemetry.addData("motor3Coefficent", motor3Coefficent);
        telemetry.addData("Jet Engine Power", power);
        telemetry.update();
    }
}
