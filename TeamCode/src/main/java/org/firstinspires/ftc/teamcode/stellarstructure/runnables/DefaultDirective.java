package org.firstinspires.ftc.teamcode.stellarstructure.runnables;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

/**
 * {@link Directive} that is assigned to a {@link Subsystem}.
 * When the {@link Subsystem} is unassigned, this directive will be scheduled.
 *
 * <p> Note: All  {@link Directive}s can interrupt {@link DefaultDirective}s by default.</p>
 *
 * <pre>
 *     {@code
 *     // set up subsystems
 *     tars.init(hardwareMap);
 *
 * 	   // set up default directives
 * 	   drivebase.setDefaultDirective(new DefaultDrivebase(gamepad1, gamepad2));
 *     }
 * </pre>
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
