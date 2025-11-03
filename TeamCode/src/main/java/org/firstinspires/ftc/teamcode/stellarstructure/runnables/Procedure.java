package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

public class Procedure extends Runnable {
    private final Runnable[] runnables;
    private int currentDirectiveIndex = 0;

    public Procedure(@NonNull Runnable... runnables) {
        if (runnables.length == 0) {
            throw new IllegalArgumentException("No directives provided");
        }

        this.runnables = runnables;

        setRequiredSubsystems();
        setInterruptible(false);
    }

    @Override
    public final void start(boolean hadToInterruptToStart) {
        runnables[0].schedule();
    }

    @Override
    public final void update() {
        // if the current directive is invalid or the whole procedure is done, do nothing
        if (currentDirectiveIndex < 0 || currentDirectiveIndex >= runnables.length) {
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
}
