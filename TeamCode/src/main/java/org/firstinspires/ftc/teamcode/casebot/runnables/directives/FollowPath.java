package org.firstinspires.ftc.teamcode.casebot.runnables.directives;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.casebot.subsystems.PedroDrivebase;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;

public class FollowPath extends Directive {
    private final PathChain path;
    private final Follower follower;
    private final Pose endPose;

    private final boolean holdEnd;
    private final double pathingPower;
    private final double preMaxPower;

    public FollowPath(PathChain path, Follower follower, Pose endPose, boolean holdEnd, double pathingPower) {
        setInterruptible(true);
        this.path = path;
        this.follower = follower;
        this.endPose = endPose;
        this.holdEnd = holdEnd;
        this.pathingPower = pathingPower;
        this.preMaxPower = follower.getMaxPowerScaling();

        setRequiredSubsystems(subsystem(PedroDrivebase.class));
    }

    @Override
    protected void onStart(boolean hadToInterruptToStart) {
        follower.followPath(path, pathingPower, holdEnd);
    }

    @Override
    protected void onUpdate() {
        follower.update();
    }

    @Override
    protected void onStop(boolean interrupted) {
        follower.setMaxPower(preMaxPower);
    }

    @Override
    protected boolean isFinished() {
        return (follower.atPose(endPose, 2,2) && !follower.isBusy())|| (!holdEnd);
    }
}
