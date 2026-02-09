package org.firstinspires.ftc.teamcode.util;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class LaunchInterpolator {
    private LaunchInterpolator() {}

    static TreeMap<Double, LaunchParameters> launchData = new TreeMap<>(
            Map.of(
                    65.0, new LaunchParameters(0.55, 1120*1.5),
                    123.0, new LaunchParameters(0.2,1300*1.5),
                    140.0, new LaunchParameters(0.2, 1460*1.5),
                    172.0, new LaunchParameters(0.2,1640*1.5)
            )
    );

    // LaunchInterpolar.getEstimatedLaunchParameters(0.0);

    public static LaunchParameters getEstimatedLaunchParameters(double realDistance) {
        Double lowerBoundKey = launchData.floorKey(realDistance);
        Double upperBoundKey = launchData.ceilingKey(realDistance);
        if (lowerBoundKey == null) {
            lowerBoundKey = launchData.firstKey();
        }

        // FIX: If we are out of range (too far), clamp to the highest available key
        if (upperBoundKey == null) {
            upperBoundKey = launchData.lastKey();
        }

        double x1 = lowerBoundKey;
        double x2 = upperBoundKey;

        double angle1 = Objects.requireNonNull(launchData.get(x1)).getAngle();
        double angle2 = Objects.requireNonNull(launchData.get(x2)).getAngle();

        double velocity1 = Objects.requireNonNull(launchData.get(x1)).getVelocity();
        double velocity2 = Objects.requireNonNull(launchData.get(x2)).getVelocity();
        double interpolatedAngle = interpolateValues(realDistance, x1, x2, angle1, angle2);
        double interpolatedVelocity = interpolateValues(realDistance, x1, x2, velocity1, velocity2);
        return new LaunchParameters(interpolatedAngle, interpolatedVelocity);


    }
    public static double interpolateValues(double x, double x1, double x2, double y1, double y2){
        if (x1 == x2) {
            return (y1);
        }

        return (((y2 - y1) * (x - x1)) / (x2 - x1)) + y1;
    }
}