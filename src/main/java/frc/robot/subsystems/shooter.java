package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.StrictFollower;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.portMap;

public class shooter extends SubsystemBase {

    public enum shooterState {
        Idle,
        shooting,
        ballRemoving,
    }

    private shooterState state = shooterState.Idle;
    private final TalonFX mainMotor = new TalonFX(portMap.shooterConstants.mainMotorID);  
    private final TalonFX slaveMotor = new TalonFX(portMap.shooterConstants.slaveMotorID);
    private final VelocityVoltage velocityRequest = new VelocityVoltage(0);
    private StrictFollower follower;
    private final StatusSignal<AngularVelocity> velocitySignal = mainMotor.getVelocity();
    private final StatusSignal<Current> supplyCurrent = mainMotor.getSupplyCurrent();
    
    public shooter() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = Constants.shooterConstants.kPeakCurrentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.CurrentLimits.StatorCurrentLimit = Constants.shooterConstants.kStatorCurrentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.Feedback.gearRatio = Constants.shooterConstants.kGearRatio;
        
        mainMotor.getConfigurator().apply(config);
        slaveMotor.getConfigurator().apply(config);
        
        supplyCurrent.setUpdateFrequency(50);
        velocitySignal.setUpdateFrequency(50);
        
        follower = new StrictFollower(mainMotor.getDeviceID());
        slaveMotor.setControl(follower);
    }

    public void setMotorVelocity(double velocity) {
        mainMotor.setControl(velocityRequest.withVelocity(velocity));
    }

    public void runStates() {
        switch (state) {
            case Idle: 
                setMotorVelocity(0);
                break;
            case shooting:
                setMotorVelocity(4000);
                break;
            case ballRemoving:
                setMotorVelocity(-4000);
                break;
        }
    }

    public shooterState setState(shooterState newState) {
        state = newState;
        return state;
    }

    public shooterState getState() {
        return state;
    }

    public double getVelocitySignal() {
        return velocitySignal.getValueAsDouble();
    }

    @Override
    public void periodic() {
        supplyCurrent.refresh();
        velocitySignal.refresh();
        runStates();
    }
}