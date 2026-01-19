package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class Config {
    public enum AllianceColors {RED, BLUE}
    public static AllianceColors allianceColor = AllianceColors.RED;
    public enum GoalOptions {NEAR, FAR}
    public static GoalOptions goalOption = GoalOptions.NEAR;
    public enum MotifPatterns {GPP, PGP, PPG}
    public static MotifPatterns motifPattern = MotifPatterns.GPP;
    public enum Colors {PURPLE, GREEN}
    public static Colors catapult01Color = Colors.GREEN;
    public static Colors catapult02Color = Colors.PURPLE;
    public static Colors catapult03Color = Colors.PURPLE;
    public static Pose autoEndPose = new Pose(113, 129.5, Math.toRadians(90));
    public static double deltaToCenterX = 0.0, deltaToCenterY = 0.0, deltaToCenterAngleInDeg = 0.0;
}
