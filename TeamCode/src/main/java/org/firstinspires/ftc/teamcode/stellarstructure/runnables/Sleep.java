package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import androidx.annotation.NonNull;

public class Sleep extends Directive {
	private final double sleepTimeSeconds;
	private long endingTime;

	public Sleep(double sleepTimeSeconds) {
		this.sleepTimeSeconds = sleepTimeSeconds;
		setInterruptible(true);
	}

	@Override
	protected void onStart(boolean hadToInterruptToStart) {
		endingTime = System.currentTimeMillis() + (long) (sleepTimeSeconds * 1000.0);
	}

	@Override
	protected void onUpdate() {}

	@Override
	protected void onStop(boolean interrupted) {}

	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() >= endingTime;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Sleep: %.2fs / %.2fs", sleepTimeSeconds - (double) (endingTime - System.currentTimeMillis()) / 1000.0, sleepTimeSeconds);
	}
}