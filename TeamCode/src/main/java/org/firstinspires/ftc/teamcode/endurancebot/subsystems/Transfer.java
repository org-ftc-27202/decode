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

	public DigitalChannel getBeamBreak1() {return beamBreak1;}
	public DigitalChannel getBeamBreak2() {return beamBreak2;}

	public void setTransferPower(double power) {
		transfer.setPower(power);
	}

	private DigitalChannel beamBreak1, beamBreak2;
	private StellarDcMotor transfer;
	private Boolean beamBreakState = false;
	private Boolean turretFull = false;
	private Boolean lastBeamBreakState = false;
	private static final int TURRET_FULL_CYCLES = 4;

	private int updateNo = 0;

	@Override
	public void init(HardwareMap hardwareMap) {
		transfer = new StellarDcMotor(hardwareMap, "transfer");

		beamBreak1 = hardwareMap.get(DigitalChannel.class, "beamBreak1");
		beamBreak1.setMode(DigitalChannel.Mode.INPUT);

		beamBreak2 = hardwareMap.get(DigitalChannel.class, "beamBreak2");
		beamBreak2.setMode(DigitalChannel.Mode.INPUT);

		setTransferPower(1.0);

	}
	public boolean getBeamBreakState(){
		return beamBreakState;
	}
	public boolean getTurretFull(){
		return turretFull;
	}

	@Override
	public void update() {
		beamBreakState = (!beamBreak1.getState() || !beamBreak2.getState());
		updateNo = updateNo+1;
		if (updateNo%TURRET_FULL_CYCLES ==0){
            turretFull = beamBreakState && lastBeamBreakState;
			lastBeamBreakState = beamBreakState;
		}
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format("TurretFull: %b", turretFull);
	}
}