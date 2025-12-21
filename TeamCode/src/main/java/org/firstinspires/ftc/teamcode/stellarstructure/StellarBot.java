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

	private final Scheduler scheduler = new Scheduler();
	private boolean printDebug;
	private AllianceColor allianceColor = AllianceColor.UNSET;

	private final Map<Class<? extends Subsystem>, Subsystem> subsystems = new LinkedHashMap<>();

	public enum AllianceColor {
		UNSET,
		RED,
		BLUE
	}

	@SafeVarargs
    public final <T extends Subsystem> void setupBot(@NonNull AllianceColor allianceColor, @NonNull T... constructorSubsystems) {
		this.allianceColor = allianceColor;
		this.printDebug = false;

		for (T subsystem : constructorSubsystems) {
			if (subsystem == null) {
				throw new IllegalArgumentException("Subsystems for setting up StellarBot cannot be null");
			}
			this.subsystems.put(subsystem.getClass(), subsystem);
		}

		// todo: add all at once
		subsystems.forEach((key, subsystem) -> {
			scheduler.addSubsystem(subsystem);
		});
	}

	@NonNull
	public AllianceColor getAllianceColor() {
		return this.allianceColor;
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	// create a shortcut
	// import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;
	public static <T extends Subsystem> T subsystem(Class<T> subsystemClass) {
		return getInstance().getSubsystem(subsystemClass);
	}

	public <T extends Subsystem> T getSubsystem(@NonNull Class<T> clazz) {
		return clazz.cast(subsystems.get(clazz));
	}

	public final void init(@NonNull HardwareMap hardwareMap) {
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
	}
}