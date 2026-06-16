package org.firstinspires.ftc.teamcode.endurancebot.subsystems;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;

public final class Transfer extends Subsystem {
	public void setIntakePhase(boolean intakePhase) {
		isIntakePhase = intakePhase;
	}

	private boolean wasIntakePhaseLast = false;
	private boolean isIntakePhase = false;

	public StellarDcMotor getTransfer() {
		return transfer;
	}
	public CRServo getTransferWheel() {
		return transferWheel;
	}

	public DigitalChannel getBeamBreak1() {return beamBreak1;}
	public DigitalChannel getBeamBreak2() {return beamBreak2;}

	public void setTransferPower(double power) {
		transfer.setPower(power);
	}

	public void setTransferWheelPower(double power) {
		transferWheel.setPower(power);
	}

	private DigitalChannel beamBreak1, beamBreak2;
	private StellarDcMotor transfer;
	private Boolean beamBreakState = false;
	private Boolean turretFull = false;
	private Boolean lastBeamBreakState = false;
	private static final int TURRET_FULL_CYCLES = 4;
	private CRServo transferWheel;

	private int updateNo = 0;

	@Override
	public void init(HardwareMap hardwareMap) {
		transfer = new StellarDcMotor(hardwareMap, "transfer");

		beamBreak1 = hardwareMap.get(DigitalChannel.class, "beamBreak1");
		beamBreak1.setMode(DigitalChannel.Mode.INPUT);

		beamBreak2 = hardwareMap.get(DigitalChannel.class, "beamBreak2");
		beamBreak2.setMode(DigitalChannel.Mode.INPUT);

		transferWheel = hardwareMap.get(CRServo.class, "transferWheel");

		setTransferPower(1.0);
	}

	public boolean getBeamBreakState(){
		return beamBreakState;
	}

	@Override
	public void update() {
		beamBreakState = (!beamBreak1.getState() || !beamBreak2.getState());
		updateNo++;
		if (updateNo % TURRET_FULL_CYCLES == 0) {
            turretFull = beamBreakState && lastBeamBreakState;
			lastBeamBreakState = beamBreakState;
		}

		if (isIntakePhase && turretFull) {
			isIntakePhase = false;
		}

		if (!isIntakePhase && wasIntakePhaseLast) {
			subsystem(Intake.class).getIntakeMotor().setPower(-0.5);
			subsystem(Transfer.class).setTransferPower(0.3);
		}
		
		wasIntakePhaseLast = isIntakePhase;
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format(
				"TurretFull: %b",
		turretFull);
	}
}