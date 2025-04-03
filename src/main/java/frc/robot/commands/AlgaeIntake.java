package frc.robot.commands;

import frc.robot.subsystems.algae.Algae;
import frc.robot.subsystems.algae.Algae.AlgaeState;
import frc.robot.subsystems.coral.Coral;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator.ElevatorState;
import frc.robot.subsystems.elevator.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.vision.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;

public class AlgaeIntake extends Command {

	private final Algae m_algae;

	public AlgaeIntake(Algae algae) {
		m_algae = algae;
		addRequirements(algae);
	}

	@Override
	public void initialize() {
		Logger.recordOutput("algae/hasPiece", false);
	}

	@Override
	public void execute() {
		Logger.recordOutput("algae/hasPiece", false);
	}

	@Override
	public void end(boolean interrupted) {
		Logger.recordOutput("algae/hasPiece", true);
	}

	@Override
	public boolean isFinished() {
		return m_algae.getCurrent();
	}
}
