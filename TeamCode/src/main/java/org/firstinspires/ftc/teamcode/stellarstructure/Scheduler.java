package org.firstinspires.ftc.teamcode.stellarstructure;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Runnable;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scheduler {
	private static Scheduler activeSchedulerInstance;

	public static void setGlobalInstance(Scheduler scheduler) {
		activeSchedulerInstance = scheduler;
	}

	public static Scheduler getGlobalInstance() {
		if (activeSchedulerInstance == null) {
			throw new IllegalStateException("Scheduler global instance has not been set!");
		}
		return activeSchedulerInstance;
	}

	//todo: reorganize order of methods and variables

	private final List<Subsystem> subsystems = new ArrayList<>();
	private final List<Runnable> pendingRunnables = new ArrayList<>();
	private final List<Runnable> activeRunnables = new ArrayList<>();

	private final List<Runnable> runnablesToAdd = new ArrayList<>();
	private final List<Runnable> runnablesToRemove = new ArrayList<>();

	public final void addSubsystem(Subsystem subsystem) {
		subsystems.add(subsystem);
	}

	public final void removeSubsystem(Subsystem subsystem) {
		subsystems.remove(subsystem);
	}

	private boolean checkStartingConditions(@NonNull Runnable runnable) {
		Condition[] conditions = runnable.getStartingConditions();

		// return false if any starting conditions are not met
		for (Condition condition : conditions) {
			if (!condition.evaluate()) {
				return false;
			}
		}

		// all conditions met
		return true;
	}

	private boolean hasUninterruptibleConflict(Runnable runnableToCheck, List<Runnable> runnables) {
		for (Runnable runningRunnable : runnables) {
			for (Subsystem requiredByNew : runnableToCheck.getRequiredSubsystems()) {
				for (Subsystem requiredByRunning : runningRunnable.getRequiredSubsystems()) {
					if (requiredByNew.equals(requiredByRunning) && !runningRunnable.getInterruptible()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean interruptConflictingRunnables(Runnable runnableToCheck, List<Runnable> runnables) {
		boolean didInterrupt = false;

		for (Runnable runningRunnable : runnables) {
			if (this.runnablesToRemove.contains(runningRunnable)) {
				continue;
			}
			for (Subsystem requiredByNew : runnableToCheck.getRequiredSubsystems()) {
				for (Subsystem requiredByRunning : runningRunnable.getRequiredSubsystems()) {
					if (requiredByNew.equals(requiredByRunning)) {
						this.runnablesToRemove.add(runningRunnable);
						didInterrupt = true;
					}
				}
			}
		}
		return didInterrupt;
	}

	private boolean startRunnable(Runnable runnableToStart) {
		// check for un-interruptable conflicts
		if (
				hasUninterruptibleConflict(runnableToStart, this.activeRunnables) ||
				hasUninterruptibleConflict(runnableToStart, this.runnablesToAdd)
		) {
			// can't schedule
			return false;
		}

		boolean didInterruptActive = interruptConflictingRunnables(runnableToStart, this.activeRunnables);
		boolean didInterruptPending = interruptConflictingRunnables(runnableToStart, this.runnablesToAdd);

		// going to be started
		this.runnablesToAdd.add(runnableToStart);

		runnableToStart.setHadToInterruptToStart(didInterruptActive || didInterruptPending);
		return true;
	}

	public final void stopRunnable(@NonNull Runnable runnableToCancel) {
		pendingRunnables.remove(runnableToCancel);
		runnablesToAdd.remove(runnableToCancel);

		if (activeRunnables.contains(runnableToCancel) && !runnablesToRemove.contains(runnableToCancel)) {
			runnablesToRemove.add(runnableToCancel);
		}
	}

	public final void schedule(@NonNull Runnable runnableToSchedule) {
		// prevent scheduling of the same directive multiple times
		if (
				this.pendingRunnables.contains(runnableToSchedule) ||
				this.runnablesToAdd.contains(runnableToSchedule) ||
				this.activeRunnables.contains(runnableToSchedule)
		) {
			return;
		}

		// check for starting conditions
		if (!checkStartingConditions(runnableToSchedule)) {
			if (runnableToSchedule.getWaitForStartingConditions()) {
				this.pendingRunnables.add(runnableToSchedule);
			} else {
				runnableToSchedule.setHasBeenInterrupted(true);
			}

			return;
		}

		// try to run
		if (!startRunnable(runnableToSchedule)) {
			// didn't start, so add to queue
			if (runnableToSchedule.getWaitForStartingConditions()) {
				this.pendingRunnables.add(runnableToSchedule);
			} else {
				runnableToSchedule.setHasBeenInterrupted(true);
			}
		}
	}

	public final void checkScheduleQueue() {
		for (Iterator<Runnable> iterator = this.pendingRunnables.iterator(); iterator.hasNext(); ) {
			Runnable runnable = iterator.next();
			if (checkStartingConditions(runnable)) {
				if (startRunnable(runnable)) {
					iterator.remove();
				}
			}
		}
	}

	public final void run() {
		// check scheduled runnables and start them if possible
		checkScheduleQueue();

		// update runnables
		for (Runnable runnable : new ArrayList<>(this.activeRunnables)) {
			if (runnable.getFinished()) {
				if (!runnablesToRemove.contains(runnable)) {
					runnablesToRemove.add(runnable);
				}
			} else {
				runnable.update();

				for (Trigger trigger : runnable.getOwnedTriggers()) {
					if (trigger.check()) {
						trigger.run();
					}
				}
			}
		}

		// remove runnables to be removed
		for (Runnable runnable : runnablesToRemove) {
			this.activeRunnables.remove(runnable);
			runnable.stopInScheduler();
		}
		runnablesToRemove.clear();

		// add runnables to be added
		for (Runnable runnable : runnablesToAdd) {
			this.activeRunnables.add(runnable);
			runnable.startInScheduler();
		}
		runnablesToAdd.clear();

		// if subsystem is not being used, run default directive
		for (Subsystem subsystem : this.subsystems) {
			Runnable defaultDirective = subsystem.getDefaultDirective();

			if (defaultDirective != null && !isSubsystemInUse(subsystem)) {
				schedule(defaultDirective);
			}
		}
	}

	private boolean isSubsystemInUse(Subsystem subsystemToCheck) {
		List<Runnable> futureRunnables = new ArrayList<>();
		futureRunnables.addAll(this.activeRunnables);
		futureRunnables.addAll(this.runnablesToAdd);

		// for every running directive
		for (Runnable runnable : futureRunnables) {
			// check if running directive requires subsystem

			// ignore to-be removed directives
			if (this.runnablesToRemove.contains(runnable)) {
				continue;
			}

			// includes the default directive itself but that's fine for this application
			for (Subsystem requiredSubsystem : runnable.getRequiredSubsystems()) {
				if (requiredSubsystem.equals(subsystemToCheck)) {
					return true;
				}
			}
		}

		return false;
	}

	public final void cancelAll() {
		// clear all queued directives
		this.pendingRunnables.clear();

		// stop all directives
		for (Runnable runnable : this.activeRunnables) {
			runnable.stopInScheduler();
		}

		// clear all running directives
		this.activeRunnables.clear();

		this.runnablesToAdd.clear();
		this.runnablesToRemove.clear();
	}

	@NonNull
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Runnables Queue: %d\nActive Runnables: %d", this.pendingRunnables.size(), this.activeRunnables.size()));

		builder.append("\nRunnables Queue\n");
		for (Runnable runnable : this.pendingRunnables) {
			builder.append(String.format("\n%s", runnable.toString()));
		}

		builder.append("\nActive Runnables\n");
		for (Runnable runnable : this.activeRunnables) {
			builder.append(String.format("\n%s", runnable.toString()));
		}

		return builder.toString();
	}
}
