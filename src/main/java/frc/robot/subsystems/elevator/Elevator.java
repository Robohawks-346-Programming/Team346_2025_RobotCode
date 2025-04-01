package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.elevator.ElevatorIO;
import frc.robot.subsystems.elevator.ElevatorIOInputsAutoLogged;

public class Elevator extends SubsystemBase {
	private final ElevatorIO io;
	private ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
	private ElevatorState state = ElevatorState.ELEVATOR_HOME_POSITION;
	private double targetPosition;

	public Elevator(ElevatorIO io) {
		this.io = io;
		targetPosition = 0.0;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("elevator", inputs);

		switch (state) {
			case LEVEL_1_POSITION:
				moveToPosition(ElevatorConstants.LEVEL_1_POSITION);
				break;
			case LEVEL_2_POSITION:
				moveToPosition(ElevatorConstants.LEVEL_2_POSITION);
				break;
			case LEVEL_3_POSITION:
				moveToPosition(ElevatorConstants.LEVEL_3_POSITION);
				break;
			case LEVEL_4_POSITION:
				moveToPosition(ElevatorConstants.LEVEL_4_POSITION);
				break;
			case ALGAE_LOW:
				moveToPosition(ElevatorConstants.ALGAE_LOW);
				break;
			case ALGAE_HIGH:
				moveToPosition(ElevatorConstants.ALGAE_HIGH);
				break;
			case ELEVATOR_HOME_POSITION:
				moveToPosition(ElevatorConstants.ELEVATOR_HOME_POSITION);
				break;
		}

		Logger.recordOutput("elevator/currentPosition", inputs.position);
		Logger.recordOutput("elevator/state", state);
		Logger.recordOutput("elevator/targetPosition", targetPosition);
	}

	private void moveToPosition(double targetPosition) {
		this.targetPosition = targetPosition;

	}

	public enum ElevatorState {
		LEVEL_1_POSITION, LEVEL_2_POSITION, LEVEL_3_POSITION, LEVEL_4_POSITION, ALGAE_LOW, ALGAE_HIGH, ELEVATOR_HOME_POSITION
	}

	public Command setState(ElevatorState newState) {
		return Commands.runOnce(() -> this.state = newState);
	}

	public void setStateNonCommand(ElevatorState newState) {
		this.state = newState;
	}

	public Command setTargetPos(double pos) {
		return Commands.runOnce(() -> targetPosition = pos);
	}

	public Command stop() {
		return Commands.runEnd(() -> io.stop(), () -> io.stop());
	}

	public double getTargetPose() {
		return targetPosition;
	}
}
