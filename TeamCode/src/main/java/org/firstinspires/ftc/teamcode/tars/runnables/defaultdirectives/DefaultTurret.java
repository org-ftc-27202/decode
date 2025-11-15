package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.Trigger;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.SetPower;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullIntake;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullOuttake;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.FullPatternOuttake;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.PedroFullOuttake;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;

import java.util.Set;

public class DefaultTurret extends DefaultDirective {
    public DefaultTurret(Gamepad gamepad1) {
        super(Turret.getInstance());

        addTrigger(new Trigger(
                // when y just first pressed
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_UP),
                        StatefulCondition.Edge.RISING
                ),
                () -> {
                    //outtake 3 in pattern order (PGP for now)
                    new SetPower(Turret.getInstance().getLeftTurretMotor(), 1).schedule();
                    new SetPower(Turret.getInstance().getRightTurretMotor(), 1).schedule();
                }
        ));

        addTrigger(new Trigger(
                // when x just first pressed
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_DOWN),
                        StatefulCondition.Edge.RISING
                ),
                () -> {
                    // intake 3
                    new SetPower(Turret.getInstance().getLeftTurretMotor(), 0).schedule();
                    new SetPower(Turret.getInstance().getRightTurretMotor(), 0).schedule();
                }
        ));
    }
}
