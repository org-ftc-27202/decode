package org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.control.KineticState;

public class StellarDcMotor {
	private final DcMotorEx dcMotorEx;
	public StellarDcMotor(HardwareMap hardwareMap, String dcMotorExName) {
		dcMotorEx = hardwareMap.get(DcMotorEx.class, dcMotorExName);}
	public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		dcMotorEx.setZeroPowerBehavior(zeroPowerBehavior);}
	public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {return dcMotorEx.getZeroPowerBehavior();}
	public void setTargetPosition(int targetPosition) {
		dcMotorEx.setTargetPosition(targetPosition);}
	public int getTargetPosition() {return dcMotorEx.getTargetPosition();}
	public void setPower(double power) {
		dcMotorEx.setPower(power);}
	public double getPower() {return dcMotorEx.getPower();}
	public void setMode(DcMotor.RunMode mode) {
		dcMotorEx.setMode(mode);}
	public DcMotor.RunMode getMode() {return dcMotorEx.getMode();}
	public void setDirection(DcMotor.Direction direction) {
		dcMotorEx.setDirection(direction);}
	public DcMotor.Direction getDirection() {return dcMotorEx.getDirection();}
	public double getVelocity() { return dcMotorEx.getVelocity();}
	public int getCurrentPosition() {return dcMotorEx.getCurrentPosition();}
	public KineticState getState() {
		return new KineticState(dcMotorEx.getCurrentPosition(), dcMotorEx.getVelocity());
	}
	public boolean isBusy() {return dcMotorEx.isBusy();}
	public void resetDeviceConfigurationForOpMode() {dcMotorEx.resetDeviceConfigurationForOpMode();}
}