package org.firstinspires.ftc.teamcode.stellarstructure;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;

// todo: make a iterator trigger that can iterator through actions
public class Trigger {
	private final Condition condition;
	private final Action action;

	public Trigger(Condition condition, Action action) {
		this.condition = condition;
		this.action = action;
	}

	public final boolean check() {
		return condition.evaluate();
	}

	public final void run() {
		action.run();
	}
}