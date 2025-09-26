package frc.robot.subsystems.ball;

public class BallIOSim implements BallIO {
	private double left = 0.0;
	private double right = 0.0;

	@Override
	public void updateInputs(BallIOInputs inputs) {
		inputs.leftMotorSpeed = left;
		inputs.rightMotorSpeed = right;
	}

	@Override
	public void intake() {
		left = -0.85;
		right = 0.85;
	}

	@Override
	public void outtake() {
		left = 0.9;
		right = -0.9;
	}

	@Override
	public void stop() {
		left = 0.0;
		right = 0.0;
	}
}
