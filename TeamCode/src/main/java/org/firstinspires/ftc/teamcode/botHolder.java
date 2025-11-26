package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public final class botHolder {
    final double HOLDER_HOLD = 0.56;
    final double HOLDER_RELEASE = HOLDER_HOLD + 0.15;
    private final ServoImplEx holder;

    public botHolder(HardwareMap hardwareMap, String inDeviceName){
        holder = hardwareMap.get(ServoImplEx.class, inDeviceName);
    }

    public class HoldPosition implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                holder.setPosition(HOLDER_HOLD);
                initialized = true;
            }
            return false;
        }
    }

    public Action HoldPosition() {
        return new HoldPosition();
    }

    public class ReleasePosition implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                holder.setPosition(HOLDER_RELEASE);
                initialized = true;
            }
            return false;
        }
    }

    public Action ReleasePosition() {
        return new ReleasePosition();
    }

    public double getPosition() {
        return holder.getPosition();
    }
}
