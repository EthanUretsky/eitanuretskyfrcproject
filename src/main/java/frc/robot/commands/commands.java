package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.subsystems.shooter;
import frc.robot.subsystems.hood;
import frc.robot.subsystems.feeder;
import frc.robot.subsystems.shooter.shooterState;
import frc.robot.subsystems.hood.hoodState;
import frc.robot.subsystems.feeder.feederState;

public class commands extends Command {

    public enum robotState {
        Idle,
        Intake,
        Shooting,
        Hold,
        BallRemoving,
        OpenWalls
    }

    private robotState state = robotState.Idle;
    private final shooter shooter;
    private final hood hood;
    private final feeder feeder;
    private final CommandPS4Controller driverController;

    public commands(shooter shooter, hood hood, feeder feeder, CommandPS4Controller controller) {
        this.shooter = shooter;
        this.hood = hood;
        this.feeder = feeder;
        this.driverController = controller;

        addRequirements(shooter, hood, feeder);
    }

    @Override
    public void initialize() {
        state = robotState.Idle;
    }

   @Override
    public void execute() {
        if (driverController.triangle().getAsBoolean() && !hasBall()) {
            state = robotState.Intake;
        } else if (driverController.circle().getAsBoolean() && checkAlignment()) {
            state = robotState.Shooting;
        } else if (driverController.cross().getAsBoolean() && feeder.isThereBall()) {
            state = robotState.BallRemoving;
        } else if (driverController.l1().getAsBoolean()) {
            state = robotState.OpenWalls;
        } else if (driverController.touchpad().getAsBoolean()) {
            state = robotState.Idle;
        }

        if (state == robotState.Intake && feeder.isThereBall()) {
            state = robotState.Hold;
        }

        updateSubsystemStates();
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setMotorVelocity(0);
        feeder.setMotorOutput(0);
        hood.setHoodPosition(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void updateSubsystemStates() {
        switch (state) {
            case Idle:
                shooter.setState(shooterState.Idle);
                feeder.setState(feederState.Idle);
                hood.setState(hoodState.Idle);
                break;

            case OpenWalls:
                shooter.setState(shooterState.Idle);
                feeder.setState(feederState.Idle);
                hood.setState(hoodState.Idle);
                break;

            case Intake:
                shooter.setState(shooterState.Idle);
                feeder.setState(feederState.Forward);
                hood.setState(hoodState.Idle);
                break;

            case Hold:
                shooter.setState(shooterState.Idle);
                feeder.setState(feederState.Idle);
                hood.setState(hoodState.Idle);
                break;

            case Shooting:
                shooter.setState(shooterState.shooting);
                feeder.setState(feederState.Forward);
                hood.setState(hoodState.High);
                break;

            case BallRemoving:
                shooter.setState(shooterState.ballRemoving);
                feeder.setState(feederState.Backward);
                hood.setState(hoodState.Idle);
                break;
        }
    }

    public boolean checkAlignment() {
        return true;
    }

    public boolean hasBall() {
        return feeder.isThereBall();
    }
}