package org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.casebot.runnables.directives.GetMotif;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.CloseSingleLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarColorLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FarSingleLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.FullIntakeColor;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.ShortColorLaunch;
import org.firstinspires.ftc.teamcode.casebot.runnables.procedures.ShortLaunch;
import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class DefaultSpindexer extends DefaultDirective {
	public DefaultSpindexer(Gamepad gamepad1, Gamepad gamepad2) {
		super(subsystem(Spindexer.class));

		addTrigger(new ActionTrigger(
				// when y just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.Y),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// outtake 3 in pattern order (PGP for now)
					new FarLaunch().schedule();
				}
		));

		addTrigger(new ActionTrigger(
				// when x just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.RIGHT_BUMPER),
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
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.LEFT_BUMPER),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// intake 3
					new FullIntakeColor().schedule();
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
					new ShortLaunch().schedule();
				}
		));

		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.B),
						StatefulCondition.Edge.RISING
				),
				()->{
					new ShortColorLaunch().schedule();
				}
		));
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.X),
						StatefulCondition.Edge.RISING
				),
				()->{
					new FarColorLaunch().schedule();
				}
		));

		//GAMEPAD 2 CONTROLS:



		addTrigger(new ActionTrigger(
				// when y just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.Y),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// outtake 3
					new FarSingleLaunch(DecodeDataTypes.ArtifactColor.PURPLE);
				}
		));

		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.B),
						StatefulCondition.Edge.RISING
				),
				()->{
					new CloseSingleLaunch(DecodeDataTypes.ArtifactColor.PURPLE);
				}
		));

		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.A),
						StatefulCondition.Edge.RISING
				),
				()->{
					new CloseSingleLaunch(DecodeDataTypes.ArtifactColor.GREEN);
				}
		));
		addTrigger(new ActionTrigger(
				// when y just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.X),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// outtake 3
					new FarSingleLaunch(DecodeDataTypes.ArtifactColor.GREEN);
				}
		));
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_UP),
						StatefulCondition.Edge.RISING
				),
				()->{
					new GetMotif().schedule();
				}
		));
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_LEFT),
						StatefulCondition.Edge.RISING
				),
				()->{
					new InstantlyDo(()-> subsystem(PedroDrivebase.class).getFollower().startTeleopDrive(true));
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