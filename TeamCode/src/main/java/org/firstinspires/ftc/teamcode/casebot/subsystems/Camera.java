package org.firstinspires.ftc.teamcode.casebot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.stellarstructure.Subsystem;

public final class Camera extends Subsystem {
    private static final Camera camera = new Camera();
    public static Camera getInstance() {
        return camera;
    }
    private Camera() {}

    @Override
    public void init(HardwareMap hardwareMap) {}

    @Override
    public void update() {}
}
