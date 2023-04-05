package org.mort11.commands.endeffector.floortake;

import org.mort11.subsystems.Floortake;
import org.mort11.util.Constants;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class FloorIntake extends CommandBase {
    private Floortake floortake;

    public FloorIntake() {
        floortake = Floortake.getInstance();
        addRequirements(floortake);
    }

    @Override
    public void initialize() {
        floortake.setFlipState(false);
        floortake.setDrive(Constants.Floortake.INTAKE_SPEED);
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        floortake.setDrive(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
