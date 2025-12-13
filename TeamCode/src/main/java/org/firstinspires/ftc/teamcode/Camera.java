package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;

public class Camera implements Subsystem {
    public static final Camera INSTANCE = new Camera();
    public Camera() { }
    private HuskyLens huskyLensFront, huskylensLeft, huskylensRight;
    private boolean flagCapturePatternComplete = false;
    private boolean flagGetCatapultArtifactColors = false;

    public void mapCameraHardware(HardwareMap hardwareMap) {
        huskyLensFront = hardwareMap.get(HuskyLens.class, "huskylensFront");
        huskyLensFront.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        huskyLensFront.knock();

        huskylensLeft = hardwareMap.get(HuskyLens.class, "huskylensLeft");
        huskylensLeft.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        huskylensLeft.knock();

        huskylensRight = hardwareMap.get(HuskyLens.class, "huskylensRight");
        huskylensRight.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        huskylensRight.knock();
    }
    public Command capturePattern = new LambdaCommand("capturePattern")
            .setStart(() -> {
                flagCapturePatternComplete = false;
                int blockLength, blockIndex, minX, maxX;
                HuskyLens.Block[] blocks = huskyLensFront.blocks();
                blockLength = blocks.length;
                if (blockLength != 0) {
                    blockIndex = 0;
                    minX = blocks[0].x;
                    maxX = blocks[0].x;

                    // if RED alliance, then get the leftmost, otherwise, go rightmost
                    if (Config.allianceColor == Config.AllianceColors.RED) {
                        for (int i = 0; i < blockLength; i++) {
                            if (blocks[i].x < minX) {
                                minX = blocks[i].x;
                                blockIndex = i;
                            }
                        }
                    } else if (Config.allianceColor == Config.AllianceColors.BLUE) {
                        for (int i = 0; i < blockLength; i++) {
                            if (blocks[i].x > maxX) {
                                maxX = blocks[i].x;
                                blockIndex = i;
                            }
                        }
                    }

                    if (blocks[blockIndex].id == 1) {
                        Config.motifPattern = Config.MotifPatterns.GPP;
                    } else if (blocks[blockIndex].id == 2) {
                        Config.motifPattern = Config.MotifPatterns.PGP;
                    } else if (blocks[blockIndex].id == 3) {
                        Config.motifPattern = Config.MotifPatterns.PPG;
                    }
                }
                flagCapturePatternComplete = true;
                })
            .setUpdate(() -> {})
            .setIsDone(() -> flagCapturePatternComplete)
            .setStop(interrupted -> {})
            .setInterruptible(false)
            .requires(this);
    public Command getCatapultArtifactColors = new LambdaCommand("getCatapultArtifactColors")
            .setStart(() -> {
                flagGetCatapultArtifactColors = false;
                HuskyLens.Block[] huskylensLeftBlocks = huskylensLeft.blocks();
                HuskyLens.Block[] huskylensRightBlocks = huskylensRight.blocks();
                int huskylensLeftBlockLength = huskylensLeftBlocks.length;
                int huskylensRightBlockLength = huskylensRightBlocks.length;
                int minX, maxX, blockMinIndex, blockMaxIndex;

                Config.catapult01Color = Config.Colors.PURPLE;
                Config.catapult02Color = Config.Colors.PURPLE;
                Config.catapult03Color = Config.Colors.PURPLE;

                // Right-side Camera to get Catapult01 and Catapult02 colors
                minX = 0;
                maxX = 0;
                blockMinIndex = 0;
                blockMaxIndex = 0;
                for (int i = 0; i < huskylensRightBlockLength; i++) {
                    if (huskylensRightBlocks[i].x < minX) {
                        minX = huskylensRightBlocks[i].x;
                        blockMinIndex = i;
                    }
                    if (huskylensRightBlocks[i].x > maxX) {
                        maxX = huskylensRightBlocks[i].x;
                        blockMaxIndex = i;
                    }
                }

                if (huskylensRightBlockLength == 2) {
                    if (huskylensRightBlocks[blockMinIndex].id == 2) {
                        Config.catapult02Color = Config.Colors.GREEN;
                    }
                    if (huskylensRightBlocks[blockMaxIndex].id == 2) {
                        Config.catapult01Color = Config.Colors.GREEN;
                    }
                } else if (huskylensRightBlockLength == 1) {
                    if (huskylensRightBlocks[0].id == 2) {
                        Config.catapult02Color = Config.Colors.GREEN;
                    }
                }

//                // Left-side Camera to get Catapult02 and Catapult03 colors
//                minX = 0;
//                maxX = 0;
//                blockMinIndex = 0;
//                blockMaxIndex = 0;
//                for (int i = 0; i < huskylensLeftBlockLength; i++) {
//                    if (huskylensLeftBlocks[i].x < minX) {
//                        minX = huskylensLeftBlocks[i].x;
//                        blockMinIndex = i;
//                    }
//                    if (huskylensLeftBlocks[i].x > maxX) {
//                        maxX = huskylensLeftBlocks[i].x;
//                        blockMaxIndex = i;
//                    }
//                }
//
//                if (huskylensLeftBlockLength == 2) {
//                    if (huskylensLeftBlocks[blockMinIndex].id == 1) {
//                        Config.catapult03Color = Config.Colors.PURPLE;
//                    } else if (huskylensLeftBlocks[blockMinIndex].id == 2) {
//                        Config.catapult03Color = Config.Colors.GREEN;
//                    }
//                    if (huskylensLeftBlocks[blockMaxIndex].id == 1) {
//                        Config.catapult02Color = Config.Colors.PURPLE;
//                    } else if (huskylensLeftBlocks[blockMaxIndex].id == 2) {
//                        Config.catapult02Color = Config.Colors.GREEN;
//                    }
//                } else if (huskylensLeftBlockLength == 1) {
//                    if (huskylensLeftBlocks[0].id == 1) {
//                        Config.catapult02Color = Config.Colors.PURPLE;
//                    } else if (huskylensLeftBlocks[0].id == 2) {
//                        Config.catapult02Color = Config.Colors.GREEN;
//                    }
//                    Config.catapult03Color = Config.Colors.PURPLE;
//                }
//                else {
//                    Config.catapult02Color = Config.Colors.PURPLE;
//                    Config.catapult03Color = Config.Colors.PURPLE;
//                }

                flagGetCatapultArtifactColors = true;
            })
            .setUpdate(() -> {})
            .setIsDone(() -> flagGetCatapultArtifactColors)
            .setStop(interrupted -> {})
            .setInterruptible(false)
            .requires(this);
}
