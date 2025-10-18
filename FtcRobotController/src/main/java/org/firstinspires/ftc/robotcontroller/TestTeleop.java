package org.firstinspires.ftc.robotcontroller;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
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

                new SubsystemComponent(Intake1.INSTANCE, Spindexer.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    private MotorEx frontLeftMotor = new MotorEx("leftFront").reversed();
    private MotorEx frontRightMotor = new MotorEx("rightFront");
    private MotorEx backLeftMotor = new MotorEx("leftBack").reversed();
    private MotorEx backRightMotor = new MotorEx("rightBack");
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

    Button toBaseButton = button(() -> gamepad1.a);
    Button IntakeButton = button(() -> gamepad1.right_trigger>0);
    Button LeverButton = button(() -> gamepad1.left_trigger>0);
    Button ZeroButton = button(()-> gamepad1.dpad_left);
    Button OneButton = button(()-> gamepad1.dpad_up);
    Button TwoButton = button(() -> gamepad1.dpad_right);
    Button ZeroButtonIntake = ZeroButton.and(IntakeButton);
    Button OneButtonIntake = OneButton.and(IntakeButton);
    Button TwoButtonIntake = TwoButton.and(IntakeButton);
    Button ZeroButtonLever = ZeroButton.xor(ZeroButtonIntake);
    Button OneButtonLever = OneButton.xor(OneButtonIntake);
    Button TwoButtonLever = TwoButton.xor(TwoButtonIntake);

    Button BeamBroke = button(()-> Spindexer.INSTANCE.beamBreakCheck());

    @Override public void onInit(){
        buildPaths();
        baseMove = new FollowPath(BaseMovePath, false);
        follower().setStartingPose(startPose);
    }
    @Override public void onStartButtonPressed(){
        drivercontrolled.schedule();
       // toBaseButton.whenBecomesTrue(() -> baseMove.schedule());
        IntakeButton.whenBecomesTrue(()-> Intake1.INSTANCE.runMotor.schedule());
        IntakeButton.whenBecomesFalse(()-> Intake1.INSTANCE.stopMotor.schedule());
        LeverButton.whenBecomesTrue(()-> Spindexer.INSTANCE.leverUp.schedule());
        LeverButton.whenBecomesFalse(()-> Spindexer.INSTANCE.leverDown.schedule());

        ZeroButtonLever.whenBecomesTrue(()-> Spindexer.INSTANCE.lever0.schedule());
        OneButtonLever.whenBecomesTrue(()-> Spindexer.INSTANCE.lever1.schedule());
        TwoButtonLever.whenBecomesTrue(()-> Spindexer.INSTANCE.lever2.schedule());

        ZeroButtonIntake.whenBecomesTrue(()-> Spindexer.INSTANCE.intake0.schedule());
        OneButtonIntake.whenBecomesTrue(()-> Spindexer.INSTANCE.intake1.schedule());
        TwoButtonIntake.whenBecomesTrue(()-> Spindexer.INSTANCE.intake2.schedule());
        BeamBroke.whenBecomesTrue(()-> Spindexer.INSTANCE.ballThru.schedule());




    }
    @Override public void onUpdate() {
        BindingManager.update();
        telemetry.addData("position", follower().getPose());
        telemetry.addData("heading", follower().getHeading());
        telemetry.addData("button", TwoButtonLever);
        telemetry.update();

    }
    @Override public void onStop(){
        BindingManager.reset();

    }
}