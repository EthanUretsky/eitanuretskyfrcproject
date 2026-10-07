package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.commands.Commands;
import frc.robot.subsystems.feeder;
import frc.robot.subsystems.hood;
import frc.robot.subsystems.shooter;

public class RobotContainer {

  private final shooter shooter = new shooter();
  private final hood hood = new hood();
  private final feeder feeder = new feeder();
  private final CommandPS4Controller driverController = new CommandPS4Controller(0);

  private final Commands commands = new Commands(shooter, hood, feeder, driverController);

  public RobotContainer() {
    configureButtonBindings();
  }

  private void configureButtonBindings() {
    shooter.setDefaultCommand(commands);
  }
}