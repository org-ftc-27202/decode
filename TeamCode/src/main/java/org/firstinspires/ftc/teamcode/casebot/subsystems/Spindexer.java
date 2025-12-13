package org.firstinspires.ftc.teamcode.casebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;
import org.firstinspires.ftc.teamcode.util.GameState;

import java.util.function.Supplier;


public final class Spindexer extends Subsystem {
	private static final Spindexer spindexer = new Spindexer();
	public static Spindexer getInstance() {
		return spindexer;
	}
	private Spindexer() {}
	private final static double[] INTAKE_POSITIONS = {0.014, 0.382, 0.747};
	private final static double[] TRANSFER_POSITIONS = {0.567, 0.938, 0.193};

	private DecodeDataTypes.ArtifactColor[] artifactColorsInSpindexer = new DecodeDataTypes.ArtifactColor[]{
		DecodeDataTypes.ArtifactColor.NONE,
		DecodeDataTypes.ArtifactColor.NONE,
		DecodeDataTypes.ArtifactColor.NONE
	};

	public enum Position {
		INTAKE, TRANSFER
	}

	/*

	+-----------+--------+----------+
	|           | Intake | Transfer |
	| Segment 0 |   0    |   180    |
	| Segment 1 |  120   |   300    |
	| Segment 2 |  240   |   60     |
	+-----------+--------+----------+

	Intake Procedure (60 ->) 0 -> 120 -> 240
	Transfer Procedure 240 -> 300 -> 180 -> 60

	 */

	private StellarServo spindexerServo;
	private DigitalChannel beamBreak1, beamBreak2;
	private NormalizedColorSensor colorSensor;
	private AnalogInput spindexerServoEncoder;

	@Override
	public void init(HardwareMap hardwareMap) {
		spindexerServo = new StellarServo(hardwareMap, "spindexerServo");
		spindexerServoEncoder = hardwareMap.get(AnalogInput.class, "spindexerServoEncoder");

		//todo
		spindexerServo.setPosition(getServoPositionFromSegment(0, Position.INTAKE));

		beamBreak1 = hardwareMap.get(DigitalChannel.class, "beamBreak1");
		beamBreak1.setMode(DigitalChannel.Mode.INPUT);

		beamBreak2 = hardwareMap.get(DigitalChannel.class, "beamBreak2");
		beamBreak2.setMode(DigitalChannel.Mode.INPUT);

		colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

		artifactColorsInSpindexer = new DecodeDataTypes.ArtifactColor[] {
				DecodeDataTypes.ArtifactColor.NONE,
				DecodeDataTypes.ArtifactColor.NONE,
				DecodeDataTypes.ArtifactColor.NONE
		};
	}

	@Override
	public void update() {}

	public double getSpindexerEncoderPosition() {
		//return (spindexerServoEncoder.getVoltage() / 3.3) - (15.0 / 360.0);
		double pos = ((spindexerServoEncoder.getVoltage() - 0.170) / 2.96);
		double realPos = pos;
		if (pos >= 1){
			realPos = pos - 1;
		}
		return realPos;
	}


	public boolean spindexerEncoderIsWithinTolerance(double position, double tolerance) {
		return Math.abs(getSpindexerEncoderPosition() - position) <= tolerance;
	}

	public StellarServo getSpindexerServo() {
		return spindexerServo;
	}

	public boolean getBreamBreak1Broken() {
		return !beamBreak1.getState();
	}

	public boolean getBeamBreak2Broken() {
		return !beamBreak2.getState();
	}

	public DecodeDataTypes.ArtifactColor[] getArtifactColorsInSpindexer() {
		return artifactColorsInSpindexer;
	}

	public void setArtifactColorsInSpindexerFromSupplier(Supplier<Integer> segmentSupplier, DecodeDataTypes.ArtifactColor artifactColor) {
		setArtifactColorInSpindexer(segmentSupplier.get(), artifactColor);
	}

	public void setArtifactColorInSpindexer(int index, DecodeDataTypes.ArtifactColor artifactColor) {
		artifactColorsInSpindexer[index] = artifactColor;
		//new SetLight(PedroDrivebase.getInstance().getLeftLight(), artifactColor.toString());
		//new SetLight(PedroDrivebase.getInstance().getRightLight(), artifactColor.toString());
	}

