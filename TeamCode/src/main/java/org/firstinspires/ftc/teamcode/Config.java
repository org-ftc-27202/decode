package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class Config {
    public enum AllianceColors {RED, BLUE}
    public static AllianceColors allianceColor = AllianceColors.RED;
    public static Pose autoEndPose = new Pose(112, 135.5, Math.toRadians(90));
    public enum MotifPatterns {GPP, PGP, PPG}
    public static MotifPatterns motifPattern = MotifPatterns.GPP;
    public enum Colors {PURPLE, GREEN}
    public static Colors catapult01Color = Colors.GREEN;
    public static Colors catapult02Color = Colors.PURPLE;
    public static Colors catapult03Color = Colors.PURPLE;
}
