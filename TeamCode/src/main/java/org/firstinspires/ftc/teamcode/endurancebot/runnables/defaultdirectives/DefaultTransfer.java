package org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;

public class DefaultTransfer extends DefaultDirective {
	private final Transfer transfer = subsystem(Transfer.class);

	public DefaultTransfer(Gamepad gamepad1) {
		super(subsystem(Transfer.class));

		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_UP),
						StatefulCondition.Edge.RISING //On initial press
				),
				() -> {
					new FullOuttake().schedule();
				}
		));

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

		/*addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_LEFT),
						StatefulCondition.Edge.RISING //On initial press
				),
				() -> {
					// up down up
					new PulseLever().schedule();
				}
		));*/
	}

	@Override
	protected void onUpdate() {

	}
}