package org.firstinspires.ftc.teamcode.endurancebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class TurnTo extends Directive {

    private final Follower follower;
    private double turnAngle;
    
    public TurnTo(double turnAngle, Follower follower){
        setInterruptible(true);
        this.follower = follower;
        this.turnAngle = turnAngle;


        setRequiredSubsystems(subsystem(PedroDrivebase.class));
    }

    @Override
    protected void onStart(boolean hadToInterruptToStart) {
        follower.turnTo(turnAngle);
    }

    @Override
    protected void onUpdate() {
        follower.update();
    }

    @Override
    protected void onStop(boolean interrupted) {}
    //(Math.abs(follower.getHeadingError()) < Math.toDegrees(0.05))
    @Override
    protected boolean isFinished() {
        return true;
    }
}
