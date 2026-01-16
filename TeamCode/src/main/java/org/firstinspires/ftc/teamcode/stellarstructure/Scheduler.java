package org.firstinspires.ftc.teamcode.stellarstructure;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.CompositeRunnable;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Runnable;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.Trigger;
import org.firstinspires.ftc.teamcode.util.MonoSpacedFont;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scheduler {
	// todo: make the scheduler check to see if a directive finishes instantly or smt
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
					if (requiredByNew.equals(requiredByRunning) && !runningRunnable.isInterruptible()) {
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
				runnableToSchedule.setStatus(Runnable.Status.PENDING);
			} else {
				runnableToSchedule.setHasBeenInterrupted(true);
				runnableToSchedule.setStatus(Runnable.Status.BLOCKED);
			}

			return;
		}

		// try to run
		if (!startRunnable(runnableToSchedule)) {
			// didn't onStart, so add to queue
			if (runnableToSchedule.getWaitForStartingConditions()) {
				this.pendingRunnables.add(runnableToSchedule);
				runnableToSchedule.setStatus(Runnable.Status.PENDING);
			} else {
				runnableToSchedule.setHasBeenInterrupted(true);
				runnableToSchedule.setStatus(Runnable.Status.BLOCKED);
			}
		}
	}

	public final void run() {
		// check scheduled runnables and onStart them if possible
		for (Iterator<Runnable> iterator = this.pendingRunnables.iterator(); iterator.hasNext(); ) {
			Runnable runnable = iterator.next();
			if (checkStartingConditions(runnable)) {
				if (startRunnable(runnable)) {
					iterator.remove();
				}
			}
		}

		// update runnables
		for (Runnable runnable : new ArrayList<>(this.activeRunnables)) {
			runnable.updateIsFinished();
			if (runnable.getFinished()) {
				if (!runnablesToRemove.contains(runnable)) {
					runnablesToRemove.add(runnable);
				}
			} else {
				runnable.update();

				for (Trigger trigger : runnable.getOwnedTriggers()) {
					trigger.update();
				}
			}
		}

		// remove runnables to be removed
		for (Runnable runnable : runnablesToRemove) {
			this.activeRunnables.remove(runnable);
			runnable.stop();
		}
		runnablesToRemove.clear();

		// add runnables to be added
		for (Runnable runnable : runnablesToAdd) {
			this.activeRunnables.add(runnable);
			runnable.start();
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

	private boolean isSubsystemInUse(@NonNull Subsystem subsystemToCheck) {
		List<Runnable> futureRunnables = new ArrayList<>();
		futureRunnables.addAll(this.activeRunnables);
		futureRunnables.addAll(this.runnablesToAdd);

		// check if running directive requires subsystem
		for (Runnable runnable : futureRunnables) {
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

		// onStop all directives
		for (Runnable runnable : this.activeRunnables) {
			runnable.stop();
		}

		// clear all running directives
		this.activeRunnables.clear();

		this.runnablesToAdd.clear();
		this.runnablesToRemove.clear();
	}


	public void buildBranch(StringBuilder stringBuilder, String prefix, Runnable runnable, boolean isLast) {
		stringBuilder.append(prefix);
		stringBuilder.append(runnable.getStatus() == Runnable.Status.ACTIVE ? "█" : (isLast ? "└" : "├"));
		stringBuilder.append('─');
		stringBuilder.append(runnable instanceof CompositeRunnable ? '┬' : '─');
		stringBuilder.append("   ");
		int classLength = MonoSpacedFont.toMonospace(stringBuilder, runnable.getClass().getSimpleName());

		for (int i = 0; i < 22 - classLength - prefix.length() / 2; i++) {
			stringBuilder.append("   ");
		}
		MonoSpacedFont.toMonospace(stringBuilder, runnable.getStatus().toString());
		stringBuilder.append("   ║\n");

		if (runnable instanceof CompositeRunnable) {
			Runnable[] children = ((CompositeRunnable) runnable).getOwnedRunnables();

			for (int i = 0; i < children.length; i++) {
				buildBranch(stringBuilder, prefix + "│   ", children[i], i == children.length - 1);
			}
		}
	}

	public void buildRunnable(StringBuilder stringBuilder, Runnable runnable) {
		stringBuilder.append("╔═╤   ");
		int classLength = MonoSpacedFont.toMonospace(stringBuilder, runnable.getClass().getSimpleName());

		stringBuilder.append("   ");
		for (int i = 0; i < 26 - classLength; i++) {
			stringBuilder.append('═');
		}
		stringBuilder.append("╗\n");

		if (runnable instanceof CompositeRunnable) {
			for (Runnable child : ((CompositeRunnable) runnable).getOwnedRunnables()) {
				buildBranch(stringBuilder, "║   ", child, false);
			}
		}
		stringBuilder.append("╚══════════════════════════════╝\n");
	}

	@NonNull
	@Override
	public final String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Runnables Queue: ").append(this.pendingRunnables.size()).append("\nActive Runnables: ").append(this.activeRunnables.size());

		stringBuilder.append("\nRunnables Queue\n");
		for (Runnable runnable : this.pendingRunnables) {
			stringBuilder.append(String.format("\n%s", runnable.getClass().getSimpleName()));
		}

		/*stringBuilder.append("\nActive Runnables\n");
		for (Runnable runnable : this.activeRunnables) {
			if (!runnable.isOwned()) {
				buildRunnable(stringBuilder, runnable);
			}
		}*/

		return stringBuilder.toString();
	}
}
