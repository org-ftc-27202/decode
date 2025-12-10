package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class botIntake {
    final double INTAKE_INWARDS_POWER = 1.0;
    final double INTAKE_OUTWARDS_POWER = -0.5;
    final double INTAKE_OFF_POWER = 0.0;
    private final DcMotorEx intake;
    public enum intakeModes {INWARDS, OUTWARDS, STOP};
    public intakeModes intakeMode = intakeModes.STOP;

    public botIntake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorImplEx.class, "intake");

        intake.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(0);
    }

    public class RotateInwards implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intakeMode = intakeModes.INWARDS;
                intake.setPower(INTAKE_INWARDS_POWER);
                initialized = true;
            }
            return false;
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
                intakeMode = intakeModes.OUTWARDS;
                intake.setPower(INTAKE_OUTWARDS_POWER);
                initialized = true;
            }
            return false;
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
                intakeMode = intakeModes.STOP;
                intake.setPower(INTAKE_OFF_POWER);
                initialized = true;
            }
            return false;
        }
    }

    public Action Stop() {
        return new Stop();
    }
}
