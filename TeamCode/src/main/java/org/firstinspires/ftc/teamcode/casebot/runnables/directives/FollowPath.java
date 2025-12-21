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

    public FollowPath(PathChain path, Follower follower, Pose endPose, boolean holdEnd) {
        setInterruptible(true);
        this.path = path;
        this.follower = follower;
        this.endPose = endPose;
        this.holdEnd = holdEnd;

        setRequiredSubsystems(subsystem(PedroDrivebase.class));
    }

    @Override
    public void start(boolean hadToInterruptToStart) {
        follower.followPath(path, holdEnd);
    }

    @Override
    public void update() {
        follower.update();
    }

    @Override
    public void stop(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return follower.atPose(endPose, 2,2) && !follower.isBusy();
    }
}
