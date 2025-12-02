package org.firstinspires.ftc.teamcode.tars.runnables.directives;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.hardwaremapwrappers.StellarLight;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class SetLight extends Directive {
    public StellarLight light;
    public double color;


    public SetLight(StellarLight light, String color) {
        setInterruptible(true);
        setRequiredSubsystems();

        this.light = light;
        //color to power
        switch (color) {
            case "RED":
                this.color = 0.277;
                break;
            case "ORANGE":
                this.color = 0.333;
                break;
            case "YELLOW":
                this.color = 0.388;
                break;
            case "SAGE":
                this.color = 0.444;
                break;
            case "GREEN":
                this.color = 0.500;
                break;
            case "AZURE":
                this.color = 0.555;
                break;
            case "BLUE":
                this.color = 0.611;
                break;
            case "INDIGO":
                this.color = 0.666;
                break;
            case "VIOLET":
                this.color = 0.722;
                break;
            case "OFF":
                this.color = 0.000;
                break;
        }

    }

    @Override
    public void start(boolean hadToInterruptToStart) {
        light.setPosition(color);
    }

    @Override
    public void update() {}

    @Override
    public void stop(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("SetLight: %s", color);
    }
}
