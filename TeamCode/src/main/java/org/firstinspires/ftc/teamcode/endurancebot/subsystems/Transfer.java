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

	public void setWasIntakePhaseLast(boolean intakePhaseLast) {
		wasIntakePhaseLast = intakePhaseLast;
	}

	private boolean wasIntakePhaseLast = false;
	private boolean isIntakePhase = false;
	private boolean wasTurretFull = false;

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


		if (isIntakePhase && turretFull && !wasTurretFull) {
			isIntakePhase = false;
		}

		if (isIntakePhase) {
			setTransferPower(1.0);
			subsystem(Intake.class).getIntakeMotor().setPower(1.0);
			subsystem(Turret.class).lockTurret();
		}

		if (!isIntakePhase && wasIntakePhaseLast) {
			setTransferPower(0.3);
			subsystem(Intake.class).getIntakeMotor().setPower(-0.5);
		}

		if (!isIntakePhase) {
			subsystem(Turret.class).unlockTurret();
		}

		wasIntakePhaseLast = isIntakePhase;
		wasTurretFull = turretFull;
	}

	@NonNull
	@Override
	public String debugTelemetry() {
		return String.format(
				"TurretFull: %b",
		turretFull);
	}
}