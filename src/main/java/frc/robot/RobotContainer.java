package frc.robot;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.fasterxml.jackson.databind.util.Named;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AkitDriveCommands;
import frc.robot.commands.AlgaeIntake;
import frc.robot.commands.AutoAlgaeAlign;
import frc.robot.commands.ReefBranchAlign;
import frc.robot.subsystems.algae.Algae;
import frc.robot.subsystems.algae.AlgaeConstants;
import frc.robot.subsystems.algae.AlgaeIO;
import frc.robot.subsystems.algae.AlgaeIOReal;
import frc.robot.subsystems.algae.AlgaeIOSim;
import frc.robot.subsystems.algae.Algae.AlgaeState;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.Climb.ClimbState;
import frc.robot.subsystems.coral.Coral;
import frc.robot.subsystems.coral.CoralIOReal;
import frc.robot.subsystems.coral.CoralIOSim;
import frc.robot.subsystems.coral.Coral.CoralState;
import frc.robot.subsystems.coral.CoralIO;
import frc.robot.subsystems.climb.ClimbIO;
import frc.robot.subsystems.climb.ClimbIOReal;
import frc.robot.subsystems.climb.ClimbIOSim;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.drive.TunerConstants;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.Elevator.ElevatorState;
import frc.robot.subsystems.elevator.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorIO;
import frc.robot.subsystems.elevator.ElevatorIOReal;
import frc.robot.subsystems.elevator.ElevatorIOSim;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOPhotonReal;
import frc.robot.subsystems.vision.VisionIOPhotonSim;
import frc.robot.subsystems.vision.VisionLocalizer;

public class RobotContainer {
	private final Drive drive;
	private final Climb climb;
	private final Elevator elevator;
	private final Coral coral;
	private final Algae algae;
	private final VisionLocalizer vision;

	private final CommandXboxController controller = new CommandXboxController(0);
	private final CommandXboxController controller2 = new CommandXboxController(2);

	public static final Joystick operatorControl = new Joystick(Constants.OperatorConstants.OPERATOR_CONTROLLER_PORT);
	private final LoggedDashboardChooser<Command> autoChooser;

	public static final JoystickButton BUTTON_1 = new JoystickButton(operatorControl, 1),
			BUTTON_2 = new JoystickButton(operatorControl, 2),
			BUTTON_3 = new JoystickButton(operatorControl, 3),
			BUTTON_4 = new JoystickButton(operatorControl, 4),
			BUTTON_5 = new JoystickButton(operatorControl, 5),
			BUTTON_6 = new JoystickButton(operatorControl, 6),
			BUTTON_7 = new JoystickButton(operatorControl, 7),
			BUTTON_8 = new JoystickButton(operatorControl, 8),
			BUTTON_9 = new JoystickButton(operatorControl, 9),
			BUTTON_10 = new JoystickButton(operatorControl, 10),
			BUTTON_11 = new JoystickButton(operatorControl, 11),
			BUTTON_12 = new JoystickButton(operatorControl, 12),
			BUTTON_13 = new JoystickButton(operatorControl, 13),
			BUTTON_14 = new JoystickButton(operatorControl, 14),
			BUTTON_15 = new JoystickButton(operatorControl, 15),
			BUTTON_16 = new JoystickButton(operatorControl, 16);

