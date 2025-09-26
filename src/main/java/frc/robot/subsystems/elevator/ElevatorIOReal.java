package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;

public class ElevatorIOReal implements ElevatorIO {
	private final TalonFX elevatorMotorLEFT;
	private final TalonFX elevatorMotorRIGHT;
	private double targetPositionInRotations;
	private TalonFXConfiguration elevatorMotorsConfig;
	private MotionMagicExpoVoltage motionMagicVoltage;
	private Follower follower;
	private MotionMagicConfigs motionMagicConfigs;

	public ElevatorIOReal() {
		elevatorMotorLEFT = new TalonFX(ElevatorConstants.MOTOR_ELEVATOR_LEFT);
		elevatorMotorRIGHT = new TalonFX(ElevatorConstants.MOTOR_ELEVATOR_RIGHT);
		elevatorMotorsConfig = new TalonFXConfiguration();

		elevatorMotorsConfig.Slot0.kP = ElevatorConstants.ELEVATOR_P;
		elevatorMotorsConfig.Slot0.kI = ElevatorConstants.ELEVATOR_I;
		elevatorMotorsConfig.Slot0.kD = ElevatorConstants.ELEVATOR_D;
		elevatorMotorsConfig.Slot0.GravityType = GravityTypeValue.Elevator_Static;
		elevatorMotorsConfig.Slot0.kG = ElevatorConstants.ELEVATOR_kG;
		elevatorMotorsConfig.Slot0.kA = ElevatorConstants.ELEVATOR_kA;
		elevatorMotorsConfig.Slot0.kV = ElevatorConstants.ELEVATOR_kV;
		elevatorMotorsConfig.Slot0.kS = ElevatorConstants.ELEVATOR_kS;

		elevatorMotorsConfig.CurrentLimits.StatorCurrentLimitEnable = true;
		elevatorMotorsConfig.CurrentLimits.StatorCurrentLimit = 70;

		elevatorMotorsConfig.Feedback.SensorToMechanismRatio = ElevatorConstants.ELEVATOR_GEAR_RATIO;

		elevatorMotorsConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

		motionMagicVoltage = new MotionMagicExpoVoltage(0);
		motionMagicVoltage.EnableFOC = true;
		motionMagicVoltage.withSlot(0);

		motionMagicConfigs = elevatorMotorsConfig.MotionMagic;
		motionMagicConfigs.MotionMagicExpo_kA = 0.55;
		motionMagicConfigs.MotionMagicExpo_kV = 0.12;

		elevatorMotorsConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

		elevatorMotorLEFT.getConfigurator().apply(elevatorMotorsConfig);

		elevatorMotorsConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
		elevatorMotorRIGHT.getConfigurator().apply(elevatorMotorsConfig);

		elevatorMotorLEFT.setPosition(ElevatorConstants.ELEVATOR_HOME_POSITION);
		elevatorMotorRIGHT.setPosition(ElevatorConstants.ELEVATOR_HOME_POSITION);

		follower = new Follower(ElevatorConstants.MOTOR_ELEVATOR_RIGHT, true);

		targetPositionInRotations = 0.0;
	}

	@Override
	public void updateInputs(ElevatorIOInputsAutoLogged inputs) {
		double leftPosition = elevatorMotorLEFT.getPosition().getValueAsDouble();
		double rightPosition = elevatorMotorRIGHT.getPosition().getValueAsDouble();

		inputs.position = ((leftPosition + rightPosition) / 2)
				* (Math.PI * ElevatorConstants.ELEVATOR_SPOOL_DIAMETER);
	}

	@Override
	public void setElevatorPosition(double wantedPosition) {
		wantedPosition = MathUtil.clamp(wantedPosition, 0, ElevatorConstants.LEVEL_4_POSITION);
		wantedPosition /= (Math.PI * ElevatorConstants.ELEVATOR_SPOOL_DIAMETER);
		elevatorMotorRIGHT.setControl(motionMagicVoltage.withPosition(wantedPosition));
		elevatorMotorLEFT.setControl(follower);
	}

	@Override
	public void runManualUp() {
		elevatorMotorLEFT.set(0.2);
		elevatorMotorRIGHT.set(0.2);
	}

	@Override
	public void stop() {
		elevatorMotorLEFT.set(0);
		elevatorMotorRIGHT.set(0);
	}

	@Override
	public void runManualDown() {
		elevatorMotorLEFT.set(-0.2);
		elevatorMotorRIGHT.set(-0.2);
	}

	@Override
	public void reset() {
		elevatorMotorLEFT.setPosition(0);
		elevatorMotorRIGHT.setPosition(0);
	}
}
