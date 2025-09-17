package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DecodeDataTypes {
    public enum ArtifactColor {
        GREEN,
        PURPLE
    }

    public static class ArtifactSequence {
        private ArtifactColor[] sequence = new ArtifactColor[3];

        public ArtifactSequence() {}
        public ArtifactSequence(ArtifactColor[] sequence) {
            this.sequence = sequence;
        }

        public static ArtifactSequence of(ArtifactColor... colors) {
            return new ArtifactSequence(colors);
        }

        public ArtifactColor[] getSequence() {
            return sequence;
        }

        public void setSequence(@NonNull String[] sequence) {
            if (sequence.length != 3) {
                throw new IllegalArgumentException("Sequence must have 3 elements");
            }

            for (int i = 0; i < 3; i++) {
                switch (sequence[i]) {
                    case "GREEN":
                        this.sequence[i] = ArtifactColor.GREEN;
                        break;
                    case "PURPLE":
                        this.sequence[i] = ArtifactColor.PURPLE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid artifact color: " + sequence[i]);
                }
            }
        }

        public String[] toStringArray() {
            String[] stringArray = new String[3];

            for (int i = 0; i < 3; i++) {
                switch (sequence[i]) {
                    case GREEN:
                        stringArray[i] = "GREEN";
                        break;
                    case PURPLE:
                        stringArray[i] = "PURPLE";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid artifact color at index " + i + ": " + sequence[i]);
                }
            }
            return stringArray;
        }
    }

    public static class Coords {
        private double x;
        private double y;
        private double z;
        private double pitch;
        private double roll;
        private double yaw;

        public Coords() {}

        public Coords(double x, double y, double z, double pitch, double roll, double yaw) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.roll = roll;
            this.yaw = yaw;
        }

        public void setCoords(double x, double y, double z, double pitch, double roll, double yaw) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.roll = roll;
            this.yaw = yaw;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public double getPitch() {
            return pitch;
        }

        public double getRoll() {
            return roll;
        }

        public double getYaw() {
            return yaw;
        }
    }

    public static class DateMs {
        private long date_ms;

        public DateMs() {}

        public DateMs(long init_ms) {
            date_ms = init_ms;
        }

        public long getDateMs() {
            return date_ms;
        }

        public void setDateMs(long new_ms) {
            date_ms = new_ms;
        }

        public double getMinutesTimeSince() {
            long differenceMillis = System.currentTimeMillis() - date_ms;
            return (double) differenceMillis / 1000 / 60;
        }
    }

    public static class MotorPositions {
        private Map<String, Double> motorPositions = new LinkedHashMap<>();

        private MotorPositions(Builder builder) {
            this.motorPositions = builder.motorPositions;
        }

        public static class Builder {
            private final Map<String, Double> motorPositions = new LinkedHashMap<>();

            public Builder() {

            }

            public Builder addMotor(String motorName, double position) {
                if (motorName == null) {
                    throw new IllegalArgumentException("Motor name cannot be null in Builder.");
                }

                motorPositions.put(motorName, position);

                return this;
            }

            public MotorPositions build() {
                return new MotorPositions(this);
            }
        }

        public void addMotorPosition(String motorName, double position) {
            if (motorName == null) {
                throw new IllegalArgumentException("Motor name cannot be null.");
            }

            motorPositions.put(motorName, position);
        }

        public double getMotorPosition(@NonNull String motorName) {
            if (motorPositions.containsKey(motorName)) {
                return motorPositions.get(motorName);
            } else {
                throw new IllegalArgumentException("Motor '" + motorName + "' not found");
            }
        }

        public List<Map.Entry<String, Double>> getEntries() {
            if (motorPositions instanceof LinkedHashMap) {
                return new ArrayList<>(motorPositions.entrySet());
            } else {
                throw new IllegalStateException("MotorPositions needs to be a LinkedHashMap");
            }
        }
    }
}
