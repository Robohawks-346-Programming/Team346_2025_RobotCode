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
	private CoralState state = CoralState.INTAKE_CORAL;
	private Elevator m_Elevator;
	private boolean algaeDetected = false;
	private double currentThreshold = 5.0;
	private double delayTime = 0.5;
	private Timer intakeTimer = new Timer();
	private boolean wasIntaking = false;

	public Coral(CoralIO io, Elevator elevator) {
		this.io = io;
		m_Elevator = elevator;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("Algae", inputs);
		Logger.processInputs("intake", inputs);
		SmartDashboard.putBoolean("Intake", inputs.coralSensed);

		switch (state) {
			case INTAKE_CORAL:
				if (!inputs.coralSensed) {
					io.setSpeeds(-0.25);
				} else {
					io.setSpeeds(0.0);
				}
				break;
			case EJECT_CORAL:
				if (m_Elevator.getTargetPose() == ElevatorConstants.LEVEL_4_POSITION
						|| m_Elevator.getTargetPose() == ElevatorConstants.LEVEL_1_POSITION) {
					io.setSpeeds(-0.9);
				} else {
					io.setSpeeds(-0.5);
				}
				break;
		}

		Logger.recordOutput("intake/state", state);
		Logger.recordOutput("intake/coralMotorSpeed", inputs.coralMotorSpeed);
	}

	public enum CoralState {
		INTAKE_CORAL, EJECT_CORAL
	}

	public Command setState(CoralState m_state) {
		return Commands.runOnce(() -> this.state = m_state);
	}

	public void setintakeStateNonCommand(CoralState m_state) {
		this.state = m_state;
	}

	public Command runManual() {
		return Commands.runEnd(() -> io.setSpeeds(-.9), () -> io.setSpeeds(0));
	}

	public Command intakeUntilAlgaeDetected(double delay) {
		io.setSpeeds(-0.5);

		if (!intakeTimer.isRunning() && inputs.coralMotorSpeed > 0 && !wasIntaking) {
			intakeTimer.reset();
			intakeTimer.start();
			wasIntaking = true;
		}

		if (intakeTimer.get() < delayTime) {
			return Commands.none();
		}

		double current = inputs.coralMotorSpeed;
		if (current > currentThreshold) {
			algaeDetected = true;
			io.setSpeeds(0.0);
			resetAlgaeDetection();
		}

		return Commands.none();
	}

	public void resetAlgaeDetection() {
		algaeDetected = false;
		intakeTimer.stop();
		intakeTimer.reset();
		wasIntaking = false;
	}

	public boolean isAlgaeDetected() {
		return algaeDetected;
	}
}
