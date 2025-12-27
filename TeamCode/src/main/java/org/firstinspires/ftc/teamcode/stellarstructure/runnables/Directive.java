package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

public abstract class Directive extends Runnable {
	protected abstract void onStart(boolean hadToInterruptToStart);

	protected abstract void onUpdate();

	protected abstract void onStop(boolean interrupted);

	protected abstract boolean isFinished();
}
