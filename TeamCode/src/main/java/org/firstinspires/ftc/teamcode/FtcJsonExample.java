package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.ArtifactColor;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.ArtifactSequence;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.Coords;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.DateMs;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.MotorPositions;

import org.firstinspires.ftc.teamcode.utils.FtcJsonStorage;

import java.lang.reflect.Array;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTC JSON Example", group = "Robot")

public class FtcJsonExample extends LinearOpMode {
	@Override
	public void runOpMode() {
		telemetry.addLine("Waiting for start");
		telemetry.update();

		// Wait for the user to press start on the Driver Station
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
			for (int i = 0; i < fileNames.length; i++) {
				telemetry.addData("File", fileNames[i]);
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
