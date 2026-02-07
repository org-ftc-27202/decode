package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class TurretPIDFConstants {
    public static double p_left = 70.0;
    public static double i_left = 0.2;
    public static double d_left = 0.5;
    public static double f_left = 15.15;

    public static double p_right = 70.0;
    public static double i_right = 0.2;
    public static double d_right = 0.5;
    public static double f_right = 15.15;
    /*public static double p_left = 30.0;
    public static double i_left = 0.01;
    public static double d_left = 1.0;
    public static double f_left = 14.3;

    public static double p_right = 30.0;
    public static double i_right = 0.01;
    public static double d_right = 1.0;
    public static double f_right = 13.7;*/
}
