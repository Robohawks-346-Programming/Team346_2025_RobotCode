package frc.robot.subsystems.algae;

import org.littletonrobotics.junction.AutoLog;

public interface AlgaeIO {

	@AutoLog
	public static class AlgaeIOInputs {
		public double position = AlgaeConstants.PIVOT_HOME_POSITION;
		public double intakeCurrent = 0;
	}

	public default void updateInputs(AlgaeIOInputs inputs) {
	}

	public default void setPivotPosition(double wantedPosition) {
	}

	public default void intake() {
	}

	public default void eject() {
	}

	public default void stop() {
	}

}
