package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.StellarBot;
import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.Trigger;

import java.util.ArrayList;
import java.util.List;

public abstract class Runnable {
    public enum Status {
        WAITING("WAIT", false),
        STARTED("STRT", false),
        BLOCKED("BLCK", false),
        PENDING("PEND", false),
        ACTIVE("ACTV", false),
        INTERRUPTED("INTR", true),
        DONE("DONE", true);

        private final String displayName;
        private final boolean isFinished;

        Status(String displayName, boolean isFinished) {
            this.displayName = displayName;
            this.isFinished = isFinished;
        }

        public boolean isFinished() {
            return isFinished;
        }

        @NonNull
        @Override
        public String toString() {
            return this.displayName;
        }
    }

    private Status status = Status.WAITING;
    private boolean isOwned = false;

    // no required subsystems by default
    private Subsystem[] requiredSubsystems = {};
    private Condition[] startingConditions = {};

    //todo: only for default directives
    private final List<Trigger> ownedTriggers = new ArrayList<>();

    // interruptible by default
    private boolean interruptible = true;

    // waits for starting conditions by default
    private boolean waitForStartingConditions = true;









    private boolean hadToInterruptToStart = false;



    private boolean hasBeenInterrupted = false;
    private boolean hasFinished = false;


    protected abstract void onStart(boolean hadToInterruptToStart);

    protected abstract void onUpdate();

    protected abstract void onStop(boolean interrupted);

    public final void setHadToInterruptToStart(boolean hadToInterruptToStart) {
        this.hadToInterruptToStart = hadToInterruptToStart;
    }

    public final void start() {
        this.hasFinished = false;
        this.status = Status.STARTED;

        this.onStart(hadToInterruptToStart);
    }

    public final void update() {
        this.status = Status.ACTIVE;
        this.onUpdate();
    }

    public final void stop() {
        this.hasFinished = true;

        boolean runnableFinished = isFinished();
        this.status = runnableFinished ? Status.DONE : Status.INTERRUPTED;
        this.setHasBeenInterrupted(!runnableFinished);
        this.onStop(!runnableFinished);
    }

    public final Status getStatus() {
        return status;
    }

    public final void setStatus(Status status) {
        this.status = status;
    }

    public final boolean isOwned() {
        return isOwned;
    }

    public final void setOwned() {
        this.isOwned = true;
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

    public final boolean isInterruptible() {
        return interruptible;
    }

    public final void updateIsFinished() {
        if (!hasFinished) {
            hasFinished = isFinished();
        }
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