	public RobotContainer() {
		switch (Constants.currentMode) {
			case REAL:
				drive = new Drive(
						new GyroIOPigeon2(),
						new ModuleIOTalonFX(TunerConstants.FrontLeft),
						new ModuleIOTalonFX(TunerConstants.FrontRight),
						new ModuleIOTalonFX(TunerConstants.BackLeft),
						new ModuleIOTalonFX(TunerConstants.BackRight));
				climb = new Climb(new ClimbIOReal());
				elevator = new Elevator(new ElevatorIOReal());
				coral = new Coral(new CoralIOReal());
				algae = new Algae(new AlgaeIOReal());
				vision = new VisionLocalizer(drive::addVisionMeasurement, drive,
						new VisionIOPhotonReal(VisionConstants.cameraNames[0],
								VisionConstants.vehicleToCameras[0]),
						new VisionIOPhotonReal(VisionConstants.cameraNames[1],
								VisionConstants.vehicleToCameras[1]),
						new VisionIOPhotonReal(VisionConstants.cameraNames[2],
								VisionConstants.vehicleToCameras[2]),
						new VisionIOPhotonReal(VisionConstants.cameraNames[3], VisionConstants.vehicleToCameras[3]));
				break;

			case SIM:
				drive = new Drive(
						new GyroIO() {
						},
						new ModuleIOSim(TunerConstants.FrontLeft),
						new ModuleIOSim(TunerConstants.FrontRight),
						new ModuleIOSim(TunerConstants.BackLeft),
						new ModuleIOSim(TunerConstants.BackRight));
				climb = new Climb(new ClimbIOSim());
				elevator = new Elevator(new ElevatorIOSim());
				coral = new Coral(new CoralIOSim());
				algae = new Algae(new AlgaeIOSim());
				vision = new VisionLocalizer(
						drive::addVisionMeasurement,
						drive,
						new VisionIOPhotonSim(VisionConstants.cameraNames[0],
								VisionConstants.vehicleToCameras[0],
								drive::getPose),
						new VisionIOPhotonSim(VisionConstants.cameraNames[1],
								VisionConstants.vehicleToCameras[1], drive::getPose),
						new VisionIOPhotonSim(VisionConstants.cameraNames[2],
								VisionConstants.vehicleToCameras[2], drive::getPose),
						new VisionIOPhotonSim(VisionConstants.cameraNames[3],
								VisionConstants.vehicleToCameras[3], drive::getPose));

				break;

			default:
				drive = new Drive(
						new GyroIO() {
						},
						new ModuleIO() {
						},
						new ModuleIO() {
						},
						new ModuleIO() {
						},
						new ModuleIO() {
						});
				climb = new Climb(new ClimbIO() {
				});
				elevator = new Elevator(new ElevatorIO() {
				});
				coral = new Coral(new CoralIO() {
				});
				algae = new Algae(new AlgaeIO() {
				});
				vision = new VisionLocalizer(drive::addVisionMeasurement, drive, new VisionIO() {
				});
		}

		vision.setVisionConsumer(drive::addVisionMeasurement);

		NamedCommands.registerCommand("L4",
				Commands.sequence(
						elevator.setTargetPos(ElevatorConstants.LEVEL_4_POSITION),
						Commands.waitSeconds(0.75),
						Commands.waitSeconds(0.25),
						coral.setState(CoralState.EJECT_CORAL),
						Commands.waitSeconds(.25),
						Commands.parallel(
								elevator.setTargetPos(ElevatorConstants.ELEVATOR_HOME_POSITION),
								coral.setState(CoralState.INTAKE_CORAL)),
						Commands.waitSeconds(0.5)));
		NamedCommands.registerCommand("Algae Low",
				Commands.sequence(
						Commands.parallel(
								elevator.setTargetPos(ElevatorConstants.ALGAE_LOW),
								Commands.waitSeconds(0.5)),
						algae.setState(AlgaeState.REEF_CLEAR)));
		NamedCommands.registerCommand("Intake", Commands.race(
				coral.setState(CoralState.INTAKE_CORAL),
				Commands.waitSeconds(0.5)));
		NamedCommands.registerCommand("Algae Home", Commands.parallel(
				elevator.setTargetPos(ElevatorConstants.ELEVATOR_HOME_POSITION)));
		NamedCommands.registerCommand("Algae Score",
				Commands.sequence(
						algae.setState(AlgaeState.PROCESSOR),
						Commands.waitSeconds(1),
						algae.setState(AlgaeState.IDLE),
						Commands.waitSeconds(1)));
		NamedCommands.registerCommand("Align Right", Commands.race(
				new ReefBranchAlign(drive,
						new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(-6.5),
								new Rotation2d()),
						() -> controller.getLeftY()),
				Commands.waitSeconds(1)));

