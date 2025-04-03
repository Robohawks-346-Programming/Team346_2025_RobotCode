package frc.robot.subsystems.coral;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorConstants;
import frc.robot.subsystems.coral.CoralIOInputsAutoLogged;

public class Coral extends SubsystemBase {
	private CoralIO io;
	private CoralIOInputsAutoLogged inputs = new CoralIOInputsAutoLogged();
	private CoralState state = CoralState.IDLE;
	private boolean algaeDetected = false;
	private double currentThreshold = 5.0;
	private double delayTime = 0.5;
	private Timer intakeTimer = new Timer();
	private boolean wasIntaking = false;

	public Coral(CoralIO io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("coral", inputs);
		SmartDashboard.putBoolean("Intake", inputs.coralSensed);

		switch (state) {
			case INTAKE_CORAL:
				if (!inputs.coralSensed) {
					io.setSpeeds(0.7);
				} else {
					io.setSpeeds(0);
				}
				break;
			case EJECT_CORAL:
				io.setSpeeds(0.75);
				break;
			case STAGE:
				if (inputs.coralSensed) {
					io.setSpeeds(0.1);
				} else {
					io.setSpeeds(0);
				}
				break;
			case IDLE:
				io.setSpeeds(0);
				break;
		}

		Logger.recordOutput("coral/state", state);
		Logger.recordOutput("coral/coralMotorSpeed", inputs.coralMotorSpeed);
	}

	public enum CoralState {
		INTAKE_CORAL, EJECT_CORAL, STAGE, IDLE
	}

	public Command setState(CoralState m_state) {
		return Commands.runOnce(() -> this.state = m_state);
	}

	public void setStateNew(CoralState m_state) {
		this.state = m_state;
	}

	public void setintakeStateNonCommand(CoralState m_state) {
		this.state = m_state;
	}

	public Command runManual() {
		return Commands.runEnd(() -> io.setSpeeds(-.9), () -> io.setSpeeds(0));
	}

	public boolean isCoralDetected() {
		return inputs.coralSensed;
	}
}
