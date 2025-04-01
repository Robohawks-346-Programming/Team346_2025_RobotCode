package frc.robot.subsystems.climb;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj.Servo; // Import Servo class
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.elevator.Elevator.ElevatorState;

public class Climb extends SubsystemBase {
	private final ClimbIO io;
	private ClimbIOInputsAutoLogged inputs = new ClimbIOInputsAutoLogged();
	private double position = ClimbConstants.CLIMB_HOME_POSITION;
	private ClimbState state = ClimbState.IDLE;

	public Climb(ClimbIO io) {
		this.io = io;
		position = 0.0;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("climb", inputs);

		Logger.recordOutput("climb/targetPos", position);
		switch (state) {
			case CLIMB_OUT:
				io.setClimbPosition(ClimbConstants.CLIMB_POSITION_1);
				break;
			case CLIMB_IN:
				io.setClimbPosition(ClimbConstants.CLIMB_POSITION_2);
				break;
			case SERVO_DEPLOY:
				io.funnelUp();
				break;
			case IDLE:
				io.setClimbPosition(ClimbConstants.CLIMB_HOME_POSITION);
				break;
		}
	}

	public enum ClimbState {
		CLIMB_OUT, CLIMB_IN, SERVO_DEPLOY, IDLE
	}

	public Command setTargetPos(double pos) {
		return Commands.run(() -> position = pos);
	}

	public Command setState(ClimbState newState) {
		return Commands.runOnce(() -> this.state = newState);
	}

	public void setStateNonCommand(ClimbState newState) {
		this.state = newState;
	}

	public double getPosition() {
		return inputs.position;
	}

	public Command runUp() {
		return Commands.runEnd(() -> io.runManualUp(), () -> io.stop());
	}

	public Command runDown() {
		return Commands.runEnd(() -> io.runManualDown(), () -> io.stop());
	}

	public Command stop() {
		return Commands.runEnd(() -> io.stop(), () -> io.stop());
	}
}
