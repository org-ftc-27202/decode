package org.firstinspires.ftc.teamcode.util.bootscreen;

public class TerminalVelocityLogo extends BootScreenAnimation {
    @Override
    public int getFrameCount() {
        return 1;
    }

    @Override
    public int getMilliBetweenFrames() {
        return 0;
    }

    @Override
    public String getFrame(int frame) {
        return "\n                                 █\n" +
                "                                 █              █\n" +
                "                                 █              █              █\n" +
                "                                                  █              █\n" +
                "                       ▄███▄                             █\n" +
                "                    ▐█████▌██████▄\n" +
                "        ▌            ▀███▀▄███████▄                  ▄▄\n" +
                "        █                  ▄███████████▌     ▄▄███▀\n" +
                "        ▐▄    ▐██████▀      ▀▀▀▀████████▀\n" +
                "              ▀▀▀▀                                          ▀▀";
    }
}
