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
	public void start(boolean hadToInterruptToStart) {
		action.run();
	}

	@Override
	public final void update() {}

	@Override
	public final void stop(boolean interrupted) {}

	@Override
	public final boolean isFinished() {
		return true;
	}
}