package org.firstinspires.ftc.robotcontroller.teamcode;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;

public class IntakeSubsystem implements Subsystem {
    // put hardware, commands, etc here

    Command runIntake = new LambdaCommand()
            .setStart(() -> {
                // Runs on start


            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true)
            .named("run intake"); // sets the name of the command; optional

    @Override
    public void initialize() {
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }
}
