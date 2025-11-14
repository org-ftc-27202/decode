package org.firstinspires.ftc.teamcode.tars.runnables.directives;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;
import org.firstinspires.ftc.teamcode.tars.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class ClearColor extends Directive {
    public int segment;
    public ClearColor(int segment){
        this.segment = segment;
    }
    @Override
    public void start(boolean hadToInterruptToStart) {
        Spindexer.getInstance().setArtifactInSpindexer(segment, DecodeDataTypes.ArtifactColor.NONE);}

    @Override
    public void update() {

    }

    @Override
    public void stop(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
