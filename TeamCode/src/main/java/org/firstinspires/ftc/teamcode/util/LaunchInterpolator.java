package org.firstinspires.ftc.teamcode.util;
import java.util.Map;
import java.util.TreeMap;

public class LaunchInterpolator {
    private LaunchInterpolator() {}

    static TreeMap<Double, LaunchParameters> launchData = new TreeMap<>(
            Map.of(
                    5.0, new LaunchParameters(100,100),
                    7.5, new LaunchParameters(100,100),
                    10.0, new LaunchParameters(150, 100),
                    15.0, new LaunchParameters(200,200)
            )
    );

    // LaunchInterpolar.getEstimatedLaunchParameters(0.0);

    public static LaunchParameters getEstimatedLaunchParameters(double realDistance) {
        Double lowerBoundKey = launchData.floorKey(realDistance);
        Double upperBoundKey = launchData.ceilingKey(realDistance);
        if (lowerBoundKey == null || upperBoundKey == null) {
            return null;
        }

        double x1 = lowerBoundKey;
        double x2 = upperBoundKey;

        double angle1 = launchData.get(x1).getAngle();
        double angle2 = launchData.get(x2).getAngle();

        double velocity1 = launchData.get(x1).getVelocity();
        double velocity2 = launchData.get(x2).getVelocity();
        double interpolatedAngle = interpolateValues(realDistance, x1, x2, angle1, angle2);
        double interpolatedVelocity = interpolateValues(realDistance, x1, x2, velocity1, velocity2);
        return new LaunchParameters(interpolatedAngle, interpolatedVelocity);


    }
    public static double interpolateValues(double x, double x1, double x2, double y1, double y2){
        if (x1 == x2) {
            return (y1);
        }

        // x
        // x - x1
        // x2 - x

        /*
        x - x1
        x2 - x

        ((x - x1) / (x) * y1 +
        ((x2 - x) / (x)) * y2


        (x1 / x) * y1 +
        (x2 / x) * y2
         */


        return (
                    ((y2 - y1) * (x - x1)) / (x2 - x1)
                ) + y1;
    }
}