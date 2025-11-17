package org.firstinspires.ftc.teamcode.tars.runnables.directives;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;

public class SetSpeedScale extends Directive {
    public double scale;
    public SetSpeedScale(double scale){
        this.scale = scale;
    }

    @Override
    public void start(boolean hadToInterruptToStart) {
        PedroDrivebase.getInstance().setSpeedScale(scale);
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
