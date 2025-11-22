package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;

import dev.nextftc.control.KineticState;

public class DefaultIntakePID extends DefaultDirective {
	private final Intake intake = Intake.getInstance();

	public DefaultIntakePID(Gamepad gamepad1) {
		super(Intake.getInstance());

		//todo: make if/else

		addTrigger(new ActionTrigger(
				() -> gamepad1.left_trigger > 0.05, //when left trigger pressed
				() -> {intake.setIntakeTarget(new KineticState(-200 * gamepad1.left_trigger));} //set intake to left trigger
		));

		addTrigger(new ActionTrigger(
				() -> gamepad1.right_trigger > 0.05, //when right trigger pressed
				() -> {intake.setIntakeTarget(new KineticState(200 * gamepad1.right_trigger));} //set intake to right trigger
		));

		addTrigger(new ActionTrigger(
				() -> (gamepad1.right_trigger <= 0.05) == (gamepad1.left_trigger <= 0.05), //neither or both triggers are pressed
				() -> {intake.setIntakeTarget(new KineticState(0));} //set intake speed to 0
		));
	}

	@Override
	public void update() {
		intake.setMotorPID();
	}
}