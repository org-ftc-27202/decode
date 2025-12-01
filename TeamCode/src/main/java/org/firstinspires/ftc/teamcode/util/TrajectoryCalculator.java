package org.firstinspires.ftc.teamcode.util;

public class TrajectoryCalculator {
    private final static double RADIUS = 0.12;
    private final static double MASS = 0.05;
    private final static double DRAG_CONSTANT = 0.4;
    private final static double AREA = Math.PI * RADIUS * RADIUS;
    private final static double AIR_DENSITY = 1.0;
    private final static double CDAP = DRAG_CONSTANT * AREA * AIR_DENSITY / MASS;


    // 1.0, 3.6576, 0.9906, 20.0, 1.0, 2.7
    public double[] calculateTrajectory(double initialRadians, double totalDistance, double height, double slipConstant, double inertialTotalX, double inertialTotalZ) {
        // desired air time
        double timeFinal = 0.7;

        // the velocity of the robot while launching in relation to the goal, vertically
        double inertialPOVX = inertialTotalX * Math.cos(initialRadians) + inertialTotalZ * Math.sin(initialRadians);

        // the velocity of the robot while launching in relation to the goal, horizontally
        double inertialPOVZ = inertialTotalX * Math.sin(initialRadians) + inertialTotalZ * Math.cos(initialRadians);

        /*
         *  Step 1
         *  X distance to X velocity
         */

        double xFinal = totalDistance * Math.cos(initialRadians);
        double zFinal = totalDistance * Math.sin(initialRadians);
        double initialXVelocity = ((2 * MASS * (Math.pow(Math.E, ((CDAP * xFinal) / (2 * MASS)) - 1))) / (CDAP * timeFinal)) - inertialPOVX;

        /*
         *  Step 2
         *  Y velocity estimation
         */

        double initialYVelocity = 5.50715994552;

        /*
         *  Step 3
         *  Calculating z diff
         */

        double zDiff = Math.abs(((2 * MASS * Math.log(((CDAP * inertialPOVZ * timeFinal) / (2 * MASS)) + 1)) / (CDAP)) - zFinal);

        /*
         *  Step 4
         *  final
         */

        double launchRadians = Math.atan(initialYVelocity / initialXVelocity);
        double launchVelocity = Math.sqrt((initialYVelocity * initialYVelocity) + (initialXVelocity * initialXVelocity));

        return new double[] {
                launchRadians, launchVelocity, zDiff
        };
    }
}
