package org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.endurancebot.runnables.directives.GetMotif;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.CloseSingleLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FarLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FarMotifLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FarSingleLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.FullIntakeColor;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.ShortLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures.ShortMotifLaunch;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
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
					new ShortMotifLaunch().schedule();
				}
		));
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.X),
						StatefulCondition.Edge.RISING
				),
				()->{
					new FarMotifLaunch().schedule();
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
		addTrigger(new ActionTrigger(
				new StatefulCondition(
						new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_DOWN),
						StatefulCondition.Edge.RISING
				),
				()->{
					new InstantlyDo(()-> subsystem(PedroDrivebase.class).setEndgame());
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