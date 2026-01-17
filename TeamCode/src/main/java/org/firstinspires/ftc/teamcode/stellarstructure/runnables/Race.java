package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Race extends CompositeRunnable {
    private final String nameId;
    private boolean hasScheduledFirst = false;
    private boolean shouldStop = false;

    public Race(@NonNull String nameId, @NonNull Runnable... runnables) {
        super(runnables);

        if (nameId.isEmpty()) {
            throw new IllegalArgumentException("Race nameId cannot be empty");
        }

        this.nameId = nameId;

        setInterruptible(false);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // check if the other object is an instance of Race
        if (!(o instanceof Race)) return false;

        // cast to Race.
        Race race = (Race) o;

        // two races are equal if they are same class and same nameId
        return getClass() == race.getClass() && Objects.equals(nameId, race.nameId);
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
            if (runnable.getHasFinished()) {
                for (Runnable runnableToStop : runnables) {
                    if (!runnableToStop.getHasFinished()) {
                        runnableToStop.onStop(true);
                    }
                }
                return true;
            }
        }

        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Race: " + nameId;
    }
}
