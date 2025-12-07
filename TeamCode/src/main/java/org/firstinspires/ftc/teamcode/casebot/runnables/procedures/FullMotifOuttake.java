package org.firstinspires.ftc.teamcode.casebot.runnables.procedures;

import org.firstinspires.ftc.teamcode.casebot.subsystems.LeverTransfer;
import org.firstinspires.ftc.teamcode.casebot.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.util.GameState;

public class FullMotifOuttake extends Procedure {
    public FullMotifOuttake() {
        super(
                "FullPatternOuttake",
                new OuttakeMotifOrArtifactAt(0),
                new OuttakeMotifOrArtifactAt(1),
                new OuttakeMotifOrArtifactAt(2)
        );

        setWaitForStartingConditions(false);
        setRequiredSubsystems(
                Spindexer.getInstance(),
                LeverTransfer.getInstance()
        );
    }
}
