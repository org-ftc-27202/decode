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
    final double CATAPULT_LAUNCH_POWER = 0.75;  // for 3 springs -> near launch zone
//    final double CATAPULT_LAUNCH_POWER = 0.9;  // for 4 springs -> far launch zone
    private final DcMotorEx catapult;
    public AnalogEncoder encoder;
    private double maxAngle = 0.0;
    private double currentAngle = 0;
    private double toleranceAngle = 2.5;


    public botCatapult(HardwareMap hardwareMap, String inCatapultDeviceName, String inEncoderDeviceName, boolean inEncoderInverted){
        catapult = hardwareMap.get(DcMotorEx.class, inCatapultDeviceName);
        catapult.setDirection(DcMotorEx.Direction.REVERSE);
        catapult.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        catapult.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        encoder = new AnalogEncoder(hardwareMap, inEncoderDeviceName);
        encoder.setInverted(inEncoderInverted);
        encoder.setZeroPosition();
//        switchUp = hardwareMap.get(DigitalChannel.class, inSwitchUpDeviceName);
//        switchUp.setMode(DigitalChannel.Mode.INPUT);
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

    public void CheckToStopCatapult() {
        currentAngle = encoder.getAngle();
        if (currentAngle > maxAngle)
        {
            maxAngle = currentAngle;
        }
        else if (currentAngle < (maxAngle - toleranceAngle))
        {
            catapult.setPower(0);
            maxAngle = 0.0;
        }
    }
}
