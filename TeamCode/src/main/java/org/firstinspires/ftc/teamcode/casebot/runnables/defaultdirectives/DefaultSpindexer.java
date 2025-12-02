package org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntakeWaitForColor;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullPatternOuttake;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;

public class DefaultSpindexer extends DefaultDirective {
	public DefaultSpindexer(Gamepad gamepad1) {
		super(Spindexer.getInstance());

		addTrigger(new ActionTrigger(
				// when y just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.Y),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// outtake 3 in pattern order (PGP for now)
					new FullPatternOuttake().schedule();
				}
		));

		addTrigger(new ActionTrigger(
				// when x just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.X),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// intake 3
					new FullIntake().schedule();
				}
		));

		addTrigger(new ActionTrigger(
				// when b just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.B),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// intake 3
					new FullIntakeWaitForColor().schedule();
				}
		));

		addTrigger(new ActionTrigger(
				// when x just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.A),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// outtake 3
					new FullOuttake().schedule();
				}
		));

		/*
		// thomas
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.LEFT_BUMPER),
						StatefulCondition.Edge.RISING
				),
				() -> {
					new Procedure(
							"PedroOuttake",
							new PedroFullOuttake()

					).schedule();
				}
		));*/
	}
}