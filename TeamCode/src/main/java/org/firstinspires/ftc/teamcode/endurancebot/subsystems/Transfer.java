package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class Transfer extends Subsystem {



	public StellarDcMotor getTransfer() {
		return transfer;
	}

	public DigitalChannel getBeamBreak() {return beamBreak;}

	public void setTransferPower(double power) {
		transfer.setPower(power);
	}

	private DigitalChannel beamBreak;
	private StellarDcMotor transfer;
	private Boolean beamBreakState;

	@Override
	public void init(HardwareMap hardwareMap) {
		transfer = new StellarDcMotor(hardwareMap, "transfer");

		beamBreak = hardwareMap.get(DigitalChannel.class, "beamBreak");
		beamBreak.setMode(DigitalChannel.Mode.INPUT);

		setTransferPower(1.0);

	}
	public boolean getBeamBreakState(){
		return beamBreakState;
	}
	@Override
	public void update() {
		beamBreakState = beamBreak.getState();
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("Transfer Power: %f", transfer.getPower());
	}
}