package org.firstinspires.ftc.teamcode.stellarstructure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class StellarBot {
	private static final StellarBot stellarBot = new StellarBot();

	public static StellarBot getInstance() {
		return stellarBot;
	}

	private StellarBot() {}

	private boolean isActive = false;

	private final Scheduler scheduler = new Scheduler();
	private boolean printDebug = false;

	private final Map<Class<? extends Subsystem>, Subsystem> subsystems = new LinkedHashMap<>();

	@SafeVarargs
    public final <T extends Subsystem> void setupBot(T... constructorSubsystems) {
		this.isActive = true;
		for (T subsystem : constructorSubsystems) {
			this.subsystems.put(subsystem.getClass(), subsystem);
		}

		subsystems.forEach((key, subsystem) -> {
			scheduler.addSubsystem(subsystem);
		});
	}

	public final void init(HardwareMap hardwareMap) {
		Scheduler.setGlobalInstance(this.scheduler);

		// initialize all subsystems
		subsystems.forEach((key, subsystem) -> {
			subsystem.init(hardwareMap);
		});
	}

	public final void update() {
		// update triggers and directives
		scheduler.run();

		// update subsystems
		subsystems.forEach((key, subsystem) -> {
			subsystem.update();
		});
	}

	public final void setPrintDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}

	@NonNull
	@Override
	public final String toString() {
		StringBuilder telemetry = new StringBuilder();

		if (printDebug) {
			subsystems.forEach((key, subsystem) -> {
				try {
					telemetry.append(subsystem).append('\n');
				} catch (Error error) {
					telemetry.append(subsystem.getClass().getSimpleName()).append("'s Telemetry Failed!\n");
				}
			});
		}

		telemetry.append(scheduler);

		return telemetry.toString();
	}

	public final void deactivateBot() {
		scheduler.cancelAll();
		this.isActive = false;
	}
}