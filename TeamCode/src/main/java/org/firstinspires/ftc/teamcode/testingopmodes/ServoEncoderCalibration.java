package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Encoder Calibration", group = "Util")
public class ServoEncoderCalibration extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		// get the hardware components directly
		Servo spindexer = hardwareMap.get(Servo.class, "spindexerServo");
		AnalogInput encoder = hardwareMap.get(AnalogInput.class, "spindexerServoEncoder");

		telemetry.addLine("Press DPAD UP/DOWN to change servo position.");
		telemetry.addLine("Watch the telemetry for voltage values.");
		telemetry.update();

		waitForStart();

		double position = 0.5; // Start in the middle
		spindexer.setPosition(position);

		while(opModeIsActive()) {
			// use gamepad to slowly move the servo
			if (gamepad1.dpad_up) {
				position += 0.001;
			}
			if (gamepad1.dpad_down) {
				position -= 0.001;
			}

			// clamp the position between 0.0 and 1.0
			position = Math.max(0.0, Math.min(1.0, position));

			// set the servo position
			spindexer.setPosition(position);

			telemetry.addData("Servo Commanded Position", position);
			telemetry.addData("Encoder Voltage", encoder.getVoltage());
			telemetry.addData("Encoder Calculated Angle", encoder.getVoltage()/3.13);
			telemetry.update();
		}
	}
}