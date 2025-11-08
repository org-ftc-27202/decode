package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

public class Parallel extends Runnable {
    private final Runnable[] runnables;

    public Parallel(@NonNull Runnable... runnables) {
        this.runnables = runnables;

        setInterruptible(true);
        setRequiredSubsystems();
    }

    @Override
    protected void start(boolean hadToInterruptToStart) {
        for (Runnable runnable : runnables) {
            runnable.schedule();
        }
    }

    @Override
    public void update() {}

    @Override
    protected void stop(boolean interrupted) {
        for (Runnable runnable : runnables) {
            runnable.stop(true);
        }
    }

    @Override
    protected boolean isFinished() {
        for (Runnable runnable : runnables) {
            if (!runnable.getHasFinished()) {
                return false;
            }
        }

        return true;
    }
}
