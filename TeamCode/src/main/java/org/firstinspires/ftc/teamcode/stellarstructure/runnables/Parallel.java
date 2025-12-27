package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Represents a list of {@link Runnable} tasks to be executed in parallel.
 * Finishes when all {@link Runnable} tasks are complete or one is interrupted/doesn't onStart.
 *
 * <p>Example:</p>
 *
 * <pre>
 *     {@code
 *     new Parallel(
 *         "IntakeWhileWait10",
 *         new Sleep(10),
 *         new IntakeAt(2)
 *     ).schedule();
 *     }
 * </pre>
 */

public class Parallel extends CompositeRunnable {
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
        super(runnables);

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Parallel nameId cannot be empty");
        }

        this.nameId = nameId;

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
    protected final void onStart(boolean hadToInterruptToStart) {}

    @Override
    protected final void onUpdate() {
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
    protected final void onStop(boolean interrupted) {
        if (interrupted) {
            for (Runnable runnable : runnables) {
                if (!runnable.getHasFinished()) {
                    runnable.onStop(true);
                }
            }
        }
    }

    @Override
    protected final boolean isFinished() {
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
