package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class botCatapult {
    final double CATAPULT_UP_POWER = -1.0;
    final double CATAPULT_DOWN_POWER = 0.20;
    final double CATAPULT_HOLD_POWER = 0.0;

    private enum CatapultModes {UP, DOWN, HOLD}
    private CatapultModes pivotMode;

    private DcMotorEx catapult = null;

    public botCatapult(HardwareMap hardwareMap) {
        catapult = hardwareMap.get(DcMotorEx.class, "catapult");
        catapult.setDirection(DcMotor.Direction.FORWARD);
        catapult.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult.setTargetPosition(0);
        catapult.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public class Down implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                catapult.setPower(CATAPULT_DOWN_POWER);
                initialized = true;
            }

            catapult.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            catapult.setTargetPosition(350);
            catapult.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double posLeftSlide = leftSlide.getCurrentPosition();
            double posRightSlide = rightSlide.getCurrentPosition();
            packet.put("posLeftSlide", posLeftSlide);
            packet.put("posRightSlide", posRightSlide);
            if (posRightSlide < SLIDE_HIGH) {
                leftSlide.setTargetPosition(SLIDE_HIGH);
                rightSlide.setTargetPosition(SLIDE_HIGH);
                return true;
            } else {
                return false;
            }
        }
    }

    public Action Down() {
        return new DownAndHold();
    }}
