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

public class AutoAlgaeAlign extends Command {

	private final Drive m_drivetrain;
	private final Elevator m_elevator;

	private boolean highAlgae = false;
	private boolean lowAlgae = false;

	private Pose2d m_goalPose;
	private Transform2d m_shift;

	private static final int[] HIGH_ALGAE_IDS = { 18, 20, 22, 7, 9, 11 };
	private static final int[] LOW_ALGAE_IDS = { 19, 17, 21, 8, 6, 10 };

	public AutoAlgaeAlign(Drive drivetrain, Elevator elevator, Transform2d shift) {
		m_drivetrain = drivetrain;
		m_elevator = elevator;
		m_shift = shift;
		addRequirements(drivetrain, elevator);
	}

	@Override
	public void initialize() {

		ArrayList<Pose2d> alignPositionsHigh = new ArrayList<>();
		ArrayList<Pose2d> alignPositionsLow = new ArrayList<>();

		int[] a = { 6, 7, 8, 9, 10, 11,
				17, 18, 19, 20, 21, 22 };
		for (var tag : VisionConstants.aprilTagLayout.getTags()) {
			if (Arrays.stream(HIGH_ALGAE_IDS).anyMatch(x -> x == tag.ID)) {
				alignPositionsHigh.add(new Pose2d(tag.pose.toPose2d().getTranslation(),
						tag.pose.toPose2d().getRotation().rotateBy(Rotation2d.k180deg)));
			} else if (Arrays.stream(LOW_ALGAE_IDS).anyMatch(x -> x == tag.ID)) {
				alignPositionsLow.add(new Pose2d(tag.pose.toPose2d().getTranslation(),
						tag.pose.toPose2d().getRotation().rotateBy(Rotation2d.k180deg)));
			}
		}

		m_goalPose = m_drivetrain.findClosestNode();

		for (var tag : alignPositionsHigh) {
			if (m_goalPose.equals(tag)) {
				highAlgae = true;
			}
		}

		for (var tag : alignPositionsLow) {
			if (m_goalPose.equals(tag)) {
				lowAlgae = true;
			}
		}
		Logger.recordOutput("Drivetrain/DriveToPose/AlgaePose", m_goalPose);
		Logger.recordOutput("Drivetrain/DriveToPose/HighAlgae", highAlgae);
		Logger.recordOutput("Drivetrain/DriveToPose/LowAlgae", lowAlgae);

	}

	@Override
	public void execute() {
		if (highAlgae) {

			m_elevator.setStateNonCommand(ElevatorState.ALGAE_HIGH);
		} else if (lowAlgae) {

			m_elevator.setStateNonCommand(ElevatorState.ALGAE_LOW);
		}

	}

	@Override
	public void end(boolean interrupted) {
		m_elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION);
		highAlgae = false;
		lowAlgae = false;
	}

	@Override
	public boolean isFinished() {
		return m_elevator.getTargetPose() == m_elevator.getTargetPose();
	}
}
