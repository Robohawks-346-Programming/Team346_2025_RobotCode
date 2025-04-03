package frc.robot.subsystems.algae;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.algae.AlgaeIOInputsAutoLogged;

public class Algae extends SubsystemBase {
	private final AlgaeIO io;
	private AlgaeIOInputsAutoLogged inputs = new AlgaeIOInputsAutoLogged();
	private AlgaeState state = AlgaeState.IDLE;
	private double targetPositionPivot = AlgaeConstants.PIVOT_HOME_POSITION;

	public Algae(AlgaeIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("algae", inputs);

		switch (state) {
			case GROUND_INTAKE:
				// if (getCurrent()) {
				// io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION);
				// io.stop();
				// } else {
				// io.setPivotPosition(AlgaeConstants.GROUND_INTAKE);
				// io.intake();
				// }
				io.setPivotPosition(AlgaeConstants.GROUND_INTAKE);
				io.intake();
				break;
			case ALGAE_HOLD:
				io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION);
				io.intake();
				break;
			case IDLE:
				io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION);
				io.stop();
				break;
			case EJECT:
				io.eject();
				break;
			case REEF_CLEAR:
				// if (getCurrent()) {
				// io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION);
				// io.stop();
				// } else {
				// io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION);
				// io.intake();
				// }
				io.setPivotPosition(AlgaeConstants.PIVOT_HOME_POSITION + 20);
				io.intake();
				break;
			case PROCESSOR:
				io.setPivotPosition(AlgaeConstants.GROUND_INTAKE);
				io.eject();
				break;
			case START:
				io.intake();
				break;
			case NET:
				io.setPivotPosition(-150);
				break;
			case CLIMB:
				io.setPivotPosition(AlgaeConstants.GROUND_INTAKE);
				break;
		}

		Logger.recordOutput("algae/currentPosition", inputs.position);
		Logger.recordOutput("algae/state", state);
		Logger.recordOutput("algae/targetPosition", targetPositionPivot);
		Logger.recordOutput("algae/intakeCurrent", inputs.intakeCurrent);
	}

	public enum AlgaeState {
		GROUND_INTAKE, ALGAE_HOLD, IDLE, EJECT, REEF_CLEAR, PROCESSOR, START, NET, CLIMB
	}

	public Command setState(AlgaeState newState) {
		return Commands.runOnce(() -> this.state = newState);
	}

	public boolean getCurrent() {
		return inputs.intakeCurrent > 40;
	}

}
