package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.portMap;

public class feeder extends SubsystemBase {

  public enum feederState {
     Idle,
     Forward,
     Backward,
  }
  
  private feederState state = feederState.Idle;
  private final TalonFX feederMotor = new TalonFX(portMap.feeder.feederMotorID);  
  private final DutyCycleOut dutyCycleRequest = new DutyCycleOut(0);
  private final DigitalInput ballSensor = new DigitalInput(Constants.feederConstants.kDIOSensor);
  private final StatusSignal<Current> supplyCurrent = feederMotor.getSupplyCurrent();
  
  public feeder() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = Constants.feederConstants.kPeakCurrentLimit;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = Constants.feederConstants.kStatorCurrentLimit;
    config.CurrentLimits.StatorCurrentLimitEnable = true;    
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.Feedback.SensorToMechanismRatio = Constants.feederConstants.kGearRatio;
    feederMotor.getConfigurator().apply(config);
    supplyCurrent.setUpdateFrequency(50);
  }

  public void setMotorOutput(double output) {
      feederMotor.setControl(dutyCycleRequest.withOutput(output));
  }

  public void runStates(){
   switch (state){
      case Idle: 
         setMotorOutput(0);
         break;

      case Forward:
         setMotorOutput(0.5);
         break;

      case Backward:
         setMotorOutput(-0.5);
         break;
   }
  }

  public feederState setState(feederState newState){
     state = newState;
     return state;
  }

  public feederState getState(){
     return state;
  }

  public boolean isThereBall() {
     return ballSensor.get();
  }

  @Override
  public void periodic() {
     supplyCurrent.refresh();
     runStates(); 
  }
}