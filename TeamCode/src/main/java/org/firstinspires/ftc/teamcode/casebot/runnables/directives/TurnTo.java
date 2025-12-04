package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class TurnTo extends Directive {

    private final Follower follower;
    private double turnAngle;



    public TurnTo(double turnAngle, Follower follower){
        setInterruptible(true);
        this.follower = follower;
        this.turnAngle = turnAngle;


        setRequiredSubsystems(PedroDrivebase.getInstance());
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
