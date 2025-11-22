package org.firstinspires.ftc.teamcode.tars.runnables.defaultdirectives;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.stellarstructure.conditions.GamepadButtonMap;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.StatefulCondition;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;
import org.firstinspires.ftc.teamcode.stellarstructure.triggers.ActionTrigger;
import org.firstinspires.ftc.teamcode.tars.subsystems.Turret;

import java.util.Set;

public class DefaultTurret extends DefaultDirective {
    private final Turret turret = Turret.getInstance();
    private double velocity = 0.0;
    private double position = 0.0;

    private double hoodposition = 0.0;
    public DefaultTurret(Gamepad gamepad1, Gamepad gamepad2) {
        super(Turret.getInstance());

        addTrigger(new ActionTrigger(
                // when dpad up just first pressed
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_UP),
                        StatefulCondition.Edge.RISING
                ),
                () -> {
                    velocity += 100.0;
                }
        ));

        addTrigger(new ActionTrigger(
                // when dpad down just first pressed
                new StatefulCondition(
                        new GamepadButtonMap(gamepad1, GamepadButtonMap.Button.DPAD_DOWN),
                        StatefulCondition.Edge.RISING
                ),
                () -> {
                    velocity -= 100.0;
                }
        ));

        addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_LEFT),
                        StatefulCondition.Edge.RISING),
                () -> {
                    position = position + 0.05;
                }
        ));

        addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_RIGHT),
                        StatefulCondition.Edge.RISING),
                () -> {
                    position = position - 0.05;
                }
        ));
        addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_UP),
                        StatefulCondition.Edge.RISING),
                () -> {
                    hoodposition = hoodposition + 0.05;
                }
        ));

        addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_DOWN),
                        StatefulCondition.Edge.RISING),
                () -> {
                    hoodposition = hoodposition - 0.05;
                }
        ));
    }
    @Override
    public void update() {
        turret.setTurretVelocity(velocity);
        turret.getTurretHoodServo().setPosition(hoodposition);
        turret.getTurretServo().setPosition(position);
    }
}
