// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.subsystems.drive.Drive;
// import frc.robot.subsystems.elevator.Elevator;
// import frc.robot.subsystems.algae.Algae;
// import org.littletonrobotics.junction.Logger;

// //some fixes needed
// public class BranchLevelSelector extends SubsystemBase {
// private int currentBranch = 1;
// private int currentLevel = 1;
// private final Drive drive;
// private final Elevator elevator;
// private final Algae algae;

// public BranchLevelSelector(Drive drive, Elevator elevator, Algae algae) {
// this.drive = drive;
// this.elevator = elevator;
// this.algae = algae;
// }

// @Override
// public void periodic() {
// Logger.recordOutput("BranchLevelSelector/CurrentBranch", currentBranch);
// Logger.recordOutput("BranchLevelSelector/CurrentLevel", currentLevel);
// }

// public void incrementBranch() {
// currentBranch = (currentBranch % 12) + 1;
// Logger.recordOutput("BranchLevelSelector/CurrentBranch", currentBranch);
// }

// public void decrementBranch() {
// currentBranch = (currentBranch - 2 + 12) % 12 + 1;
// Logger.recordOutput("BranchLevelSelector/CurrentBranch", currentBranch);
// }

// public void incrementLevel() {
// currentLevel = (currentLevel % 4) + 1;
// Logger.recordOutput("BranchLevelSelector/CurrentLevel", currentLevel);
// }

// public void decrementLevel() {
// currentLevel = (currentLevel - 2 + 4) % 4 + 1;
// Logger.recordOutput("BranchLevelSelector/CurrentLevel", currentLevel);
// }

// public int getCurrentBranch() {
// return currentBranch;
// }

// public int getCurrentLevel() {
// return currentLevel;
// }

// public Command getScoreCommand() {
// return new BranchLevelScoreCommand(drive, elevator, algae, currentBranch,
// currentLevel);
// }
// }