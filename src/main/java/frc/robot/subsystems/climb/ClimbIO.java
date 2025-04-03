package frc.robot.subsystems.climb;

import org.littletonrobotics.junction.AutoLog;

public interface ClimbIO {

	@AutoLog
	public static class ClimbIOInputs {
		public double position = ClimbConstants.CLIMB_HOME_POSITION;
		public double servoLeftPos = 0;
		public double servoRightPose = 0;

	}

	public default void updateInputs(ClimbIOInputs inputs) {
	}

	public default void setClimbPosition(double wantedPosition) {
	}

	public default void runManualUp() {
	}

	public default void runManualDown() {
	}

	public default void setFunnel(double t) {
	}

	public default void stop() {
	}

	public default void resetFunnel() {
	}
}
