package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Procedure extends Runnable {
    private final Runnable[] runnables;
    private int currentDirectiveIndex = 0;
    private String nameId;
    private boolean hasScheduledFirst = false;

    //todo: .schedule(), .getHasFinished() not proper

    public Procedure(@NonNull String nameId, @NonNull Runnable... runnables) {
        if (runnables.length == 0) {
            throw new IllegalArgumentException("No directives provided");
        }

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Procedure nameId cannot be empty");
        }

        this.nameId = nameId;
        this.runnables = runnables;

        setRequiredSubsystems();
        setInterruptible(false);
    }

    public final String getNameId() {
        return nameId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // check if the other object is an instance of Procedure
        if (!(o instanceof Procedure)) return false;

        // cast to Procedure.
        Procedure procedure = (Procedure) o;

        // two procedures are equal if they are same class and same nameId
        return getClass() == procedure.getClass() && Objects.equals(nameId, procedure.nameId);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), nameId);
    }

    @Override
    public final void start(boolean hadToInterruptToStart) {}

    @Override
    public final void update() {
        if (isFinished()) {
            return;
        }

        if (!hasScheduledFirst) {
            runnables[0].schedule();
            hasScheduledFirst = true;
            return;
        }

        // if current directive finished
        if (runnables[currentDirectiveIndex].getHasFinished()) {
            // increase directive index
            currentDirectiveIndex++;

            if (currentDirectiveIndex >= runnables.length) {
                return;
            }

            // schedule next directive
            runnables[currentDirectiveIndex].schedule();
        }
    }

    @Override
    public final void stop(boolean interrupted) {
        if (interrupted && currentDirectiveIndex >= 0 && currentDirectiveIndex < runnables.length) {
            runnables[currentDirectiveIndex].stop(true);
        }
    }

    @Override
    public final boolean isFinished() {
        return currentDirectiveIndex >= runnables.length;
    }

    @NonNull
    @Override
    public String toString() {
        return "Procedure: " + nameId; // Custom toString for better telemetry.
    }
}
