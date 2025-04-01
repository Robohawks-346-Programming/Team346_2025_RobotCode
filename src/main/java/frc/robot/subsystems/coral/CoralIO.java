package frc.robot.subsystems.coral;

import org.littletonrobotics.junction.AutoLog;

public interface CoralIO {

	@AutoLog
	public static class CoralIOInputs {
		public double coralMotorSpeed = 0.0;

		public boolean coralSensed = false;
	}

	public default void updateInputs(CoralIOInputs inputs) {
	}

	public default void setSpeeds(double speed1) {
	}

}
