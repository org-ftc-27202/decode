package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CompositeRunnable extends Runnable {
	@Override
	protected abstract void onStart(boolean hadToInterruptToStart);

	@Override
	protected abstract void onUpdate();

	@Override
	protected abstract void onStop(boolean interrupted);

	@Override
	protected abstract boolean isFinished();

	protected final Runnable[] runnables;

	protected CompositeRunnable(@NonNull Runnable[] runnables) {
		if (runnables.length == 0) {
			throw new IllegalArgumentException("No directives provided");
		}

		this.runnables = runnables;

		Set<Subsystem> subsystems = new HashSet<>();

		for (Runnable runnable : runnables) {
			subsystems.addAll(Arrays.asList(runnable.getRequiredSubsystems()));
			runnable.setRequiredSubsystems();
			runnable.setOwned();
		}

		setRequiredSubsystems(subsystems.toArray(new Subsystem[0]));
	}

	public Runnable[] getOwnedRunnables() {
		return this.runnables;
	}
}
