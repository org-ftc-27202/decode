package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

public abstract class Directive extends Runnable {
	public abstract void start(boolean hadToInterruptToStart);

	public abstract void update();

	public abstract void stop(boolean interrupted);

	public abstract boolean isFinished();
}
