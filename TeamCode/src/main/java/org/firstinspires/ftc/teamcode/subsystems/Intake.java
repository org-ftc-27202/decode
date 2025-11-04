package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.directives.defaultdirectives.DefaultIntake;
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
	private KineticState targetState;
	private ControlSystem controller;

	@Override
	public void init(HardwareMap hardwareMap) {
		intakeMotor = new StellarDcMotor(hardwareMap, "intake");
		targetState = new KineticState(0,0);
		controller = ControlSystem.builder()
				.velPid(0.1, 0.0, 0.0)
				.build();

	}

	@Override
	public void setGamepads(Gamepad gamepad1, Gamepad gamepad2) {
		setDefaultDirective(new DefaultIntake(this, gamepad1));
	}

	@Override
	public void update(


	) {}

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
		intakeMotor.setPower((controller.calculate(new KineticState(intakeMotor.getVelocity())
        )));
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("Intake Speed: %f", intakeSpeed);
	}
}