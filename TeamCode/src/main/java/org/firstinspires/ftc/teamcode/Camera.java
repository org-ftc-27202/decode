package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;

public class Camera implements Subsystem {
    public static final Camera INSTANCE = new Camera();
    public Camera() { }
    private HuskyLens huskyLensFront;
    //        , huskylensLeft, huskylensRight;
    public boolean flagCaptured = false;

    public void mapCameraHardware(HardwareMap hardwareMap) {
        huskyLensFront = hardwareMap.get(HuskyLens.class, "huskylensFront");
        huskyLensFront.knock();
        huskyLensFront.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
    }
    public Command capturePattern = new LambdaCommand("capturePattern")
            .setStart(() -> {
                flagCaptured = false;

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
                        Config.pattern = Config.PatternOptions.GPP;
                    } else if (blocks[blockIndex].id == 2) {
                        Config.pattern = Config.PatternOptions.PGP;
                    } else if (blocks[blockIndex].id == 3) {
                        Config.pattern = Config.PatternOptions.PPG;
                    }
                }
                flagCaptured = true;
                })
            .setUpdate(() -> {})
            .setIsDone(() -> flagCaptured)
            .setStop(interrupted -> {})
            .setInterruptible(false)
            .requires(this);
}
