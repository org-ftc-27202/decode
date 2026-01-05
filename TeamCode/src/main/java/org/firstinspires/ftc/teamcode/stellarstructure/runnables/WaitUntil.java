package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import java.util.function.BooleanSupplier;

public class WaitUntil extends Directive {
	private final BooleanSupplier condition;
	public WaitUntil(BooleanSupplier condition) {
		this.condition = condition;
		setInterruptible(false);
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {}

	@Override
	protected void onUpdate() {}

	@Override
	protected void onStop(boolean interrupted) {}

	@Override
	protected boolean isFinished() {
		return condition.getAsBoolean();
	}
}
