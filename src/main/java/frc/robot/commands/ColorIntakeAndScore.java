package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.ball.Ball;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.vision.VisionColor;

public class ColorIntakeAndScore extends Command {
	private final Ball ball;
	private final Elevator elevator;
	private final VisionColor visionColor;

	public ColorIntakeAndScore(Ball ball, Elevator elevator, VisionColor visionColor) {
		this.ball = ball;
		this.elevator = elevator;
		this.visionColor = visionColor;
		addRequirements(ball, elevator);
	}

	@Override
	public void initialize() {
		VisionColor.BallColor color = visionColor.getBallColor();
		Command action;
		switch (color) {
			case RED:
				action = Commands.sequence(
						ball.intake(),
						Commands.waitSeconds(0.5),
						elevator.setState(Elevator.ElevatorState.LEVEL_2_POSITION),
						Commands.waitSeconds(0.5),
						ball.outtake(),
						Commands.waitSeconds(0.6),
						ball.stop(),
						elevator.setState(Elevator.ElevatorState.ELEVATOR_HOME_POSITION));
				break;
			case BLUE:
				action = Commands.sequence(
						ball.intake(),
						Commands.waitSeconds(0.5),
						elevator.setState(Elevator.ElevatorState.LEVEL_2_POSITION),
						Commands.waitSeconds(0.5),
						ball.outtake(),
						Commands.waitSeconds(0.6),
						ball.stop(),
						elevator.setState(Elevator.ElevatorState.ELEVATOR_HOME_POSITION));
				break;
			case NONE:
			default:
				action = Commands.sequence(ball.stop());
		}
		action.schedule();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
