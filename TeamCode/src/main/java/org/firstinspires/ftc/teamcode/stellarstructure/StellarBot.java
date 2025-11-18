package org.firstinspires.ftc.teamcode.stellarstructure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class StellarBot {
	private final Subsystem[] subsystems;

	private final Scheduler scheduler = new Scheduler();
	public Scheduler getScheduler() {
		return scheduler;
	}

	public StellarBot(Subsystem... subsystems) {
		this.subsystems = subsystems;

		for (Subsystem subsystem : subsystems) {
			scheduler.addSubsystem(subsystem);
		}
	}

	public final void init(HardwareMap hardwareMap) {
		Scheduler.setGlobalInstance(this.scheduler);

		// initialize all subsystems
		for (Subsystem subsystem : subsystems) {
			subsystem.init(hardwareMap);
		}
	}

	public final void update() {
		// update triggers and directives
		scheduler.run();

		// update subsystems
		for (Subsystem subsystem : subsystems) {
			subsystem.update();
		}
	}

	@NonNull
	@Override
	public final String toString() {
		StringBuilder telemetry = new StringBuilder();

		for (Subsystem subsystem: subsystems) {
			telemetry.append(subsystem).append('\n');
		}

		telemetry.append(scheduler);

		return telemetry.toString();
	}

	public final void cancelAll() {
		scheduler.cancelAll();
	}
}