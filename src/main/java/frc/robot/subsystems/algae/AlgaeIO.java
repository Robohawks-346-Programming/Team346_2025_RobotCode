package frc.robot.subsystems.algae;

import org.littletonrobotics.junction.AutoLog;

public interface AlgaeIO {

	@AutoLog
	public static class AlgaeIOInputs {
		public double position = AlgaeConstants.PIVOT_HOME_POSITION;
	}

	public default void updateInputs(AlgaeIOInputs inputs) {
	}

	public default void setPivotPosition(double wantedPosition) {
	}

}
