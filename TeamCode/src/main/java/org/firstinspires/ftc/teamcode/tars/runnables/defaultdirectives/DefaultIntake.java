package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Parallel;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Sleep;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.IntakeAt;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;

public class DefaultIntake extends DefaultDirective {
	private final Intake intake = Intake.getInstance();

	public DefaultIntake(Gamepad gamepad1) {
		super(Intake.getInstance());

		//todo: make if/else

		addTrigger(new Trigger(
				() -> gamepad1.left_trigger > 0.05, //when left trigger pressed
				() -> {intake.setIntakeSpeed(0);} //set intake to left trigger
		));

		addTrigger(new Trigger(
				() -> gamepad1.right_trigger > 0.05, //when right trigger pressed
				() -> {intake.setIntakeSpeed(1.0);} //set intake to right trigger
		));

		addTrigger(new Trigger(
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
		));

		/*addTrigger(new Trigger(
				() -> (gamepad1.right_trigger <= 0.05) == (gamepad1.left_trigger <= 0.05), //neither or both triggers are pressed
				() -> {intake.setIntakeSpeed(0);} //set intake speed to 0
		));*/
	}

	@Override
	public void update() {
		intake.setMotorSpeed();
	}
}