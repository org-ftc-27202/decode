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

	public LambdaDirective onStart(Consumer<Boolean> onStart) {
		this.onStart = onStart;
		return this;
	}

	@Override
	public void start(boolean hadToInterruptToStart) {
		onStart.accept(hadToInterruptToStart);
	}

	public LambdaDirective onUpdate(Action onUpdate) {
		this.onUpdate = onUpdate;
		return this;
	}

	@Override
	public void update() {
		onUpdate.run();
	}

	public LambdaDirective onStop(Consumer<Boolean> onStop) {
		this.onStop = onStop;
		return this;
	}

	@Override
	public void stop(boolean interrupted) {
		onStop.accept(interrupted);
	}

	public LambdaDirective finishedWhen(Condition finishedWhen) {
		this.finishedWhen = finishedWhen;
		return this;
	}

	@Override
	public boolean isFinished() {
		return finishedWhen.evaluate();
	}
}