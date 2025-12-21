package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
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
    public void start(boolean hadToInterruptToStart) {
        follower.turnTo(turnAngle);
    }

    @Override
    public void update() {
        follower.update();
    }

    @Override
    public void stop(boolean interrupted) {}
    //(Math.abs(follower.getHeadingError())< Math.toDegrees(.05))
    @Override
    public boolean isFinished() {
        return true;
    }
}
