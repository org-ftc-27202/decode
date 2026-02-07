package org.firstinspires.ftc.teamcode.endurancebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class SetSpeedScale extends Directive {
    public double scale;
    public SetSpeedScale(double scale){
        this.scale = scale;
    }

    @Override
    protected void onStart(boolean hadToInterruptToStart) {
        subsystem(PedroDrivebase.class).setSpeedScale(scale);
    }

    @Override
    protected void onUpdate() {}

    @Override
    protected void onStop(boolean interrupted) {}

    @Override
    protected boolean isFinished() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("SetSpeedScale: %f", scale);
    }
}
