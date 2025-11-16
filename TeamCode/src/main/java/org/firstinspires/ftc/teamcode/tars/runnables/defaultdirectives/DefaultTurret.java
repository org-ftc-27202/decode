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
import org.firstinspires.ftc.teamcode.tars.subsystems.Intake;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;

import java.util.Set;

public class DefaultTurret extends DefaultDirective {
    private final Turret turret = Turret.getInstance();
    private double velocity = 0;
    private double position = 0;
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
                    velocity = velocity + 100;
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
                    velocity = velocity -100;
                }
        ));
        addTrigger(new Trigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_LEFT),
                        StatefulCondition.Edge.RISING),
                () -> {
                    position = position +.05;
                }
        ));
        addTrigger(new Trigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_RIGHT),
                        StatefulCondition.Edge.RISING),
                () -> {
                    position = position -.05;
                }
        ));
    }
    @Override
    public void update(){
        turret.setTurretVelocity(velocity);
        turret.getTurretHoodServo().setPosition(position);
    }

}
