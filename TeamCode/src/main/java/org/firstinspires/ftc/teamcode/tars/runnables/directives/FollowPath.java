package org.firstinspires.ftc.teamcode.tars.runnables.directives;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Directive;
import org.firstinspires.ftc.teamcode.tars.subsystems.Drivebase;

public class FollowPath extends Directive {
    private final PathChain path;
    private final Follower follower;
    private final Pose endPose;

    private final boolean holdEnd;

    public FollowPath(PathChain path, Follower follower, Pose endPose, boolean holdEnd) {
        setInterruptible(true);
        setRequiredSubsystems(Drivebase.getInstance());
        this.path = path;
        this.follower = follower;
        this.endPose = endPose;
        this.holdEnd = holdEnd;
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
    public void stop(boolean interrupted) {
        follower.breakFollowing();

        Drivebase.getInstance().setDrivePower(0, 0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return follower.atPose(endPose, 1,1);
    }
}
