package org.firstinspires.ftc.teamcode.endurancebot.runnables.procedures;

import static org.firstinspires.ftc.teamcode.stellarstructure.StellarBot.subsystem;

import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Turret;
import org.firstinspires.ftc.teamcode.endurancebot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.InstantlyDo;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.Procedure;
import org.firstinspires.ftc.teamcode.stellarstructure.runnables.WaitUntil;

public class IntakeThree extends Procedure {
    public IntakeThree() {
        super(
                "Intake",
                new InstantlyDo(() -> subsystem(Transfer.class).setTransferPower(1.0)),
                new InstantlyDo(()-> subsystem(Intake.class).getIntakeMotor().setPower(1.0)),
                new WaitUntil(()-> subsystem(Transfer.class).getTurretFull()),
                new InstantlyDo(() -> subsystem(Transfer.class).setTransferPower(0.3)),
                new InstantlyDo(()-> subsystem(Intake.class).getIntakeMotor().setPower(-0.5))
        );

        setRequiredSubsystems(
                subsystem(Transfer.class),
                subsystem(Turret.class)
        );

        setInterruptible(true);
    }
}
