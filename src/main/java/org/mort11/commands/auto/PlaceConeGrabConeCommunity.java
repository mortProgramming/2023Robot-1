package org.mort11.commands.auto;

import org.mort11.commands.drivetrain.MoveToAprilTag;
import org.mort11.commands.drivetrain.MoveToPos;
import org.mort11.commands.endeffector.ScoreCone;
import org.mort11.commands.endeffector.SetArm;
import org.mort11.commands.endeffector.SetArmAndElevator;
import org.mort11.commands.endeffector.SetClawPiston;
import org.mort11.commands.endeffector.TimedIntake;
import org.mort11.subsystems.Drivetrain;
import org.mort11.subsystems.Wrist;
import org.mort11.util.Constants;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class PlaceConeGrabConeCommunity extends SequentialCommandGroup {
	private Drivetrain drivetrain;

	public PlaceConeGrabConeCommunity() {
		drivetrain = Drivetrain.getInstance();
		addRequirements(drivetrain);

		int isBlue = SmartDashboard.getBoolean("isBlue", true) ? 1 : -1;

		addCommands(
				new InstantCommand(() -> drivetrain.resetPose(0, 0, 0)),
				SetArmAndElevator.upperNode(),
				new SetClawPiston(true),
				new WaitCommand(0.5),
				new ParallelDeadlineGroup(
						new SequentialCommandGroup(
								new MoveToPos(0, isBlue * Units.inchesToMeters(43), 0),
								new MoveToPos(Units.inchesToMeters(200), 0, 0),
								new MoveToPos(Units.inchesToMeters(6), 0,0)
						),
						new SequentialCommandGroup(
								new SetArmAndElevator(Constants.Arm.REST_POSITION, Constants.Elevator.UPPER_NODE_POSITION),
								new SetClawPiston(false),
								SetArmAndElevator.rest(),
								new SetClawPiston(true),
								SetArmAndElevator.floor(),
								new TimedIntake(5, true,true)
						)
				),
				new TimedIntake(0.5, true, true),
				new SetClawPiston(false),
				new WaitCommand(0.2),

				new ParallelCommandGroup(
						new TimedIntake(2, true, true),
						new InstantCommand(() -> Wrist.getInstance().setSetpoint(Constants.Wrist.RIGHT_POSITION)),
						new SetArm(Constants.Arm.REST_POSITION),
						new MoveToPos(Units.inchesToMeters(-190), isBlue * 0.5,0)
				),
				new MoveToAprilTag(6).withTimeout(1.5)
		);

	}
}