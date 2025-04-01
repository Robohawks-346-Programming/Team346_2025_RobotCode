package frc.robot.subsystems.coral;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class CoralIOReal implements CoralIO {
	private final SparkMax coralMotor;
	private final SparkMaxConfig config;

	private static DigitalInput laserBreak;

	public CoralIOReal() {
		coralMotor = new SparkMax(CoralConstants.CORAL_MOTOR_ID, MotorType.kBrushless);
		config = new SparkMaxConfig();

		laserBreak = new DigitalInput(CoralConstants.CORAL_LASER_BREAK_PORT);

		config.smartCurrentLimit(30);
		config.idleMode(IdleMode.kBrake);
		coralMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
	}

	@Override
	public void updateInputs(CoralIOInputs inputs) {
		inputs.coralMotorSpeed = coralMotor.get();

		inputs.coralSensed = !laserBreak.get();
	}

	@Override
	public void setSpeeds(double coralSpeed) {
		coralMotor.set(coralSpeed);
	}

	public static boolean getLaserBreak() {
		return !laserBreak.get();
	}
}
