package frc.robot.subsystems.algae;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlgaeIOSim implements AlgaeIO {
	private double pivotPosition;

	public AlgaeIOSim() {
		pivotPosition = 0.0;
	}

	@Override
	public void updateInputs(AlgaeIOInputs inputs) {
		inputs.position = pivotPosition;
	}

	@Override
	public void setPivotPosition(double position) {
		pivotPosition = position;
	}
}
