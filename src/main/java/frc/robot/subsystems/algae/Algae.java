package frc.robot.subsystems.algae;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Algae extends SubsystemBase {
	private final AlgaeIO io;
	private AlgaeIOInputsAutoLogged inputs = new AlgaeIOInputsAutoLogged();
	private PivotState state = PivotState.POSITION_1;
	private double targetPositionPivot = AlgaeConstants.PIVOT_HOME_POSITION;

	public Algae(AlgaeIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("pivot", inputs);

		switch (state) {
			case POSITION_1:
				io.setPivotPosition(AlgaeConstants.POSITION_1);
				break;
			case POSITION_2:
				io.setPivotPosition(AlgaeConstants.POSITION_2);
				break;
			case POSITION_3:
				io.setPivotPosition(AlgaeConstants.POSITION_3);
				break;
			case POSITION_4:
				io.setPivotPosition(AlgaeConstants.POSITION_4);
				break;
			case GROUND_INTAKE:
				io.setPivotPosition(AlgaeConstants.GROUND_INTAKE);
				break;
			case ALGAE_HOLD:
				io.setPivotPosition(AlgaeConstants.ALGAE_HOLD);
				break;
			case ALGAE_INTAKE:
				io.setPivotPosition(AlgaeConstants.ALGAE_INTAKE);
				break;
			case PROCESSOR:
				io.setPivotPosition(AlgaeConstants.PROCESSOR);
				break;
		}

		Logger.recordOutput("pivot/currentPosition", inputs.position);
		Logger.recordOutput("pivot/state", state);
		Logger.recordOutput("pivot/targetPosition", targetPositionPivot);
	}

	public enum PivotState {
		POSITION_1, POSITION_2, POSITION_3, POSITION_4, GROUND_INTAKE, ALGAE_HOLD, ALGAE_INTAKE, PROCESSOR
	}

	public Command setState(PivotState newState) {
		return Commands.runOnce(() -> this.state = newState);
	}

	public Command setTargetPos(double pos) {
		return Commands.runOnce(() -> targetPositionPivot = pos);
	}
}
