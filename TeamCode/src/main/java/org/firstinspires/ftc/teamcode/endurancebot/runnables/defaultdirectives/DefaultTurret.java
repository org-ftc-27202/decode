package org.firstinspires.ftc.teamcode.endurancebot.runnables.defaultdirectives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.DefaultDirective;

public class DefaultTurret extends DefaultDirective {
    private final Turret turret = subsystem(Turret.class);
    private double velocity = 0.0;
    private double position = 0.0;

    private double hoodPosition = 0.0;
    public DefaultTurret(Gamepad gamepad1, Gamepad gamepad2) {
        super(subsystem(Turret.class));

        /*addTrigger(new ActionTrigger(
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
        ));*/
        /*addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_UP),
                        StatefulCondition.Edge.RISING),
                () -> {
                    hoodPosition = hoodPosition + 0.05;
                }
        ));

        addTrigger(new ActionTrigger(
                new StatefulCondition(
                        new GamepadButtonMap(gamepad2, GamepadButtonMap.Button.DPAD_DOWN),
                        StatefulCondition.Edge.RISING),
                () -> {
                    hoodPosition = hoodPosition - 0.05;
                }
        ));*/

    }

    @Override
    protected void onUpdate() {
        /*if (PedroDrivebase.getInstance().getFollower().getPose().getY() >= 44){
            velocity = 1300.0;
        } else {
            velocity = 1600.0;
        }*/

    }
}
