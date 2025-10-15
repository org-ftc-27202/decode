package org.firstinspires.ftc.robotcontroller;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;
import static dev.nextftc.bindings.Bindings.*;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

@TeleOp(name = "NextFTC TeleOp Program Java")
public class TestTeleop extends NextFTCOpMode {
    public TestTeleop() {
        addComponents(
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    private MotorEx frontLeftMotor = new MotorEx("lf").reversed();
    private MotorEx frontRightMotor = new MotorEx("rf").reversed();
    private MotorEx backLeftMotor = new MotorEx("lr").reversed();
    private MotorEx backRightMotor = new MotorEx("rr").reversed();
    private final Pose startPose = new Pose(5, 72, Math.toRadians(0));
    private final Pose basePose = new Pose(24, 100 , Math.toRadians(0));
    Path BaseMovePath;
    FollowPath baseMove;
    public void buildPaths() {
        BaseMovePath = new Path(new BezierLine(startPose, basePose));
        BaseMovePath.setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90), .8);
    }
    MecanumDriverControlled drivercontrolled = new MecanumDriverControlled(
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor,
            Gamepads.gamepad1().leftStickY(),
            Gamepads.gamepad1().leftStickX().negate(),
            Gamepads.gamepad1().rightStickX());
    Button myButton = button(() -> gamepad1.a);
    @Override public void onInit(){
        buildPaths();
        baseMove = new FollowPath(BaseMovePath, true);
        follower().setStartingPose(startPose);
    }
    @Override public void onStartButtonPressed(){
        drivercontrolled.schedule();
        myButton.whenBecomesTrue(() -> baseMove.schedule());
    }
    @Override public void onUpdate() {
        BindingManager.update();
        telemetry.addData("position", follower().getPose());
        telemetry.addData("heading", follower().getHeading());
        telemetry.update();
    }
    @Override public void onStop(){
        BindingManager.reset();

    }
}