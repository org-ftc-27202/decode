package org.firstinspires.ftc.teamcode.stellarstructure.conditions;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadButtonMap implements Condition {
	public enum Button {
		A, B, X, Y,
		LEFT_BUMPER, RIGHT_BUMPER,
		DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
	}

	private final Gamepad gamepad;
	private final Button button;

	public GamepadButtonMap(Gamepad gamepad, Button button) {
		this.gamepad = gamepad;
		this.button = button;
	}

	@Override
	public boolean evaluate() {
		switch (button) {
			case A:
				return gamepad.a;
			case B:
				return gamepad.b;
			case X:
				return gamepad.x;
			case Y:
				return gamepad.y;
			case LEFT_BUMPER:
				return gamepad.left_bumper;
			case RIGHT_BUMPER:
				return gamepad.right_bumper;
			case DPAD_UP:
				return gamepad.dpad_up;
			case DPAD_DOWN:
				return gamepad.dpad_down;
			case DPAD_LEFT:
				return gamepad.dpad_left;
			case DPAD_RIGHT:
				return gamepad.dpad_right;
			default:
				throw new IllegalArgumentException("Unknown button: " + button);
		}
	}
}
