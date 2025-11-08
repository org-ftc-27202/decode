package org.firstinspires.ftc.teamcode.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

public class DefaultSpindexer extends DefaultDirective {
	public DefaultSpindexer(Gamepad gamepad1) {
		super(Spindexer.getInstance());

		addTrigger(new Trigger(
				// when x just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.Y),
						StatefulCondition.Edge.RISING
				),
				() -> {
					new Procedure(
							"FullIntakeOuttake",
							new FullIntake(),
							new FullOuttake()
					);
				}
		));

		addTrigger(new Trigger(
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

		addTrigger(new Trigger(
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
	}
}