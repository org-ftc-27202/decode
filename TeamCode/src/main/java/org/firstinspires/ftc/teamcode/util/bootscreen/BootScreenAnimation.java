package org.firstinspires.ftc.teamcode.util.bootscreen;

public abstract class BootScreenAnimation {
    public abstract int getFrameCount();
    public abstract int getMilliBetweenFrames();
    public abstract String getFrame(int frame);
}