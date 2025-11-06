package org.firstinspires.ftc.teamcode.directives.defaultdirectives;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.directives.PulseTransferLever;
import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPosition;
import org.firstinspires.ftc.teamcode.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;

public class DefaultSpindexer extends DefaultDirective {
	public DefaultSpindexer(Spindexer spindexer, Gamepad gamepad1) {
		super(spindexer);

		StellarServo spindexerServo = spindexer.getSpindexerServo();
		DigitalChannel beamBreak = spindexer.getBeamBreak();

		addTrigger(new Trigger(
				// when x just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.X),
						StatefulCondition.Edge.RISING
				),
				() -> {
					new Procedure(
							// set transfer lever down
							new SetPosition(LeverTransfer.getInstance().getLeverTransferServo(), 0.0),

							// move to intake position when beam broken
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(0, Spindexer.Position.INTAKE)).setStartingConditions(beamBreak::getState),
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(1, Spindexer.Position.INTAKE)).setStartingConditions(beamBreak::getState),
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(2, Spindexer.Position.INTAKE)).setStartingConditions(beamBreak::getState),

							// move to transfer position and pulse lever
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(1, Spindexer.Position.TRANSFER)),
							new PulseTransferLever(),
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(0, Spindexer.Position.TRANSFER)),
							new PulseTransferLever(),
							new SetPosition(spindexerServo, spindexer.getDegreesForSegmentPosition(2   , Spindexer.Position.TRANSFER)),
							new PulseTransferLever()
					).schedule();
				}
		));
	}
}
