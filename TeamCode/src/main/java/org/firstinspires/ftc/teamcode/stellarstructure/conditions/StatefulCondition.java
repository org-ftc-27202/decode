package org.firstinspires.ftc.teamcode.stellarstructure.conditions;

public class StatefulCondition implements Condition {
	public enum Edge {
		RISING,  // on press
		FALLING, // on release
		HIGH,    // while pressed
		LOW      // while not pressed
	}

	private final Condition sourceCondition;
	private final Edge edge;
	private boolean currentState = false;
	private boolean firstRun = true;

	public StatefulCondition(Condition sourceCondition, Edge edge) {
		this.sourceCondition = sourceCondition;
		this.edge = edge;
	}

	@Override
	public boolean evaluate() {
		boolean previousState = currentState;
		currentState = sourceCondition.evaluate();

		if (firstRun) {
			firstRun = false;
			return false;
		}

		switch (edge) {
			case RISING:
				return currentState && !previousState;
			case FALLING:
				return !currentState && previousState;
			case HIGH:
				return currentState;
			case LOW:
				return !currentState;
			default:
				return false;
		}
	}
}
