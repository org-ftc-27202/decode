package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.Trigger;

import java.util.ArrayList;
import java.util.List;

public abstract class Runnable {
    // no required subsystems by default
    private Subsystem[] requiredSubsystems = {};
    private Condition[] startingConditions = {};
    private final List<Trigger> ownedTriggers = new ArrayList<>();

    private boolean hadToInterruptToStart = false;

    // interruptible by default
    private boolean interruptible = true;

    private boolean hasBeenInterrupted = false;
    private boolean hasFinished = false;

    private boolean waitForStartingConditions = true;

    protected abstract void start(boolean hadToInterruptToStart);

    public abstract void update();

    protected abstract void stop(boolean interrupted);

    public final void setHadToInterruptToStart(boolean hadToInterruptToStart) {
        this.hadToInterruptToStart = hadToInterruptToStart;
    }

    public final void startInScheduler() {
        this.hasFinished = false;

        start(hadToInterruptToStart);
    }

    public final void stopInScheduler() {
        this.hasFinished = true;

        setHasBeenInterrupted(!isFinished());
        stop(!isFinished());
    }

    protected abstract boolean isFinished();

    public final void addTrigger(Trigger trigger) {
        ownedTriggers.add(trigger);
    }

    public final void removeTrigger(Trigger trigger) {
        ownedTriggers.remove(trigger);
    }

    public final List<Trigger> getOwnedTriggers() {
        return this.ownedTriggers;
    }

    public final Runnable setRequiredSubsystems(@NonNull Subsystem... subsystems) {
        requiredSubsystems = subsystems;
        return this;
    }

    public final Subsystem[] getRequiredSubsystems() {
        return requiredSubsystems;
    }

    public final Runnable setInterruptible(boolean interruptible) {
        this.interruptible = interruptible;
        return this;
    }

    public final boolean getWaitForStartingConditions() {
        return waitForStartingConditions;
    }

    public final Runnable setWaitForStartingConditions(boolean waitForStartingConditions) {
        this.waitForStartingConditions = waitForStartingConditions;
        return this;
    }

    public final Condition[] getStartingConditions() {
        return startingConditions;
    }

    public final Runnable setStartingConditions(Condition... startingConditions) {
        this.startingConditions = startingConditions;
        return this;
    }

    public final boolean getInterruptible() {
        return interruptible;
    }

    public final boolean getFinished() {
        if (!hasFinished) {
            hasFinished = isFinished();
        }

        return hasFinished;
    }

    public final boolean getHasFinished() {
        return hasFinished;
    }

    public final void schedule() {
        StellarBot.getInstance().getScheduler().schedule(this);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

	public boolean hasBeenInterrupted() {
		return hasBeenInterrupted;
	}

	public void setHasBeenInterrupted(boolean hasBeenInterrupted) {
		this.hasBeenInterrupted = hasBeenInterrupted;
	}
}
