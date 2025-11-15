package org.firstinspires.ftc.teamcode.tars.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public final class Spindexer extends Subsystem {
	private static final Spindexer spindexer = new Spindexer();

	public static Spindexer getInstance() {
		return spindexer;
	}

	private Spindexer() {}

	private final static double DEGREES_TO_SERVO = 1.0 / 320.0;
	private final static double SPINDEXER_OFFSET = 0.0;
	private final static double[] INTAKE_DEGREE_POSITIONS = {0.0 + SPINDEXER_OFFSET, 120.0 + SPINDEXER_OFFSET, 240.0 + SPINDEXER_OFFSET};
	private final static double[] TRANSFER_DEGREE_POSITIONS = {180.0 + SPINDEXER_OFFSET, 300.0 + SPINDEXER_OFFSET, 60.0 + SPINDEXER_OFFSET};

	public final static double BUFFER_TIME = 1;

	private double ratio;

	private final DecodeDataTypes.ArtifactColor[] artifactColorsInSpindexer = new DecodeDataTypes.ArtifactColor[]{
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
	private DigitalChannel beamBreak;
	private ColorSensor colorSensor;

	@Override
	public void init(HardwareMap hardwareMap) {
		spindexerServo = new StellarServo(hardwareMap, "spindexer");
		beamBreak = hardwareMap.get(DigitalChannel.class, "beamBreak"); //unused
		beamBreak.setMode(DigitalChannel.Mode.INPUT);

		colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

	}

	@Override
	public void update() {
		setRatio();
	}

	public StellarServo getSpindexerServo() {
		return spindexerServo;
	}

	public DigitalChannel getBeamBreak() {
		return beamBreak;
	}

	public ColorSensor getColorSensor() {
		return colorSensor;
	}

	public DecodeDataTypes.ArtifactColor[] getArtifactColorsInSpindexer() {
		return artifactColorsInSpindexer;
	}

	public void setArtifactColorInSpindexer(int index, DecodeDataTypes.ArtifactColor artifactColor) {
		artifactColorsInSpindexer[index] = artifactColor;
	}

	public DecodeDataTypes.ArtifactColor setArtifactColorAtSegmentToColorSensor(int segment) {
		setArtifactColorInSpindexer(segment, getColorSensorArtifactColor());

		return getColorSensorArtifactColor();
	}
	public void setRatio(){
		ratio = (double) colorSensor.green() /colorSensor.blue();
	}
	public DecodeDataTypes.ArtifactColor getColorSensorArtifactColor() {


		if ((ratio > 1.0)&&(ratio<1.3)) {
				return DecodeDataTypes.ArtifactColor.NONE;
		}

		return ratio < 1.0 ?
				DecodeDataTypes.ArtifactColor.PURPLE :
				DecodeDataTypes.ArtifactColor.GREEN;
	}
	public int getFirstColorSegmentLocation(DecodeDataTypes.ArtifactColor color) {
		for (int i = 0; i < 3; i++){
				if (artifactColorsInSpindexer[i] == color){
					return i;
				}
		}

		// no segments
		return -1;
	}

	public boolean getHasArtifactColor(DecodeDataTypes.ArtifactColor artifactColor) {
		for (int i = 0; i < 3; i++){
			if (artifactColorsInSpindexer[i] == artifactColor){
				return true;
			}
		}

		return false;
	}

	public double getDegreesForSegmentPosition(int segment, @NonNull Position position) {
		if (position == Position.INTAKE) {
			return INTAKE_DEGREE_POSITIONS[segment] * DEGREES_TO_SERVO;
		} else if (position == Position.TRANSFER) {
			return TRANSFER_DEGREE_POSITIONS[segment] * DEGREES_TO_SERVO;
		} else {
			throw new IllegalArgumentException("Invalid Spindexer.Position provided: " + position);
		}
	}

	public boolean getIsTransferPosition() {
		double currentPosition = spindexerServo.getPosition();
		double tolerance = 0.01;

		for (double transferDegreePosition : TRANSFER_DEGREE_POSITIONS) {
			if (Math.abs(currentPosition - (transferDegreePosition * DEGREES_TO_SERVO)) < tolerance) {
				return true;
			}
		}

		return false;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(
				"beamBreak: %b\n" +
				"colorSensorRGB: %d, %d, %d"+
				"Artifact Storage Sequence: %s, %s, %s\n"+
				"G/B ratio: %f",
				beamBreak.getState(),
				colorSensor.red(), colorSensor.green(), colorSensor.blue(),
				artifactColorsInSpindexer[0].toString(), artifactColorsInSpindexer[1].toString(), artifactColorsInSpindexer[2].toString(),
				ratio
		);
	}
}