package org.firstinspires.ftc.teamcode.tars.runnables.directives;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.tars.runnables.procedures.OuttakeAt;
import org.firstinspires.ftc.teamcode.tars.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.util.DecodeDataTypes;

public class OuttakeColor extends Directive {
    DecodeDataTypes.ArtifactColor artifactColor;
    private Procedure outtakeAt;
    private boolean hasArtifactColor = false;
    public OuttakeColor(DecodeDataTypes.ArtifactColor artifactColor) {
        this.artifactColor = artifactColor;
        setInterruptible(false);
        setRequiredSubsystems();
    }

    @Override
    public void start(boolean hadToInterruptToStart) {
        int segment = Spindexer.getInstance().getFirstColorSegmentLocation(artifactColor);

        if (segment == -1){
            hasArtifactColor = false;
            return;
        }

        outtakeAt = new OuttakeAt(segment);
        outtakeAt.schedule();
    }

    @Override
    public void update() {}

    @Override
    public void stop(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return (!hasArtifactColor || outtakeAt.getHasFinished());
    }
}
