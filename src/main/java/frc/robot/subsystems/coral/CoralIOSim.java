package frc.robot.subsystems.coral;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.coral.CoralConstants;

public class CoralIOSim implements CoralIO {
	private double coralMotorSpeed;

	public CoralIOSim() {
		coralMotorSpeed = 0.0;

		SmartDashboard.putBoolean("Coral In", false);
	}

	@Override
	public void updateInputs(CoralIOInputs inputs) {
		inputs.coralMotorSpeed = coralMotorSpeed;

		inputs.coralSensed = SmartDashboard.getBoolean("Coral In", false);
	}

	@Override
	public void setSpeeds(double coralSpeed) {
		coralMotorSpeed = coralSpeed;
	}
}
