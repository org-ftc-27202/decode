package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class Config {
    public enum AllianceColors {RED, BLUE}
    public static AllianceColors allianceColor = AllianceColors.RED;
    public enum PatternOptions {GPP, PGP, PPG}
    public static PatternOptions pattern = PatternOptions.GPP;
    public static Pose autoEndPose = null;
}
