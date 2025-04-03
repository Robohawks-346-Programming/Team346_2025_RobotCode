package frc.robot.subsystems.climb;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.algae.AlgaeConstants;

import java.net.http.HttpResponse.PushPromiseHandler;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.mechanisms.swerve.utility.LegacyPhoenixPIDController;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ClimbIOReal extends SubsystemBase implements ClimbIO {

	public final TalonFX climbMotor;
	private double targetPositionInRotations;
	private TalonFXConfiguration climbMotorConfig;
	private PositionVoltage motionMagicVoltage;
	private MotionMagicConfigs motionMagicConfigs;
	private final Servo rightServo;
	private final Servo leftServo;

	public ClimbIOReal() {
		climbMotor = new TalonFX(ClimbConstants.CLIMB_MOTOR_ID);
		climbMotorConfig = new TalonFXConfiguration();

		climbMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
		climbMotorConfig.Slot0.kP = ClimbConstants.CLIMB_P;
		climbMotorConfig.Slot0.kI = ClimbConstants.CLIMB_I;
		climbMotorConfig.Slot0.kD = ClimbConstants.CLIMB_D;
		climbMotorConfig.Slot0.kG = ClimbConstants.CLIMB_kG;
		climbMotorConfig.Slot0.kA = ClimbConstants.CLIMB_kA;
		climbMotorConfig.Slot0.kV = ClimbConstants.CLIMB_kV;
		climbMotorConfig.Slot0.kS = ClimbConstants.CLIMB_kS;

		climbMotorConfig.Feedback.SensorToMechanismRatio = ClimbConstants.CLIMB_GEAR_RATIO;

		motionMagicVoltage = new PositionVoltage(0);
		motionMagicVoltage.EnableFOC = true;

		motionMagicConfigs = climbMotorConfig.MotionMagic;
		motionMagicConfigs.MotionMagicExpo_kA = 1;
		motionMagicConfigs.MotionMagicExpo_kV = ClimbConstants.CLIMB_kV;

		climbMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

		climbMotor.getConfigurator().apply(climbMotorConfig);

		climbMotor.setPosition(ClimbConstants.CLIMB_HOME_POSITION);

		targetPositionInRotations = 0.0;

		rightServo = new Servo(ClimbConstants.SERVO_RIGHT);
		leftServo = new Servo(ClimbConstants.SERVO_LEFT);

	}

	@Override
	public void updateInputs(ClimbIOInputs inputs) {
		inputs.position = Units.rotationsToDegrees(climbMotor.getPosition().getValueAsDouble());
		inputs.servoLeftPos = leftServo.getAngle();
		inputs.servoRightPose = rightServo.getAngle();
	}

	@Override
	public void setClimbPosition(double wantedPosition) {
		climbMotor.setControl(motionMagicVoltage.withPosition(Units.degreesToRotations(wantedPosition)));
	}

	@Override
	public void runManualUp() {
		climbMotor.set(0.8);
	}

	@Override
	public void stop() {
		climbMotor.set(0);

	}

	@Override
	public void runManualDown() {
		climbMotor.set(-0.8);

	}

	@Override
	public void setFunnel(double proportion) {
		int pulseTime = (int) (proportion * 2000.0) + 500;
		System.out.println("Setting pulse time " + pulseTime);
		leftServo.setPulseTimeMicroseconds(pulseTime);
		rightServo.setPulseTimeMicroseconds(3000 - pulseTime);

	}

	@Override
	public void resetFunnel() {
		leftServo.setZeroLatch();
		rightServo.setZeroLatch();

		leftServo.setAlwaysHighMode();
		rightServo.setAlwaysHighMode();
	}
}
