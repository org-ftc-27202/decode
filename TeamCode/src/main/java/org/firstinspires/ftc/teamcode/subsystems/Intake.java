package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.directives.defaultdirectives.DefaultIntakePID;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;


public final class Intake extends Subsystem {
	private static final Intake intake = new Intake();

	public static Intake getInstance() {
		return intake;
	}

	private Intake() {}

	private StellarDcMotor intakeMotor;
	private double intakeSpeed = 0;
	private ControlSystem controller;

	private double intakePower;

	@Override
	public void init(HardwareMap hardwareMap) {
		intakeMotor = new StellarDcMotor(hardwareMap, "intake");
		controller = ControlSystem.builder()
				.posPid(0.01, 0.0, 0.0)
				.build();
	}

	@Override
	public void setGamepads(Gamepad gamepad1, Gamepad gamepad2) {
		setDefaultDirective(new DefaultIntakePID(this, gamepad1));
	}

	@Override
	public void update() {}

	public void setIntakeSpeed(double intakeSpeed) {
		this.intakeSpeed = intakeSpeed;
	}
	public void setIntakeTarget(KineticState targetState){
		controller.setGoal(targetState);
	}

	public void setMotorSpeed() {
		intakeMotor.setPower(intakeSpeed);
	}
	public void setMotorPID(){
		intakePower = (controller.calculate(new KineticState(intakeMotor.getVelocity())));
		intakeMotor.setPower(intakePower);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Intake Speed: %f, Intake Power: %f", intakeMotor.getVelocity(), intakePower);
	}
}