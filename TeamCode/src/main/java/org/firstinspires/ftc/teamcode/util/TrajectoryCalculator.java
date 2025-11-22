package org.firstinspires.ftc.teamcode.util;

public class TrajectoryCalculator {
    private static double RADIUS = 0.12;
    private static double MASS = 0.05;
    private static double DRAG_CONSTANT = 0.4;
    private static double LIFT_CONSTANT = 0.195;
    private static double AREA = Math.PI * RADIUS * RADIUS;
    private static double UNKNOWN_S = 0.8 + Math.PI / 100.0;
    private static double GRAVITY = 9.8;
    private static double AIR_DENSITY = 1;



    //Initial rotation of the robot in relation to the goal
    private double initialRadians = 1;
    //Total distance to the goal
    private double totalDistance = 3.6576;
    //Height of the goal
    private double height = .9906;
    //Where slip constant is (Velocity) = (slipConstant)(angularVelocity)(RADIUS)
    private double slipConstant = 20;
    //The velocity of the robot while launching in relation to the field side opposite to the obelisk vertically
    private double inertialTotalX = 1;
    //The velocity of the robot while launching in relation to the field side opposite to the obelisk horizontally
    private double inertialTotalZ = 2.7;
    //The velocity of the robot while launching in relation to the goal, vertically
    private double inertialPOVX = inertialTotalX * Math.cos(initialRadians) + inertialTotalZ * Math.sin(initialRadians);
    //The velocity of the robot while launching in relation to the goal, horizontally
    private double inertialPOVZ = inertialTotalX * Math.sin(initialRadians) + inertialTotalZ * Math.cos(initialRadians);
    //Literally just (DRAG_CONSTANT)(AREA)(AIR_DENSITY)
    private double cdap = DRAG_CONSTANT * AREA * AIR_DENSITY;

    /* ------
     * Part 1
     * ------
     *
     * Z position to time
     */

    private double zFinal = totalDistance * Math.sin(initialRadians);
    private double zNaught = (2 * Math.log(Math.abs(cdap * (2 / (cdap * inertialPOVZ))))) / (cdap);
    private double timeFinal = ((Math.pow(Math.E, ((cdap * (zFinal + zNaught)) / 2))) / cdap) - (2 / (cdap * inertialPOVZ));

    /* ------
     * Part 2
     * ------
     *
     * time to X velocity
     */

    private double xFinal = totalDistance * Math.cos(initialRadians);
}
