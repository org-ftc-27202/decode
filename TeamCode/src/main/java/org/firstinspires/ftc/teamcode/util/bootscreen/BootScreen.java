package org.firstinspires.ftc.teamcode.util.bootscreen;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class BootScreen {
    private final Telemetry telemetry;
    private final boolean repeatAnimation;
    private final BootScreenAnimation animation;
    private int frame = 0;

    public BootScreen(Telemetry telemetry, BootScreenAnimation animation, boolean repeatAnimation) {
        this.telemetry = telemetry;
        this.animation = animation;
        this.repeatAnimation = repeatAnimation;
    }

    public void updateBootScreen() {
        try {
            if (frame >= animation.getFrameCount()) {
                if (!repeatAnimation) {
                    telemetry.addLine();
                    return;
                } else {
                    frame = 0;
                }
            }

            telemetry.addLine(animation.getFrame(frame));
            frame++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMillisBetweenFrames() {
        return animation.getMilliBetweenFrames();
    }

    public int getFrame() {
        return frame;
    }

    public int getFrameCount() {
        return animation.getFrameCount();
    }
}