package org.firstinspires.ftc.teamcode.casebot.subsystems;

import android.util.Size;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

public final class Camera extends Subsystem {

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void init(HardwareMap hardwareMap) {
        // 1. Initialize the AprilTag Processor
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(false)
                .setDrawTagOutline(false)
                .setOutputUnits(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH,
                        org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES)
                .build();

        // 2. Initialize the Vision Portal
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "camera")) // Ensure name matches configuration
                .addProcessor(aprilTag)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();
    }

    /**
     * Returns the horizontal angle (bearing) to a specific AprilTag.
     * @param targetId The ID of the tag to locate.
     * @return The angle in degrees. 0 if the tag is centered or not found.
     */
    public double getTagBearingDegrees(int targetId) {
        if (aprilTag == null) return 0.0;

        List<AprilTagDetection> detections = aprilTag.getDetections();

        for (AprilTagDetection detection : detections) {
            // Check if metadata exists and if this is the tag we want
            if (detection.metadata != null && detection.id == targetId) {
                return detection.ftcPose.bearing;
            }
        }
        return 0.0;
    }

    @Override
    public void update() {
        // The VisionPortal runs in the background, so usually nothing is needed here
    }

    // It's good practice to close the portal when the robot stops
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
    @NonNull
    @Override
    public String debugTelemetry() {
        return String.format("Camera Degrees Offset %f",  getTagBearingDegrees(20));
    }
}