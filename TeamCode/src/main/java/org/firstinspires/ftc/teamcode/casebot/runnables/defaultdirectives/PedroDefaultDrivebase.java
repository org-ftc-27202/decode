package org.firstinspires.ftc.teamcode.casebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;

public class PedroDefaultDrivebase extends DefaultDirective {
	private final Gamepad gamepad1;
	private final Gamepad gamepad2;
	private final PedroDrivebase pedroDrivebase = subsystem(PedroDrivebase.class);
	public PedroDefaultDrivebase(Gamepad gamepad1, Gamepad gamepad2) {
		super(subsystem(PedroDrivebase.class));

		this.gamepad1 = gamepad1;
		this.gamepad2 = gamepad2;
	}

	@Override
	protected void onUpdate() {
		double max, axial, lateral, yaw;
		double leftFrontPower, rightFrontPower, leftBackPower, rightBackPower;
		if (subsystem(Turret.class).velocityWithinTolerance()){
			subsystem(PedroDrivebase.class).getLeftLight().setPosition(.611);
		} else if (subsystem(Turret.class).getRealVelocityOffOfTarget()>0) {
			subsystem(PedroDrivebase.class).getLeftLight().setPosition(.677);
		}else if(subsystem(Turret.class).getRealVelocityOffOfTarget()<=0){
			subsystem(PedroDrivebase.class).getLeftLight().setPosition(.388);
		}
		// make the last parameter false for field-centric
			// in case the drivers want to use a "slowMode" you can scale the vectors
			// this is the normal version to use in the teleop

		subsystem(PedroDrivebase.class).getFollower().setTeleOpDrive(
					-gamepad1.left_stick_y * subsystem(PedroDrivebase.class).getSpeedScale(),
					-gamepad1.left_stick_x * subsystem(PedroDrivebase.class).getSpeedScale(),
					-gamepad1.right_stick_x * 0.75001 * subsystem(PedroDrivebase.class).getSpeedScale(),
					true
			); // robot centric
		if (pedroDrivebase.inWrongSideEndgame()){
			//gamepad1.rumble(1.0, 1.0, 500);
			gamepad1.rumbleBlips(1);
		}
	}
}