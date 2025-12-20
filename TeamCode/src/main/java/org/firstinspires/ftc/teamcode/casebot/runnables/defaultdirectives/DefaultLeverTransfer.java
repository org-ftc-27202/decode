package org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.PulseTransferLever;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

public class DefaultLeverTransfer extends DefaultDirective {
	private final LeverTransfer leverTransfer = subsystem(LeverTransfer.class);

	public DefaultLeverTransfer(Gamepad gamepad1) {
		super(subsystem(LeverTransfer.class));

		/*addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_UP),
						StatefulCondition.Edge.RISING //On initial press
				),
				() -> {
					leverTransfer.setLeverPositionIsUp(true);
					leverTransfer.updateServoPosition();
				}
		));*/

		/*addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_DOWN),
						StatefulCondition.Edge.RISING //On initial press
				),
				() -> {
					leverTransfer.setLeverPositionIsUp(false);
					leverTransfer.updateServoPosition();
				}
		));*/

		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_LEFT),
						StatefulCondition.Edge.RISING //On initial press
				),
				() -> {
					// up down up
					new PulseTransferLever().schedule();
				}
		));
	}
}