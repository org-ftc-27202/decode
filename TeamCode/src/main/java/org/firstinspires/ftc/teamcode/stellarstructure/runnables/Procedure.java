package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Procedure extends Runnable {
    private final Runnable[] runnables;
    private int currentRunnableIndex = 0;
    private final String nameId;
    private boolean hasScheduledFirst = false;
    private boolean shouldStop = false;


    public Procedure(@NonNull String nameId, @NonNull Runnable... runnables) {
        if (runnables.length == 0) {
            throw new IllegalArgumentException("No directives provided");
        }

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Procedure nameId cannot be empty");
        }

        this.nameId = nameId;
        this.runnables = runnables;

        Set<Subsystem> subsystems = new HashSet<>();

        for (Runnable runnable : runnables) {
            subsystems.addAll(Arrays.asList(runnable.getRequiredSubsystems()));
            runnable.setRequiredSubsystems();
        }

        setRequiredSubsystems(subsystems.toArray(new Subsystem[0]));

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
        if (isFinished()) return;

        if (!hasScheduledFirst) {
            runnables[0].schedule();
            hasScheduledFirst = true;
            return;
        }

        if (runnables[currentRunnableIndex].hasBeenInterrupted()) {
            shouldStop = true;
            return;
        }

        // if current directive finished
        if (runnables[currentRunnableIndex].getHasFinished()) {
            // increase directive index
            currentRunnableIndex++;

            if (currentRunnableIndex >= runnables.length) {
                return;
            }

            // schedule next directive
            runnables[currentRunnableIndex].schedule();
        }
    }

    @Override
    public final void stop(boolean interrupted) {
        if (interrupted && currentRunnableIndex >= 0 && currentRunnableIndex < runnables.length) {
            runnables[currentRunnableIndex].stop(true);
        }
    }

    @Override
    public final boolean isFinished() {
        return currentRunnableIndex >= runnables.length || shouldStop;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Procedure: ").append(nameId).append("\n");

        for (int i = 0; i < runnables.length; i++) {
            stringBuilder.append("    ")
                    .append(runnables[i].getClass().getSimpleName())
                    .append(i == currentRunnableIndex ? " <--" : "")
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}

/*package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Procedure extends Runnable {
    private final Runnable[] runnables;
    private final String nameId;
    private int currentRunnableIndex = 0;
    private boolean currentRunnableStarted = false;
    private boolean hasScheduledThisUpdate = false;
    private boolean shouldStop = false;


    public Procedure(@NonNull String nameId, @NonNull Runnable... runnables) {
        if (runnables.length == 0) {
            throw new IllegalArgumentException("No directives provided");
        }

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Procedure nameId cannot be empty");
        }

        this.nameId = nameId;
        this.runnables = runnables;

        Set<Subsystem> subsystems = new HashSet<>();

        for (Runnable runnable : runnables) {
            subsystems.addAll(Arrays.asList(runnable.getRequiredSubsystems()));
            runnable.setRequiredSubsystems();
        }

        setRequiredSubsystems(subsystems.toArray(new Subsystem[0]));

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
        if (isFinished()) return;

        hasScheduledThisUpdate = false;

        Runnable currentRunnable = runnables[currentRunnableIndex];

        if (currentRunnable.hasBeenInterrupted()) {
            throw new RuntimeException(String.format("Runnable %s has been interrupted inside procedure %s somehow", currentRunnable, nameId));
        }

        // if the current runnable hasn't started then
        if (!currentRunnableStarted) {
            // if the current runnable is still waiting for starting conditions, then return
            if (!currentRunnable.getWaitForStartingConditions()) return;

            currentRunnable.startInScheduler();
            hasScheduledThisUpdate = true;
            currentRunnableStarted = true;
        }

        if (currentRunnable.getHasFinished()) {
            currentRunnable.stopInScheduler();
            currentRunnableIndex++;
            currentRunnable = runnables[currentRunnableIndex];

            if (!currentRunnable.getWaitForStartingConditions()) return;

            currentRunnable.startInScheduler();
            //hasScheduledThisUpdate = true;
            currentRunnableStarted = true;
            return;
        }

        if (!hasScheduledThisUpdate) {
            currentRunnable.update();
        }
    }

    @Override
    public final void stop(boolean interrupted) {
        if (interrupted && currentRunnableIndex >= 0 && currentRunnableIndex < runnables.length) {
            runnables[currentRunnableIndex].stop(true);
        }
    }

    @Override
    public final boolean isFinished() {
        return currentRunnableIndex >= runnables.length || shouldStop;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Procedure: ").append(nameId).append("\n");

        for (int i = 0; i < runnables.length; i++) {
            stringBuilder.append("    ")
                    .append(runnables[i].getClass().getSimpleName())
                    .append((i == currentRunnableIndex ? " <-- " : ""))
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}*/