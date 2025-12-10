package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class botVisionFront {
//    private final int READ_PERIOD = 1;  // value of 1 in sample code, but mentioned that it is not needed for typical applications

    private HuskyLens huskylens;
    public boolean flagStatus = false;  // true = ok; false = not ok
    public int blockLength = 0;

    public enum PatternOptions {GPP, PGP, PPG};
    public botVisionFront.PatternOptions pattern = botVisionFront.PatternOptions.GPP;
    private TeleOp_00_base.AllianceColors allianceColor = TeleOp_00_base.AllianceColors.RED;

    public botVisionFront(HardwareMap hardwareMap, TeleOp_00_base.AllianceColors inAllianceColor) {
        huskylens = hardwareMap.get(HuskyLens.class, "huskylens01");
        /*
         * This sample rate limits the reads solely to allow a user time to observe
         * what is happening on the Driver Station telemetry.  Typical applications
         * would not likely rate limit.
         */
//        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);

        /*
         * Immediately expire so that the first time through we'll do the read.
         */
//        rateLimit.expire();

        /*
         * Basic check to see if the device is alive and communicating.  This is not
         * technically necessary here as the HuskyLens class does this in its
         * doInitialization() method which is called when the device is pulled out of
         * the hardware map.  However, sometimes it's unclear why a device reports as
         * failing on initialization.  In the case of this device, it's because the
         * call to knock() failed.
         */
        flagStatus = huskylens.knock();
        allianceColor = inAllianceColor;
        huskylens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
    }

    public class capturePattern implements Action {
        private boolean initialized = false;
        private int blockIndex = 0;
        private int minX = 0;
        private int maxX = 0;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;

                HuskyLens.Block[] blocks = huskylens.blocks();

                blockLength = blocks.length;
                if (blockLength != 0) {
                    blockIndex = 0;
                    minX = blocks[0].x;
                    maxX = blocks[0].x;

                    // if RED alliance, then get the leftmost, otherwise, go rightmost
                    if (allianceColor == TeleOp_00_base.AllianceColors.RED) {
                        for (int i = 0; i < blockLength; i++) {
                            if (blocks[i].x < minX) {
                                minX = blocks[i].x;
                                blockIndex = i;
                            }
                        }
                    } else if (allianceColor == TeleOp_00_base.AllianceColors.BLUE) {
                        for (int i = 0; i < blockLength; i++) {
                            if (blocks[i].x > maxX) {
                                maxX = blocks[i].x;
                                blockIndex = i;
                            }
                        }
                    }

                    if (blocks[blockIndex].id == 1) {
                        pattern = botVisionFront.PatternOptions.GPP;
                    } else if (blocks[blockIndex].id == 2) {
                        pattern = botVisionFront.PatternOptions.PGP;
                    } else if (blocks[blockIndex].id == 3) {
                        pattern = botVisionFront.PatternOptions.PPG;
                    }
                }
            }

            return false;
        }
    }

    public Action capturePattern() {
        return new botVisionFront.capturePattern();
    }
}

