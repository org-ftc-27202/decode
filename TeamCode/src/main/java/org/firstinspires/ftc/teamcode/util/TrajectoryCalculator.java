package org.firstinspires.ftc.teamcode.util;

public class TrajectoryCalculator {
    private final static double RADIUS = 0.12;
    private final static double MASS = 0.05;
    private final static double DRAG_CONSTANT = 0.4;
    private final static double LIFT_CONSTANT = 0.195;
    private final static double AREA = Math.PI * RADIUS * RADIUS;
    private final static double UNKNOWN_S = 0.8 + (Math.PI / 100.0);
    private final static double GRAVITY = 9.81;
    private final static double AIR_DENSITY = 1.0;
    private final static double CDAP = DRAG_CONSTANT * AREA * AIR_DENSITY / MASS;
    private final static double A_APPROX = -CDAP / 2;
    private final static double C_APPROX = -GRAVITY;


    // 1.0, 3.6576, 0.9906, 20.0, 1.0, 2.7
    public double[] calculateTrajectory(double initialRadians, double totalDistance, double height, double slipConstant, double inertialTotalX, double inertialTotalZ) {
        // the velocity of the robot while launching in relation to the goal, vertically
        double inertialPOVX = inertialTotalX * Math.cos(initialRadians) + inertialTotalZ * Math.sin(initialRadians);

        // the velocity of the robot while launching in relation to the goal, horizontally
        double inertialPOVZ = inertialTotalX * Math.sin(initialRadians) + inertialTotalZ * Math.cos(initialRadians);


        /* ------
         * Part 1
         * ------
         *
         * Z position to time
         */
        double xFinal = totalDistance * Math.cos(initialRadians);
        double zFinal = totalDistance * Math.sin(initialRadians);
        double zNaught = (2 * Math.log(Math.abs(CDAP * (2 / (CDAP * inertialPOVZ))))) / (CDAP);
        double timeFinal = ((Math.pow(Math.E, ((CDAP * (zFinal + zNaught)) / 2))) / CDAP) - (2 / (CDAP * inertialPOVZ));

        /* ------
         * Part 2
         * ------
         *
         * time to X velocity
         */
        double estimateNaughtvx = (xFinal / timeFinal) - inertialPOVX;
        double estimate1vx = estimateNaughtvx - ((xApproxvx(estimateNaughtvx, timeFinal, inertialPOVX) - xFinal) / ((xApproxvx(estimateNaughtvx + 0.0005, timeFinal, inertialPOVX) - xApproxvx(estimateNaughtvx - 0.0005, timeFinal, inertialPOVX)) / 0.001));
        double estimate2vx = estimate1vx - ((xApproxvx(estimate1vx, timeFinal, inertialPOVX) - xFinal) / ((xApproxvx(estimate1vx + 0.0005, timeFinal, inertialPOVX) - xApproxvx(estimate1vx - 0.0005, timeFinal, inertialPOVX)) / 0.001));
        double estimateFinalvx = estimate2vx - ((xApproxvx(estimate2vx, timeFinal, inertialPOVX) - xFinal) / ((xApproxvx(estimate2vx + 0.0005, timeFinal, inertialPOVX) - xApproxvx(estimate2vx - 0.0005, timeFinal, inertialPOVX)) / 0.001));

        /* ------
         * Part 3
         * ------
         *
         * X velocity to Y velocity
         */
        double estimateNaughtvy = (height / timeFinal) + ((GRAVITY * timeFinal) / 2);
        double estimate1vy = estimateNaughtvy - ((yApproxvx(estimateNaughtvy, timeFinal, estimateFinalvx, slipConstant) - height) / ((yApproxvx(estimateNaughtvy + 0.0005, timeFinal, estimateFinalvx, slipConstant) - yApproxvx(estimateNaughtvy - 0.0005, timeFinal, estimateFinalvx, slipConstant)) / 0.001));
        double estimate2vy = estimate1vy - ((yApproxvx(estimate1vy, timeFinal, estimateFinalvx, slipConstant) - height) / ((yApproxvx(estimate1vy + 0.0005, timeFinal, estimateFinalvx, slipConstant) - yApproxvx(estimate1vy - 0.0005, timeFinal, estimateFinalvx, slipConstant)) / 0.001));
        double estimateFinalvy = estimate2vy - ((yApproxvx(estimate2vy, timeFinal, estimateFinalvx, slipConstant) - height) / ((yApproxvx(estimate2vy + 0.0005, timeFinal, estimateFinalvx, slipConstant) - yApproxvx(estimate2vy - 0.0005, timeFinal, estimateFinalvx, slipConstant)) / 0.001));

        /* ------
         * Part 4
         * ------
         *
         * Calculate Final
         */
        double launchRadians = Math.atan(estimateFinalvy / estimateFinalvx);
        double launchVelocity = estimateFinalvy / Math.sin(launchRadians);

        return new double[] {
                launchRadians, launchVelocity
        };
    }

    private double xApproxvx(double vApproxvx, double timeFinal, double inertialPOVX) {
        double part1 = (2 * Math.log(Math.abs(CDAP * (timeFinal + (2 / (CDAP * (vApproxvx + inertialPOVX))))))) / CDAP;
        double part2 = (2 * Math.log(Math.abs(CDAP * (2 / (CDAP * (vApproxvx + inertialPOVX)))))) / CDAP;;

        return part1 - part2;
    }

    private double bApprox(double vApproxy, double estimateFinalvx, double slipConstant) {
        return -(LIFT_CONSTANT * AIR_DENSITY * AREA * RADIUS * (Math.sqrt((vApproxy * vApproxy) + (estimateFinalvx * estimateFinalvx))) / (slipConstant * RADIUS)) / (2 * UNKNOWN_S * MASS);
    }

    private double conApprox(double vApproxy, double estimateFinalvx, double slipConstant) {
        double placeholder = Math.sqrt((4 * A_APPROX * C_APPROX) - (Math.pow(bApprox(vApproxy, estimateFinalvx, slipConstant), 2)));

        return (2 * Math.atan((2 * A_APPROX * vApproxy) / placeholder)) / placeholder;
    }

    private double cohApprox(double vApproxy, double timeFinal, double estimateFinalvx, double slipConstant) {
        return Math.cos(((timeFinal + conApprox(vApproxy, estimateFinalvx, slipConstant)) * (Math.sqrt((4 * A_APPROX * C_APPROX) - (Math.pow(bApprox(vApproxy, estimateFinalvx, slipConstant), 2))))) / 2);
    }

    private double cohApproxConstant(double vApproxy, double estimateFinalvx, double slipConstant) {
        return Math.cos((conApprox(vApproxy, estimateFinalvx, slipConstant) * (Math.sqrt((4 * A_APPROX * C_APPROX) - (Math.pow(bApprox(vApproxy, estimateFinalvx, slipConstant), 2))))) / 2);
    }

    private double yApproxvx(double vApproxy, double timeFinal, double estimateFinalvx, double slipConstant) {
        double part1 = ((2 * Math.log(Math.abs(cohApprox(vApproxy, timeFinal, estimateFinalvx, slipConstant)))) + (bApprox(vApproxy, estimateFinalvx, slipConstant) * timeFinal)) / (2 * A_APPROX);
        double part2 = (2 * Math.log(Math.abs(cohApproxConstant(vApproxy, estimateFinalvx, slipConstant)))) / (2 * A_APPROX);

        return -part1 + part2;
    }
}
