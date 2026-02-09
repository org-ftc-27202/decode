package org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;

public class DefaultIntake extends DefaultDirective {
	private final Intake intake = subsystem(Intake.class);

	public DefaultIntake(Gamepad gamepad1, Gamepad gamepad2) {
		super(subsystem(Intake.class));

		/*addTrigger(new IteratorTrigger(
				new StatefulCondition(
						() -> gamepad1.left_trigger > 0.05,
						StatefulCondition.Edge.RISING
				),
				() -> {intake.setIntakeSpeed(1.0);},
				() -> {intake.setIntakeSpeed(0.0);},
				() -> {intake.setIntakeSpeed(-1.0);}
		));*/


		addTrigger(new ActionTrigger(
				() -> gamepad1.left_trigger > 0.05, //when left trigger pressed
				//() -> {intake.setIntakeSpeed(0.5);} //set intake to left trigger
				() -> {intake.getIntakeMotor().setPower(-1.0);} //set intake to left trigger
		));

		addTrigger(new ActionTrigger(
				() -> gamepad1.right_trigger <= 0.05, //when right trigger pressed
				() -> {intake.getIntakeMotor().setPower(1.0);} //set intake to right trigger
		));


		/*addTrigger(new ActionTrigger(
				() -> gamepad1.left_bumper, //when right trigger pressed
				() -> {intake.setIntakeSpeed(-1.0);} //set intake to right trigger
		));*/

		/*addTrigger(new ActionTrigger(
				// when dpad right just first pressed
				new StatefulCondition(
						new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_RIGHT),
						StatefulCondition.Edge.RISING
				),
				() -> {
					// test procedure
					new Procedure(
							"IntakeWithTimerProc",
							new Sleep(5),
							new Parallel(
									"IntakeWithTimer",
									new Sleep(10),
									new IntakeAt(2)
							)
					).schedule();
				}
		));*/

		/*addTrigger(new Trigger(
				() -> (gamepad1.right_trigger <= 0.05) == (gamepad1.left_trigger <= 0.05), //neither or both triggers are pressed
				() -> {intake.setIntakeSpeed(0);} //set intake speed to 0
		));*/
	}

	@Override
	protected void onUpdate() {
	//	intake.setMotorSpeed();
	}
}