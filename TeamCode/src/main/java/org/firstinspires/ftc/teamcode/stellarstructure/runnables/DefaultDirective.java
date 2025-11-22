package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

/**
 * Default directives are directives that can be applied to subsystems.
 * When applied and when the subsystem is not being used, the default directive will be scheduled automatically.
 */

public class DefaultDirective extends Directive {
    public DefaultDirective(Subsystem subsystem) {
        setRequiredSubsystems(subsystem);
        setInterruptible(true);
    }

    @Override
    public void start(boolean hadToInterruptToStart) {}

    @Override
    public void update() {}

    @Override
    public void stop(boolean interrupted) {}

    @Override
    public final boolean isFinished() {
        return false;
    }
}