	public DecodeDataTypes.ArtifactColor setArtifactColorAtSegmentToColorSensor(int segment) {
		setArtifactColorInSpindexer(segment, getColorSensorArtifactColor());

		return getColorSensorArtifactColor();
	}

	public DecodeDataTypes.ArtifactColor getColorSensorArtifactColor() {
		NormalizedRGBA colorSensorColors = colorSensor.getNormalizedColors();

		float greenToBlueRatio = colorSensorColors.green / colorSensorColors.blue;
		float total = colorSensorColors.red + colorSensorColors.green + colorSensorColors.blue;

		if (total < 0.03) {
			return DecodeDataTypes.ArtifactColor.NONE;
		}

		return greenToBlueRatio < 0.9 ?
			DecodeDataTypes.ArtifactColor.PURPLE :
			DecodeDataTypes.ArtifactColor.GREEN;
	}

	public int getFirstColorSegmentLocation(DecodeDataTypes.ArtifactColor color) {
		for (int segment = 0; segment < 3; segment++) {
				if (artifactColorsInSpindexer[segment] == color) {
					return segment;
				}
		}

		// no segments
		return -1;
	}

	public boolean getHasArtifactColor(DecodeDataTypes.ArtifactColor artifactColor) {
		for (int i = 0; i < 3; i++) {
			if (artifactColorsInSpindexer[i] == artifactColor) {
				return true;
			}
		}

		return false;
	}
	public DecodeDataTypes.ArtifactColor getArtifactColorAt(int i){
		return artifactColorsInSpindexer[i];
	}

	public double getDegreesForSegmentSupplierAndPosition(Supplier<Integer> segmentSupplier, @NonNull Position position) {
		return getServoPositionFromSegment(segmentSupplier.get(), position);
	}

	public double getServoPositionFromSegment(int segment, @NonNull Position position) {
		if (position == Position.INTAKE) {
			return INTAKE_POSITIONS[segment];
		} else if (position == Position.TRANSFER) {
			return TRANSFER_POSITIONS[segment];
		} else {
			throw new IllegalArgumentException("Invalid Spindexer Position provided: " + position);
		}
	}
	public int getFirstArtifactLocation() {
		for (int segment = 0; segment < 3; segment++) {
			if (artifactColorsInSpindexer[segment] != DecodeDataTypes.ArtifactColor.NONE) {
				return segment;
			}
		}

		// no segments
		return -1;
	}

	public int getMotifSegmentOrFirstArtifact(int motifIndex) {
		int firstColorSegment = getFirstColorSegmentLocation(GameState.getMotifPatternAt(motifIndex));
		if (firstColorSegment != -1) {
			return firstColorSegment;
		}

		return getFirstArtifactLocation();
	}
	/*
	public boolean getIsTransferPosition() {
		double currentPosition = spindexerServo.getPosition();
		double tolerance = 0.01;

		for (double transferDegreePosition : TRANSFER_DEGREE_POSITIONS) {
			if (Math.abs(currentPosition - (transferDegreePosition * DEGREES_TO_SERVO)) < tolerance) {
				return true;
			}
		}

		return false;
	}*/

	@NonNull
	@Override
	public String toString() {
		//NormalizedRGBA colorSensorColors = colorSensor.getNormalizedColors();
		return String.format(
				"beamBreak1: %b\n" +
				"beamBreak2: %b\n" +
				//"colorSensorRGB: %f, %f, %f\n"+
				"Artifact Storage: %s, %s, %s\n"+
				//"G/B ratio: %f\n" +
				//"Total RGB: %f\n" +
				"Spindexer Servo: %f\n" +
				"Spindexer Encoder: %f\n"+
				"Motif Sequence: %s, %s, %s\n",
				getBreamBreak1Broken(),
				getBeamBreak2Broken(),
				//colorSensorColors.red, colorSensorColors.green, colorSensorColors.blue,
				//colorSensor.red(), colorSensor.green(), colorSensor.blue(),
				artifactColorsInSpindexer[0].toString(), artifactColorsInSpindexer[1].toString(), artifactColorsInSpindexer[2].toString(),
				//colorSensorColors.green / colorSensorColors.blue,
				//getGreenToBlueRatio(),
				//getRGBSum(),
				spindexerServo.getPosition(),
				getSpindexerEncoderPosition(),
				GameState.getMotifPatternAt(0),
				GameState.getMotifPatternAt(1),
				GameState.getMotifPatternAt(2)
		);
	}
}