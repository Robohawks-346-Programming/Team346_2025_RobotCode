package frc.robot.subsystems.ball;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Ball extends SubsystemBase {
	private final BallIO io;
	private BallIO.BallIOInputsAutoLogged inputs = new BallIO.BallIOInputsAutoLogged();
	private BallState state = BallState.IDLE;

	public Ball(BallIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("ball", inputs);

		switch (state) {
			case INTAKE:
				io.intake();
				break;
			case OUTTAKE:
				io.outtake();
				break;
			case IDLE:
			default:
				io.stop();
				break;
		}

		Logger.recordOutput("ball/state", state);
		Logger.recordOutput("ball/leftMotorSpeed", inputs.leftMotorSpeed);
		Logger.recordOutput("ball/rightMotorSpeed", inputs.rightMotorSpeed);
	}

	public enum BallState {
		INTAKE, OUTTAKE, IDLE
	}

	public Command setState(BallState newState) {
		return Commands.runOnce(() -> this.state = newState);
	}

	public Command intake() {
		return setState(BallState.INTAKE);
	}

	public Command outtake() {
		return setState(BallState.OUTTAKE);
	}

	public Command stop() {
		return setState(BallState.IDLE);
	}
}
