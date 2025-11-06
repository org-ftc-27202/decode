package org.firstinspires.ftc.teamcode.directives.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import dev.nextftc.control.KineticState;

public class DefaultIntakePID extends DefaultDirective {
	private final Intake intake;
	public DefaultIntakePID(Intake intake, Gamepad gamepad) {
		super(intake);
		this.intake = intake;

		//todo: make if/else

		addTrigger(new Trigger(
				() -> gamepad.left_trigger > 0.05, //when left trigger pressed
				() -> {intake.setIntakeTarget(new KineticState(-200 * gamepad.left_trigger));} //set intake to left trigger
		));

		addTrigger(new Trigger(
				() -> gamepad.right_trigger > 0.05, //when right trigger pressed
				() -> {intake.setIntakeTarget(new KineticState(200 * gamepad.right_trigger));} //set intake to right trigger
		));

		addTrigger(new Trigger(
				() -> (gamepad.right_trigger <= 0.05) == (gamepad.left_trigger <= 0.05), //neither or both triggers are pressed
				() -> {intake.setIntakeTarget(new KineticState(0));} //set intake speed to 0
		));
	}

	@Override
	public void update() {
		intake.setMotorPID();
	}
}