package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class SetSpeedScale extends Directive {
    public double scale;
    public SetSpeedScale(double scale){
        this.scale = scale;
    }

    @Override
    public void start(boolean hadToInterruptToStart) {
        subsystem(PedroDrivebase.class).setSpeedScale(scale);
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
        return String.format("SetSpeedScale: %f", scale);
    }
}
