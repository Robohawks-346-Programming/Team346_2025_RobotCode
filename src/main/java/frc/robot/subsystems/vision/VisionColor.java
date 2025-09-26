package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionColor extends SubsystemBase {
	public enum BallColor {
		RED, BLUE, NONE
	}

	private final NetworkTable limelight;
	private final String colorKey;

	public VisionColor(String tableName, String colorKey) {
		this.limelight = NetworkTableInstance.getDefault().getTable(tableName);
		this.colorKey = colorKey;
	}

	public BallColor getBallColor() {
		String val = limelight.getEntry(colorKey).getString("");
		if (val == null)
			return BallColor.NONE;
		val = val.toLowerCase();
		if (val.contains("red"))
			return BallColor.RED;
		if (val.contains("blue") || val.contains("lightblue") || val.contains("cyan"))
			return BallColor.BLUE;
		return BallColor.NONE;
	}
}
