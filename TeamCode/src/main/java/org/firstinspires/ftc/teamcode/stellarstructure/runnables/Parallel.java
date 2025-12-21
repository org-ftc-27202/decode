package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a list of {@link Runnable} tasks to be executed in parallel.
 * Finishes when all {@link Runnable} tasks are complete or one is interrupted/doesn't start.
 *
 * <p>Example:</p>
 *
 * <pre>
 *     {@code
 *     new Parallel(
 *         "IntakeWithTimer",
 *         new Sleep(10),
 *         new IntakeAt(2)
 *     ).schedule();
 *     }
 * </pre>
 */

public class Parallel extends Runnable {
    private final Runnable[] runnables;
    private final String nameId;
    private boolean hasScheduledFirst = false;
    private boolean shouldStop = false;

    /**
     * Constructs a new parallel runnable.
     *
     * @param nameId The name of the parallel command. Must be unique.
     * @param runnables The {@link Runnable} tasks to be executed in parallel.
     */
    public Parallel(@NonNull String nameId, @NonNull Runnable... runnables) {
        if (runnables.length == 0) {
            throw new IllegalArgumentException("No directives provided");
        }

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Parallel nameId cannot be empty");
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // check if the other object is an instance of Parallel
        if (!(o instanceof Parallel)) return false;

        // cast to Parallel.
        Parallel parallel = (Parallel) o;

        // two parallels are equal if they are same class and same nameId
        return getClass() == parallel.getClass() && Objects.equals(nameId, parallel.nameId);
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

        // todo:
        if (!hasScheduledFirst) {
            for (Runnable runnable : runnables) {
                runnable.schedule();
            }

            hasScheduledFirst = true;
            return;
        }

        for (Runnable runnable : runnables) {
			if (runnable.hasBeenInterrupted()) {
				shouldStop = true;
				return;
			}
        }
    }

    @Override
    public final void stop(boolean interrupted) {
        for (Runnable runnable : runnables) {
            if (!runnable.getHasFinished()) {
                runnable.stop(interrupted);
            }
        }
    }

    @Override
    public final boolean isFinished() {
        if (shouldStop) {
            return true;
        }

        for (Runnable runnable : runnables) {
            if (!runnable.getHasFinished()) {
                return false;
            }
        }

        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Parallel: " + nameId;
    }
}
