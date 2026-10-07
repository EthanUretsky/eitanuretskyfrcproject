package frc.robot;

public final class Constants {
  public static final class shooterConstants {
    public static final double kPeakCurrentLimit = 80.0;
    public static final double kStatorCurrentLimit = 60.0;
    public static final double kGearRatio = 2.0/1.0;
  }
  public static final class feederConstants {
    public static final double kPeakCurrentLimit = 60.0;
    public static final double kStatorCurrentLimit = 30.0;
    public static final int kDIOSensor = 0;
    public static final double kGearRatio = 3.0/5.0;
  }
  public static final class hoodConstants {
    public static final double kPeakCurrentLimit = 30.0;
    public static final double kStatorCurrentLimit = 15.0;
    public static final double kGearRatio = 16.0;
    public static final double kP = 0.1;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
  }
}