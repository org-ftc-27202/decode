/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * This OpMode executes a Tank Drive control TeleOp a direct drive robot
 * The code is structured as an Iterative OpMode
 *
 * In this mode, the left and right joysticks control the left and right motors respectively.
 * Pushing a joystick forward will make the attached motor drive forward.
 * It raises and lowers the claw using the Gamepad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Robot: Arcade Drive", group="Robot")
//@Disabled
public class ArcadeDrive extends OpMode{

    /* Declare OpMode members. */
    public DcMotor  leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
    public DcMotor  leftArm     = null;
    private DcMotor intake = null;

    // motor power 1 = 100% and 0.5 = 50%
    // negative values = reverse ex: -0.5 = reverse 50%
    private double INTAKE_IN_POWER = 1.0;
    private double INTAKE_OUT_POWER = -0.9;
    private double INTAKE_OFF_POWER = 0.0;
    private double intakePower = INTAKE_OFF_POWER;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Define and Initialize Motors
//        leftFrontMotor  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBackMotor  = hardwareMap.get(DcMotor.class, "leftRear");
//        rightFrontMotor  = hardwareMap.get(DcMotor.class, "rightFront");
        rightBackMotor  = hardwareMap.get(DcMotor.class, "rightRear");
//        leftArm    = hardwareMap.get(DcMotor.class, "left_arm");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left and right sticks forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
//        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.FORWARD);
//        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
//        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intake = hardwareMap.get(DcMotor.class, "intake");

        // set direction of subsystem motors
        intake.setDirection(DcMotor.Direction.REVERSE);

        // set initial subsystem behavior
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press START.");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
    }

    /*
     * Code to drive
     */
    public void drive(double throttle, double spin) {
        double leftPower = throttle + spin;
        double rightPower = throttle - spin;
        double largest = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (largest > 1.0) {
            leftPower /= largest;
            rightPower /= largest;
        }

//        leftFrontMotor.setPower(leftPower);
        leftBackMotor.setPower(leftPower);
//        rightFrontMotor.setPower(rightPower);
        rightBackMotor.setPower(rightPower);
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        double throttle;
        double spin;

        boolean intakeInButton = gamepad1.right_trigger > 0.2;
        boolean intakeOutButton = gamepad1.right_bumper;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forward, so negate it)
        throttle = -gamepad1.left_stick_y;
        spin = gamepad1.left_stick_x + gamepad1.right_stick_x;

        drive(throttle, spin);

        // This conditional reduces ambiguity when multiple buttons are pressed.
        if (intakeInButton && intakeOutButton) {
            intakeInButton = false;
        }

        // INTAKE CODE
        if (intakeInButton) {
            intakePower = INTAKE_IN_POWER;
        } else if (intakeOutButton) {
            intakePower = INTAKE_OUT_POWER;
        } else {
            intakePower = INTAKE_OFF_POWER;
        }

        intake.setPower(intakePower);

//        // Send telemetry message to signify robot running;
//        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("throttle",  "%.2f", throttle);
        telemetry.addData("spin", "%.2f", spin);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
