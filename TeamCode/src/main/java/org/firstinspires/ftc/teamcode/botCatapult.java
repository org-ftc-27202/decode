package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class botCatapult {
    final double CATAPULT_LAUNCH_POWER = 0.8;  // for 3 springs -> near launch zone
//    final double CATAPULT_LAUNCH_POWER = 1.0;  // for 4 springs -> far launch zone
    final int HALF_ROTATION = 700;  // smaller slip gear
//    final int HALF_ROTATION = 750; // larger slip gear
    private final DcMotorEx catapult;
    public double pos = 0.0;


    public botCatapult(HardwareMap hardwareMap, String inCatapultDeviceName){
        catapult = hardwareMap.get(DcMotorEx.class, inCatapultDeviceName);
        catapult.setDirection(DcMotorEx.Direction.REVERSE);
        catapult.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult.setTargetPosition(0);
        catapult.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public class Launch implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                catapult.setPower(CATAPULT_LAUNCH_POWER);
                initialized = true;
            }

            pos = catapult.getCurrentPosition();
            if (pos < HALF_ROTATION) {
                catapult.setTargetPosition(HALF_ROTATION);
                return true;
            } else {
                catapult.setPower(0);
                catapult.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult.setTargetPosition(0);
                catapult.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                return false;
            }
        }
    }
    public Action Launch() {
        return new Launch();
    }
}
