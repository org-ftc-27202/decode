package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class botCatapult {
    final double CATAPULT_PRELAUNCH_POWER = 0.5;
    final double CATAPULT_LAUNCH_POWER = 1.0;
    private final DcMotorEx catapult;
//    private DigitalChannel switchUp;
//    private boolean flagLaunching = false;


    public botCatapult(HardwareMap hardwareMap, String inCatapultDeviceName, String inSwitchUpDeviceName){
        catapult = hardwareMap.get(DcMotorEx.class, inCatapultDeviceName);
        catapult.setDirection(DcMotorEx.Direction.REVERSE);
        catapult.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        switchUp = hardwareMap.get(DigitalChannel.class, inSwitchUpDeviceName);
//        switchUp.setMode(DigitalChannel.Mode.INPUT);
    }

    public class PreLaunch implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                catapult.setPower(CATAPULT_PRELAUNCH_POWER);
//                flagLaunching = true;
                initialized = true;
            }
            return false;
        }
    }
    public Action PreLaunch() {
        return new PreLaunch();
    }

    public class Launch implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                catapult.setPower(CATAPULT_LAUNCH_POWER);
//                flagLaunching = true;
                initialized = true;
            }
            return false;
        }
    }
    public Action Launch() {
        return new Launch();
    }
    public class StopCatapult implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                catapult.setPower(0);
                initialized = true;
            }
            return false;
        }
    }
    public Action StopCatapult() {return new StopCatapult();}

    public double getPower() {
        return catapult.getPower();
    }
}
