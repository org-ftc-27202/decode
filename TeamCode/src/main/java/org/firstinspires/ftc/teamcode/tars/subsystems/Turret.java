package org.firstinspires.ftc.teamcode.tars.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarDcMotor;
import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarServo;

public final class Turret extends Subsystem {
    private static final Turret turret = new Turret();

    public static Turret getInstance() {
        return turret;
    }

    private Turret() {}

    private final static double DEGREES_TO_SERVO = 1.0 / 320.0;
    private final static double SPINDEXER_OFFSET = 0.0;
    private final static double[] INTAKE_DEGREE_POSITIONS = {0.0 + SPINDEXER_OFFSET, 120.0 + SPINDEXER_OFFSET, 240.0 + SPINDEXER_OFFSET};
    private final static double[] TRANSFER_DEGREE_POSITIONS = {180.0 + SPINDEXER_OFFSET, 300.0 + SPINDEXER_OFFSET, 60.0 + SPINDEXER_OFFSET};

    public final static double BUFFER_TIME = 1;

    public enum Position {
        ON, OFF
    }

	/*

	+-----------+--------+----------+
	|           | Intake | Transfer |
	| Segment 0 |   0    |   180    |
	| Segment 1 |  120   |   300    |
	| Segment 2 |  240   |   60     |
	+-----------+--------+----------+

	Intake Procedure (60 ->) 0 -> 120 -> 240
	Transfer Procedure 240 -> 300 -> 180 -> 60

	 */

    private StellarServo turretServo;
    private StellarDcMotor leftTurretMotor;
    private StellarDcMotor rightTurretMotor;


    @Override
    public void init(HardwareMap hardwareMap) {
        turretServo= new StellarServo(hardwareMap, "turretServo");
        leftTurretMotor = new StellarDcMotor(hardwareMap, "leftTurretMotor" );
        rightTurretMotor = new StellarDcMotor(hardwareMap, "rightTurretMotor");
        leftTurretMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightTurretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void update() {}

    public StellarServo getTurretServo() {
        return turretServo;
    }

    public StellarDcMotor getLeftTurretMotor() {
        return leftTurretMotor;
    }

    public StellarDcMotor getRightTurretMotor() {
        return rightTurretMotor;
    }




    @NonNull
    @Override
    public String toString() {
        return String.format(
                "Turret Servo Pos: %f\n" +
                        "Turret Motor Vel: %f\n",
                turretServo.getPosition(),
                leftTurretMotor.getVelocity(), rightTurretMotor.getVelocity()
        );
    }
}