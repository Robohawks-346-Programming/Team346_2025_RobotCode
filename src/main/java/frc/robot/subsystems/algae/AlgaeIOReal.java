package frc.robot.subsystems.algae;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.util.Units;
import frc.robot.Constants;
import frc.robot.subsystems.algae.AlgaeConstants;
import frc.robot.subsystems.algae.AlgaeIO;

public class AlgaeIOReal implements AlgaeIO {
	private final TalonFX pivotMotor;
	private double targetPositionInRotations;
	private TalonFXConfiguration pivotMotorConfig;
	private MotionMagicExpoVoltage motionMagicVoltage;
	private MotionMagicConfigs motionMagicConfigs;
	private TalonFX algaeMotor;

	public AlgaeIOReal() {
		pivotMotor = new TalonFX(AlgaeConstants.PIVOT_MOTOR_ID);
		pivotMotorConfig = new TalonFXConfiguration();

		pivotMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
		pivotMotorConfig.Slot0.kP = AlgaeConstants.PIVOT_P;
		pivotMotorConfig.Slot0.kI = AlgaeConstants.PIVOT_I;
		pivotMotorConfig.Slot0.kD = AlgaeConstants.PIVOT_D;
		pivotMotorConfig.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
		pivotMotorConfig.Slot0.kG = AlgaeConstants.PIVOT_kG;
		pivotMotorConfig.Slot0.kA = AlgaeConstants.PIVOT_kA;
		pivotMotorConfig.Slot0.kV = AlgaeConstants.PIVOT_kV;
		pivotMotorConfig.Slot0.kS = AlgaeConstants.PIVOT_kS;
		pivotMotorConfig.Feedback.SensorToMechanismRatio = AlgaeConstants.PIVOT_GEAR_RATIO;

		pivotMotorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

		pivotMotorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
		pivotMotorConfig.CurrentLimits.StatorCurrentLimit = 150;

		motionMagicVoltage = new MotionMagicExpoVoltage(0);
		motionMagicVoltage.EnableFOC = true;

		motionMagicConfigs = pivotMotorConfig.MotionMagic;
		motionMagicConfigs.MotionMagicExpo_kA = 0.1;
		motionMagicConfigs.MotionMagicExpo_kV = AlgaeConstants.PIVOT_kV;

		pivotMotor.getConfigurator().apply(pivotMotorConfig);

		pivotMotor.setPosition(Units.degreesToRotations(AlgaeConstants.PIVOT_HOME_POSITION));

		targetPositionInRotations = 0.0;

		algaeMotor = new TalonFX(AlgaeConstants.ALGAE_MOTOR_ID);
	}

	@Override
	public void updateInputs(AlgaeIOInputs inputs) {
		inputs.position = Units.rotationsToDegrees(pivotMotor.getPosition().getValueAsDouble());
		inputs.intakeCurrent = algaeMotor.getStatorCurrent().getValueAsDouble();
	}

	@Override
	public void setPivotPosition(double wantedPosition) {
		pivotMotor.setControl(motionMagicVoltage.withPosition(Units.degreesToRotations(wantedPosition)));
	}

	@Override
	public void intake() {
		algaeMotor.setVoltage(-3.7);
	}

	@Override
	public void eject() {
		algaeMotor.setVoltage(4);
	}

	@Override
	public void stop() {
		algaeMotor.stopMotor();
	}

}
