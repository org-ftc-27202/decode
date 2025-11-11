package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetLight;
import org.firstinspires.ftc.teamcode.tars.runnables.directives.SetSpeedScale;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.PedroFullOuttake;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;

public class DefaultSpindexer extends DefaultDirective {
	public DefaultSpindexer(Gamepad gamepad1) {
		super(Spindexer.getInstance());

		addTrigger(new Trigger(
				// when y just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.Y),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// intake 3, outtake 3
					new Procedure(
							"FullIntakeOuttake",
							new FullIntake(),
							new FullOuttake()
					).schedule();
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
		//thomas
		addTrigger(new Trigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.LEFT_BUMPER),
						StatefulCondition.Edge.RISING
				),
				() -> {
					new Procedure(
							"PedroOuttake",
							new SetLight(PedroDrivebase.getInstance().getLight(), "YELLOW"),
							new SetSpeedScale(.2),
							new PedroFullOuttake(),
							new Sleep(Spindexer.BUFFER_TIME),
							new SetSpeedScale(1),
							new SetLight(PedroDrivebase.getInstance().getLight(), "BLUE")

					).schedule();
				}
		));
	}
}