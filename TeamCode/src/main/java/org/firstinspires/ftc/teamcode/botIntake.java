package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class botIntake {
    final double INTAKE_INWARDS_POWER = 1.0;
    final double INTAKE_OUTWARDS_POWER = -0.5;
    final double INTAKE_OFF_POWER = 0.0;
    final double intakePower = INTAKE_OFF_POWER;

    private DcMotorEx intake;

    public botIntake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorEx.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public class RotateInwards implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intake.setPower(INTAKE_INWARDS_POWER);
                initialized = true;
            }

            packet.put("intakePower", intake.getPower());
            return true;
        }
    }

    public Action RotateInwards() {
        return new RotateInwards();
    }

    public class RotateOutwards implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intake.setPower(INTAKE_OUTWARDS_POWER);
                initialized = true;
            }

            packet.put("intakePower", intake.getPower());
            return true;
        }
    }

    public Action RotateOutwards() {
        return new RotateOutwards();
    }

    public class Stop implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intake.setPower(INTAKE_OFF_POWER);
                initialized = true;
            }

            packet.put("intakePower", intake.getPower());
            return true;
        }
    }

    public Action Stop() {
        return new Stop();
    }
}
