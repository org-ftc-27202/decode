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

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.StreamFormat;
import org.firstinspires.ftc.vision.VisionProcessor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Size;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;

@TeleOp(name = "April Tags Test", group = "Robot")

public class AprilTags extends LinearOpMode {
	@Override
    public void runOpMode() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        PositionalPipeline positionalPipeline = new PositionalPipeline();

        AprilTagLibrary gameTagLibrary = AprilTagGameDatabase.getDecodeTagLibrary();

        AprilTagProcessor aprilTag= new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(gameTagLibrary)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setLensIntrinsics(397.917, 397.917, 666.941, 358.274) // Calibrated at 1280x720
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCamera(webcamName)
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(StreamFormat.MJPEG)
                .addProcessor(positionalPipeline)
                .addProcessor(aprilTag);

        VisionPortal visionPortal = builder.build();

        telemetry.addLine("Waiting for start");
        telemetry.update();

        // Wait for the user to press start on the Driver Station
        waitForStart();

        FtcJsonStorage storage = new FtcJsonStorage(hardwareMap.appContext);

        /*
        storage.writeToInternalStorage(
                "autoData.json",
                ArtifactSequence.of(ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE),
                new Coords(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                new MotorPositions.Builder()
                        .addMotor("hoodedShooter", 1.0)
                        .addMotor("carouselPaddles", 2.0)
                        .addMotor("botFlipper", 3.0)
                        .build(),
                new DateMs(System.currentTimeMillis())
        );*/

        ArtifactSequence artifactSequence = new ArtifactSequence();
        Coords coords = new Coords();
        MotorPositions motorPositions = new MotorPositions.Builder().build();
        DateMs dateMs = new DateMs();

        boolean read = storage.readAndParseAutoData("autoData.json", artifactSequence, coords, motorPositions, dateMs);

        while (opModeIsActive()) {
            telemetry.addData("read", read);
            //telemetry.addData("Artifact sequence", artifactSequence.toStringArray());
            //telemetry.addData("Coords", coords.getX() + ", " + coords.getY() + ", " + coords.getZ() + ", " + coords.getPitch() + ", " + coords.getRoll() + ", " + coords.getYaw());
            //telemetry.addData("Motor positions", motorPositions.getMotorPosition("hoodedShooter") + ", " + motorPositions.getMotorPosition("carouselPaddles") + ", " + motorPositions.getMotorPosition("botFlipper"));
            //telemetry.addData("Date ms", dateMs.getMinutesTimeSince());


            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            telemetry.addData("# AprilTags Detected", currentDetections.size());

            // Loop through each detection and display its info
            boolean detectsBlueTag = false; //id 20
            boolean detectsRedTag = false; //id 24
            ArtifactSequence artifactColors;

            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) { // Check if metadata is available
                    if (detection.metadata.id == 20) {
                        detectsBlueTag = true;
                    } else if (detection.metadata.id == 24) {
                        detectsRedTag = true;
                    }

                    switch (detection.metadata.id) {
                        case 21:
                            artifactColors = ArtifactSequence.of(
                                ArtifactColor.GREEN,
                                ArtifactColor.PURPLE,
                                ArtifactColor.PURPLE
                            );

                        case 22:
                            artifactColors = ArtifactSequence.of(
                                    ArtifactColor.PURPLE,
                                    ArtifactColor.GREEN,
                                    ArtifactColor.PURPLE
                            );
                        case 23:
                            artifactColors = ArtifactSequence.of(
                                    ArtifactColor.PURPLE,
                                    ArtifactColor.PURPLE,
                                    ArtifactColor.GREEN
                            );
                    }

                    telemetry.addLine(String.format("\n==== ID %d (%s)", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                } else {
                    /*
                    telemetry.addLine(String.format("\n==== ID %d (Unknown)", detection.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
                    */
                }
            }
            /*
            telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
            telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
            telemetry.addLine("RBE = Range, Bearing & Elevation");
            */
            telemetry.addData("Blue Goal Tag Detected:", detectsBlueTag);
            telemetry.addData("Red Goal Tag Detected:", detectsRedTag);
            //telemetry.addData("Artifact Color", artifactColors[i]);



            //telemetry.addData("Total frame time ms", visionPortal.getTotalFrameTimeMs());
            //telemetry.addData("Pipeline time ms", visionPortal.getPipelineTime());
            //telemetry.addData("Overhead time ms", visionPortal.getOverheadTime());
            //telemetry.addData("Theoretical max FPS", visionPortal.getCurrentPipelineMaxFps());
            //telemetry.addData("Angle", myPipeline.getAnalysis());
            telemetry.update();

            if (gamepad1.a) {
                // IMPORTANT NOTE: calling stopStreaming() will indeed stop the stream of images
                // from the camera (and, by extension, stop calling your vision pipeline). HOWEVER,
                // if the reason you wish to stop the stream early is to switch use of the camera
                // over to, say, Vuforia or TFOD, you will also need to call closeCameraDevice()
                // (commented out below), because according to the Android Camera API documentation:
                //	     "Your application should only have one Camera object active at a time for
                //	      a particular hardware camera."
                //
                // NB: calling closeCameraDevice() will internally call stopStreaming() if applicable,
                // but it doesn't hurt to call it anyway, if for no other reason than clarity.
                //
                // NB2: if you are stopping the camera stream to simply save some processing power
                // (or battery power) for a short while when you do not need your vision pipeline,
                // it is recommended to NOT call closeCameraDevice() as you will then need to re-open
                // it the next time you wish to activate your vision pipeline, which can take a bit of
                // time. Of course, this comment is irrelevant in light of the use case described in
                // the above "important note".
                visionPortal.close(); //closes communication with USB permanently
                //visionPortal.stopStreaming(); //can be re-enabled
                //phoneCam.closeCameraDevice();
            }

            sleep(100);
        }
    }

    public static class PositionalPipeline implements VisionProcessor {
        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile double aprilTags;

        @Override
        public void init(int width, int height, CameraCalibration calibration) {
            // We need to call this in order to make sure the 'Cb' object is initialized, so that the
            // submats we make will still be linked to it on subsequent frames. (If the object were to
            // only be initialized in processFrame, then the submats would become delinked because the
            // backing buffer would be re-allocated the first time a real frame was crunched)
            //inputToCb(firstFrame);

            // Submats are a persistent reference to a region of the parent buffer. Any changes to the
            // child affect the parent, and the reverse also holds true.
            /*
            region1_Cb = colorR.submat(new Rect(region1_pointA, region1_pointB));
            region2_Cb = colorR.submat(new Rect(region2_pointA, region2_pointB));
            region3_Cb = colorR.submat(new Rect(region3_pointA, region3_pointB));*/
        }

        public boolean areWithin(double i1, double i2, double range) {
            return Math.abs(i1 - i2) <= range;
        }

        @Override
        public Mat processFrame(Mat frame, long captureTimeNanos) {
            Mat flippedFrame = new Mat();

            // Flip camera feed right side-up
            //Core.flip(frame, flippedFrame, -1);

            return flippedFrame;
        }

        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
            Mat processedMat = (Mat) userContext;

            if (processedMat != null && !processedMat.empty()) {
                Bitmap processedBitmap = Bitmap.createBitmap(processedMat.cols(), processedMat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(processedMat, processedBitmap);

                canvas.drawBitmap(processedBitmap, 0, 0, null);
            }
        }

        // Call this from the OpMode thread to obtain the latest analysis
        public double getAnalysis() {
            return aprilTags;
        }
    }
}