		NamedCommands.registerCommand("Align Left", Commands.race(
				new ReefBranchAlign(drive,
						new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(6.5),
								new Rotation2d()),
						() -> controller.getLeftY()),
				Commands.waitSeconds(1)));
		NamedCommands.registerCommand("Align Right and L4", Commands.parallel(
				new ReefBranchAlign(drive,
						new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(-6.5),
								new Rotation2d()),
						() -> controller.getLeftY()),
				elevator.setState(ElevatorState.LEVEL_4_POSITION)));
		NamedCommands.registerCommand("Align Left and L4", Commands.parallel(
				new ReefBranchAlign(drive,
						new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(6.5),
								new Rotation2d()),
						() -> controller.getLeftY()),
				elevator.setState(ElevatorState.LEVEL_4_POSITION)));

		NamedCommands.registerCommand("INSANE L4", Commands.runOnce(() -> System.out.println()));

		NamedCommands.registerCommand("Auto Score Right",
				Commands.sequence(
						elevator.setState(ElevatorState.LEVEL_4_POSITION),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-15), Units.inchesToMeters(-4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						coral.setState(CoralState.EJECT_CORAL),
						Commands.waitSeconds(0.5),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION),
						coral.setState(CoralState.INTAKE_CORAL),
						Commands.waitSeconds(0.5)));
		NamedCommands.registerCommand("Auto Score Left",
				Commands.sequence(
						elevator.setState(ElevatorState.LEVEL_4_POSITION),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-15), Units.inchesToMeters(4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						coral.setState(CoralState.EJECT_CORAL),
						Commands.waitSeconds(0.5),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION),
						coral.setState(CoralState.INTAKE_CORAL),
						Commands.waitSeconds(0.5)));
		// NamedCommands.registerCommand("Net",
		// Commands.sequence(
		// elevator.setState(ElevatorState.LEVEL_4_POSITION),
		// Commands.waitSeconds(1),
		// algae.setState(AlgaeState.NET),
		// Commands.waitSeconds(0.25),
		// algae.setState(AlgaeState.EJECT),
		// Commands.waitSeconds(0.5),
		// algae.setState(AlgaeState.IDLE),
		// Commands.waitSeconds(0.5),
		// elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION),
		// Commands.waitSeconds(0.5)));

		NamedCommands.registerCommand("Net",
				Commands.runOnce(() -> System.out.println()));

		NamedCommands.registerCommand("Algae Clear",
				Commands.sequence(
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(0),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						Commands.waitSeconds(0.5),
						new AutoAlgaeAlign(drive, elevator, new Transform2d()),
						algae.setState(AlgaeState.REEF_CLEAR),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(0),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						new AlgaeIntake(algae),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(0), // Backup before
																									// bringing algae
																									// down
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION)));

		autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

		autoChooser.addOption("Drive Wheel Radius Characterization",
				AkitDriveCommands.wheelRadiusCharacterization(drive));
		autoChooser.addOption("Drive Simple FF Characterization", AkitDriveCommands.feedforwardCharacterization(drive));

		configureButtonBindings();
	}

	private void configureButtonBindings() {
		drive.setDefaultCommand(
				AkitDriveCommands.joystickDrive(
						drive,
						() -> -controller.getLeftY(),
						() -> -controller.getLeftX(),
						() -> -controller.getRightX()));
		controller
				.rightBumper()
				.onTrue(
						Commands.runOnce(
								() -> drive.setPose(
										new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
								drive)
								.ignoringDisable(true));

		BUTTON_1.onTrue(elevator.setState(ElevatorState.LEVEL_1_POSITION));
		BUTTON_2.onTrue(elevator.setState(ElevatorState.LEVEL_2_POSITION));
		BUTTON_3.onTrue(elevator.setState(ElevatorState.LEVEL_3_POSITION));
		BUTTON_4.onTrue(elevator.setState(ElevatorState.LEVEL_4_POSITION));

		BUTTON_8.onTrue(elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION));

		BUTTON_15.onTrue(
				Commands.sequence(
						elevator.setState(ElevatorState.LEVEL_4_POSITION),
						Commands.waitSeconds(1),
						algae.setState(AlgaeState.NET)));

		BUTTON_16.onTrue(Commands.sequence(
				algae.setState(AlgaeState.ALGAE_HOLD),
				Commands.waitSeconds(0.5),
				elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION)));

		BUTTON_9.whileTrue(algae.setState(AlgaeState.GROUND_INTAKE)).whileFalse(algae.setState(AlgaeState.IDLE));
		BUTTON_10.whileTrue(algae.setState(AlgaeState.PROCESSOR)).whileFalse(algae.setState(AlgaeState.IDLE));
		BUTTON_11.onTrue(
				Commands.sequence(
						elevator.setTargetPos(ElevatorConstants.ALGAE_LOW),
						Commands.waitSeconds(0.5),
						algae.setState(AlgaeState.REEF_CLEAR)));
		BUTTON_12.onTrue(
				Commands.sequence(
						elevator.setTargetPos(ElevatorConstants.ALGAE_HIGH),
						Commands.waitSeconds(0.5),
						algae.setState(AlgaeState.REEF_CLEAR)));

		BUTTON_13.whileTrue(climb.runUp()).whileFalse(climb.stop());

		BUTTON_14.whileTrue(climb.runDown()).whileFalse(climb.stop());

		BUTTON_5.whileTrue(coral.setState(CoralState.INTAKE_CORAL)).onFalse(coral.setState(CoralState.IDLE));
		BUTTON_6.whileTrue(coral.setState(CoralState.EJECT_CORAL)).onFalse(coral.setState(CoralState.IDLE));
		BUTTON_7.whileTrue(algae.setState(AlgaeState.EJECT)).onFalse(coral.setState(CoralState.IDLE));

		controller.rightTrigger().onTrue(
				Commands.sequence(
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(-4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						elevator.autoScore(),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-16), Units.inchesToMeters(-4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						coral.setState(CoralState.EJECT_CORAL),
						Commands.waitSeconds(0.5),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION),
						coral.setState(CoralState.INTAKE_CORAL)));

		controller.leftTrigger().onTrue(
				Commands.sequence(
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						elevator.autoScore(),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-16), Units.inchesToMeters(4),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						coral.setState(CoralState.EJECT_CORAL),
						Commands.waitSeconds(0.5),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION),
						coral.setState(CoralState.INTAKE_CORAL)));

		controller.rightBumper().onTrue(
				Commands.sequence(
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(0),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						Commands.waitSeconds(0.5),
						new AutoAlgaeAlign(drive, elevator, new Transform2d()),
						algae.setState(AlgaeState.REEF_CLEAR),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-16.5), Units.inchesToMeters(0),
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						new AlgaeIntake(algae),
						new ReefBranchAlign(drive,
								new Transform2d(Units.inchesToMeters(-30), Units.inchesToMeters(0), // Backup before
																									// bringing algae
																									// down
										new Rotation2d(Math.PI)),
								() -> controller.getLeftY()),
						elevator.setState(ElevatorState.ELEVATOR_HOME_POSITION)));

		controller.leftBumper().onTrue(
				AkitDriveCommands.joystickDrive(
						drive,
						() -> -controller.getLeftY(),
						() -> -controller.getLeftX(),
						() -> -controller.getRightX()));

		controller2.x().onTrue(
				elevator.setAutoScore(ElevatorState.LEVEL_1_POSITION));

		controller2.y().onTrue(
				elevator.setAutoScore(ElevatorState.LEVEL_2_POSITION));

		controller2.b().onTrue(
				elevator.setAutoScore(ElevatorState.LEVEL_3_POSITION));

		controller2.a().onTrue(
				elevator.setAutoScore(ElevatorState.LEVEL_4_POSITION));

		controller2.rightBumper().onTrue(
				Commands.sequence(
						climb.setState(ClimbState.SERVO_DEPLOY),
						Commands.waitSeconds(1),
						climb.setState(ClimbState.CLIMB_OUT)));
		controller2.leftBumper().onTrue(
				Commands.sequence(
						algae.setState(AlgaeState.CLIMB),
						climb.setState(ClimbState.CLIMB_IN)));
	}

	public Command getAutonomousCommand() {
		return autoChooser.get();
	}

}
