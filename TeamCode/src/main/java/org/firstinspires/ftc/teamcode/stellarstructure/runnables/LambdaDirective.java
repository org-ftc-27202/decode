package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;
import org.firstinspires.ftc.teamcode.stellarstructure.actions.BooleanAction;
import java.util.function.BooleanSupplier;

public class LambdaDirective extends Directive {
	private BooleanAction onStart = (interrupted) -> {};
	private Action onUpdate = () -> {};
	private BooleanAction onStop = (interrupted) -> {};
	private BooleanSupplier finishedWhen = () -> true;

	public LambdaDirective() {}

	public LambdaDirective onStart(BooleanAction onStart) {
		this.onStart = onStart;
		return this;
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		onStart.run(hadToInterruptToStart);
	}

	public LambdaDirective onUpdate(Action onUpdate) {
		this.onUpdate = onUpdate;
		return this;
	}

	@Override
	public void update() {
		onUpdate.run();
	}

	public LambdaDirective onStop(BooleanAction onStop) {
		this.onStop = onStop;
		return this;
	}

	@Override
	public void stop(boolean interrupted) {
		onStop.run(interrupted);
	}

	public LambdaDirective finishedWhen(BooleanSupplier finishedWhen) {
		this.finishedWhen = finishedWhen;
		return this;
	}

	@Override
	public boolean isFinished() {
		return finishedWhen.getAsBoolean();
	}
}