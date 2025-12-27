package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;

public class InstantlyDo extends Directive {
	private final Action action;
	public InstantlyDo(Action action) {
		this.action = action;

		setInterruptible(false);
		setRequiredSubsystems();
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		action.run();
	}

	@Override
	protected final void onUpdate() {}

	@Override
	protected final void onStop(boolean interrupted) {}

	@Override
	protected final boolean isFinished() {
		return true;
	}
}