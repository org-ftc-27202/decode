package org.firstinspires.ftc.teamcode.stellarstructure;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;

public abstract class Subsystem {
	//todo: maybe add start() or smt
	//todo: make subsystems say what motors/other stuff they use
	private DefaultDirective defaultDirective;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public abstract void init(HardwareMap hardwareMap);
	public abstract void update();

	public final void setDefaultDirective(DefaultDirective defaultDirective) {
		this.defaultDirective = defaultDirective;
	}

	public final DefaultDirective getDefaultDirective() {
		return defaultDirective;
	}
}