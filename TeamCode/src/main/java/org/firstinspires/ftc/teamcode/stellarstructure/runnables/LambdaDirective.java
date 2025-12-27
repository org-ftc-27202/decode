package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;

import java.util.function.Consumer;

public class LambdaDirective extends Directive {
	private Consumer<Boolean> onStart = (interrupted) -> {};
	private Action onUpdate = () -> {};
	private Consumer<Boolean> onStop = (interrupted) -> {};
	private Condition finishedWhen = () -> true;

	public LambdaDirective() {}

	public LambdaDirective setOnStart(Consumer<Boolean> onStart) {
		this.onStart = onStart;
		return this;
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		onStart.accept(hadToInterruptToStart);
	}

	public LambdaDirective setOnUpdate(Action onUpdate) {
		this.onUpdate = onUpdate;
		return this;
	}

	@Override
	protected void onUpdate() {
		onUpdate.run();
	}

	public LambdaDirective setOnStop(Consumer<Boolean> onStop) {
		this.onStop = onStop;
		return this;
	}

	@Override
	protected void onStop(boolean interrupted) {
		onStop.accept(interrupted);
	}

	public LambdaDirective setFinishedWhen(Condition finishedWhen) {
		this.finishedWhen = finishedWhen;
		return this;
	}

	@Override
	protected boolean isFinished() {
		return finishedWhen.evaluate();
	}
}