package org.firstinspires.ftc.teamcode.util;

public class GameState {
    private static GameState gameState = new GameState();
    private GameState() {}

    public GameState getInstance() {
        return gameState;
    }

    private static DecodeDataTypes.ArtifactColor[] motifPattern = {
            DecodeDataTypes.ArtifactColor.PURPLE,
            DecodeDataTypes.ArtifactColor.GREEN,
            DecodeDataTypes.ArtifactColor.PURPLE,
    };

    public static void setMotifPattern(
            DecodeDataTypes.ArtifactColor color1,
            DecodeDataTypes.ArtifactColor color2,
            DecodeDataTypes.ArtifactColor color3
    ) {
        motifPattern[0] = color1;
        motifPattern[1] = color2;
        motifPattern[2] = color3;
    }

    public static DecodeDataTypes.ArtifactColor getMotifPatternAt(int index) {
        return motifPattern[index];
    }
}
