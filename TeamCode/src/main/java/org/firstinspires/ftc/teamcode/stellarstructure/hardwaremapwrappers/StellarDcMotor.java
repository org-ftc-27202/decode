package org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.control.KineticState;

public class StellarDcMotor {
	private final DcMotorEx dcMotorEx;
	private final String dcMotorExName;

	public StellarDcMotor(HardwareMap hardwareMap, @NonNull String dcMotorExName) {
		this.dcMotorEx = hardwareMap.get(DcMotorEx.class, dcMotorExName);
		this.dcMotorExName = dcMotorExName;
	}
	public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		dcMotorEx.setZeroPowerBehavior(zeroPowerBehavior);
	}
	public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
		return dcMotorEx.getZeroPowerBehavior();
	}
	public void setTargetPosition(int targetPosition) {
		dcMotorEx.setTargetPosition(targetPosition);
	}
	public void setTargetVelocity(double targetVelocity){
		dcMotorEx.setVelocity(targetVelocity);
	}
	public int getTargetPosition() {
		return dcMotorEx.getTargetPosition();
	}
	public void setVelocityPIDFCoefficents(double p, double i, double d, double f) {
		dcMotorEx.setVelocityPIDFCoefficients(p,i,d,f);
	}
	public void setPower(double power) {
		dcMotorEx.setPower(power);
	}
	public double getPower() {
		return dcMotorEx.getPower();
	}
	public void setMode(DcMotorEx.RunMode mode) {
		dcMotorEx.setMode(mode);
	}
	public DcMotor.RunMode getMode() {
		return dcMotorEx.getMode();
	}
	public void setDirection(DcMotorEx.Direction direction) {
		dcMotorEx.setDirection(direction);
	}
	public DcMotor.Direction getDirection() {
		return dcMotorEx.getDirection();
	}
	public double getVelocity() {
		return dcMotorEx.getVelocity();
	}
	public int getCurrentPosition() {
		return dcMotorEx.getCurrentPosition();
	}
	public KineticState getState() {
		return new KineticState(dcMotorEx.getCurrentPosition(), dcMotorEx.getVelocity());
	}
	public boolean isBusy() {
		return dcMotorEx.isBusy();
	}
	public void resetDeviceConfigurationForOpMode() {
		dcMotorEx.resetDeviceConfigurationForOpMode();
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("StellarDcMotor: %s", dcMotorExName);
	}
}