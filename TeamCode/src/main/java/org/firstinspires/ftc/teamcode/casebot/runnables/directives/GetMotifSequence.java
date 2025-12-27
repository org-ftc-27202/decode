package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;
import org.firstinspires.ftc.teamcode.util.GameState;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/*public class GetMotifSequence extends Directive {
    private AprilTagLibrary gameTagLibrary;
    private AprilTagProcessor aprilTag;
    private VisionPortal.Builder builder;

    public GetMotifSequence() {
        setInterruptible(false);
        setRequiredSubsystems();
    }

    @Override
    public void onStart(boolean hadToInterruptToStart) {
        //AprilTags.PositionalPipeline positionalPipeline = new AprilTags.PositionalPipeline();

        gameTagLibrary = AprilTagGameDatabase.getDecodeTagLibrary();

        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(gameTagLibrary)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                //.setLensIntrinsics(397.917, 397.917, 666.941, 358.274) // Calibrated at 1280x720
                .build();

        builder = new VisionPortal.Builder()
                .setCamera(Turret.getInstance().getWebcamName())
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                //.addProcessor(positionalPipeline)
                .addProcessor(aprilTag)
        ;
    }

    @Override
    public void update() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        int motifAprilTabCount = 0;
        DecodeDataTypes.ArtifactColor[] motifSequence = new DecodeDataTypes.ArtifactColor[3];

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) { // check if metadata is available
                switch (detection.metadata.id) {
                    case 21:
                        motifAprilTabCount++;

                        motifSequence[0] = DecodeDataTypes.ArtifactColor.GREEN;
                        motifSequence[1] = DecodeDataTypes.ArtifactColor.PURPLE;
                        motifSequence[2] = DecodeDataTypes.ArtifactColor.PURPLE;

                        continue;
                    case 22:
                        motifAprilTabCount++;

                        motifSequence[0] = DecodeDataTypes.ArtifactColor.PURPLE;
                        motifSequence[1] = DecodeDataTypes.ArtifactColor.GREEN;
                        motifSequence[2] = DecodeDataTypes.ArtifactColor.PURPLE;

                        continue;
                    case 23:
                        motifAprilTabCount++;

                        motifSequence[0] = DecodeDataTypes.ArtifactColor.PURPLE;
                        motifSequence[1] = DecodeDataTypes.ArtifactColor.PURPLE;
                        motifSequence[2] = DecodeDataTypes.ArtifactColor.GREEN;
                }
            }
        }
        if (motifAprilTabCount == 1) {
            GameState.setMotifPattern(
                    motifSequence[0],
                    motifSequence[1],
                    motifSequence[2]
            );
        }
    }

    @Override
    public void onStop(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}*/
public class GetMotifSequence extends Directive {
    private AprilTagLibrary gameTagLibrary;
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal; // Field to hold the portal
    private boolean isTargetFound = false; // Flag to finish the command

    public GetMotifSequence() {
        setInterruptible(true);
        setRequiredSubsystems(subsystem(Camera.class)); // Ideally, require the Turret/Camera subsystem here if you have one
    }

    @Override
    protected void onStart(boolean hadToInterruptToStart) {
        gameTagLibrary = AprilTagGameDatabase.getDecodeTagLibrary();

        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(gameTagLibrary)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        // FIX 1: Actually build the portal and assign it to the variable
        visionPortal = new VisionPortal.Builder()
                .setCamera(subsystem(Turret.class).getWebcamName())
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(aprilTag)
                .build();

        isTargetFound = false;
    }

    @Override
    protected void onUpdate() {
        // If we already found it, don't keep processing
        if (isTargetFound) return;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        // Loop through detections
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                // Check ID
                if (detection.metadata.id == 21) {
                    GameState.setMotifPattern(DecodeDataTypes.ArtifactColor.GREEN, DecodeDataTypes.ArtifactColor.PURPLE, DecodeDataTypes.ArtifactColor.PURPLE);
                    isTargetFound = true;
                    break;
                } else if (detection.metadata.id == 22) {
                    GameState.setMotifPattern(DecodeDataTypes.ArtifactColor.PURPLE, DecodeDataTypes.ArtifactColor.GREEN, DecodeDataTypes.ArtifactColor.PURPLE);
                    isTargetFound = true;
                    break;
                } else if (detection.metadata.id == 23) {
                    GameState.setMotifPattern(DecodeDataTypes.ArtifactColor.PURPLE, DecodeDataTypes.ArtifactColor.PURPLE, DecodeDataTypes.ArtifactColor.GREEN);
                    isTargetFound = true;
                    break;
                }
            }
        }
    }

    @Override
    protected void onStop(boolean interrupted) {
        // FIX 2: Close the camera to prevent crashes
            // Check if the portal exists before trying to close it
            if (visionPortal != null) {

                // Optional: Stop streaming first to ensure a clean shutdown
                if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
                    visionPortal.stopStreaming();
                }

                // CRITICAL: Release the camera hardware so it can be used again
                visionPortal.close();
            }

    }

    @Override
    protected boolean isFinished() {
        return isTargetFound;
    }
}
