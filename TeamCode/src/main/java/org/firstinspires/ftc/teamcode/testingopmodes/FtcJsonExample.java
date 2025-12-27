package org.firstinspires.ftc.teamcode.testingopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.DecodeDataTypes.ArtifactSequence;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes.Coords;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes.DateMs;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes.MotorPositions;
import org.firstinspires.ftc.teamcode.util.FtcJsonStorage;

@TeleOp(name = "FTC JSON Example", group = "Robot")

public class FtcJsonExample extends LinearOpMode {
	@Override
	public void runOpMode() {
		telemetry.addLine("Waiting for onStart");
		telemetry.update();

		// Wait for the user to press onStart on the Driver Station
		waitForStart();

		FtcJsonStorage storage = new FtcJsonStorage(hardwareMap.appContext);

		String[] fileNames = storage.getInternalFileNames();
		String jsonContent = storage.readFromJsonInternalStorage("autoData.json");
/*
        storage.writeToInternalStorage(
                "autoData.json",
                ArtifactSequence.of(ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE),
                new Coords(1.1, 2.2, 3.3, 4.4, 5.5, 6.6),
                new MotorPositions.Builder()
                        .addMotor("hoodedShooter", 1.0)
                        .addMotor("carouselPaddles", 2.0)
                        .addMotor("botFlipper", 3.0)
                        .build(),
                new DateMs(System.currentTimeMillis())
        );
        */


		ArtifactSequence artifactSequence = new ArtifactSequence();
		Coords coords = new Coords();
		MotorPositions motorPositions = new MotorPositions.Builder().build();
		DateMs dateMs = new DateMs();

		boolean read = storage.readAndParseAutoData("autoData.json", artifactSequence, coords, motorPositions, dateMs);

		while (opModeIsActive()) {
			try {
				for (int i = 0; i < fileNames.length; i++) {
					telemetry.addData("File", fileNames[i]);
				}
			} catch (Exception err) {
				telemetry.addData("Exception", err);
			}
			telemetry.addData("JSON", jsonContent);
			telemetry.addData("read", read);
			telemetry.addData("Artifact sequence", artifactSequence.toStringArray());
			telemetry.addData("Coords", coords.getX() + ", " + coords.getY() + ", " + coords.getZ() + ", " + coords.getPitch() + ", " + coords.getRoll() + ", " + coords.getYaw());
			telemetry.addData("Motor positions", motorPositions.getMotorPosition("hoodedShooter") + ", " + motorPositions.getMotorPosition("carouselPaddles") + ", " + motorPositions.getMotorPosition("botFlipper"));
			telemetry.addData("Minutes since", dateMs.getMinutesTimeSince());


			telemetry.update();

			sleep(100);
		}
	}
}
