package frc.robot.subsystems.ball;

import com.ctre.phoenix6.hardware.TalonFX;

public class BallIOReal implements BallIO {
	private final TalonFX left;
	private final TalonFX right;

	public BallIOReal(int leftId, int rightId) {
		this.left = new TalonFX(leftId);
		this.right = new TalonFX(rightId);
	}

	@Override
	public void updateInputs(BallIOInputs inputs) {
		inputs.leftMotorSpeed = left.get();
		inputs.rightMotorSpeed = right.get();
	}

	@Override
	public void intake() {
		left.set(-0.85);
		right.set(0.85);
	}

	@Override
	public void outtake() {
		left.set(0.9);
		right.set(-0.9);
	}

	@Override
	public void stop() {
		left.set(0);
		right.set(0);
	}
}
