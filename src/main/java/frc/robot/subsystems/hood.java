package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.portMap;

public class hood extends SubsystemBase {

  public enum hoodState {
     Idle,
     Low,
     High,
  }
  
  private hoodState state = hoodState.Idle;
  private final TalonFX hoodMotor = new TalonFX(portMap.hoodConstants.hoodMotorID);  
  private final CANcoder hoodEncoder = new CANcoder(portMap.hoodConstants.hoodEncoderID);
  private final PositionVoltage positionRequest = new PositionVoltage(0);
  private final StatusSignal<Current> supplyCurrent = hoodMotor.getSupplyCurrent();
  private final StatusSignal<Angle> positionSignal = hoodMotor.getPosition();
  
  public hood() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = Constants.hoodConstants.kPeakCurrentLimit;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = Constants.hoodConstants.kStatorCurrentLimit;
    config.CurrentLimits.StatorCurrentLimitEnable = true;    
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.Feedback.SensorToMechanismRatio = Constants.hoodConstants.kGearRatio;
    config.Slot0.kP = Constants.hoodConstants.kP;
    config.Slot0.kI = Constants.hoodConstants.kI;
    config.Slot0.kD = Constants.hoodConstants.kD;
    hoodMotor.getConfigurator().apply(config);
    supplyCurrent.setUpdateFrequency(50);
    positionSignal.setUpdateFrequency(50);  
  }

  public void setHoodPosition(double rotations) {
      hoodMotor.setControl(positionRequest.withPosition(rotations));
  }

  public void runStates(){
   switch (state){
      case Idle: 
         setHoodPosition(0);
         break;

      case Low:
         setHoodPosition(5.0);
         break;

      case High:
         setHoodPosition(15.0);
         break;
   }
  }

  public hoodState setState(hoodState newState){
     state = newState;
     return state;
  }

  public hoodState getState(){
     return state;
  }

  @Override
  public void periodic() {
     supplyCurrent.refresh();
     positionSignal.refresh();
     runStates(); 
  }
}