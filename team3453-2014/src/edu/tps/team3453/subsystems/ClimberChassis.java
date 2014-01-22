/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.OI;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.tps.team3453.RobotMap;
import edu.tps.team3453.Utils;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.tps.team3453.commands.ClimberChassisDoNothing;
import edu.wpi.first.wpilibj.DigitalInput;
import java.lang.Boolean;
/**
 *
 * @author admin
 */
public class ClimberChassis extends PIDSubsystem {

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    
    Relay climberChassis = new Relay(RobotMap.climberChassis);
    //private static final DigitalInput limitSwitchExtend = new DigitalInput(RobotMap.climberChassisExtendLimitSwitch);
    private static final DigitalInput limitSwitchRetract = new DigitalInput(RobotMap.climberChassisRetractLimitSwitch);
    //private static final DigitalInput interLimitSwitchOpen = new DigitalInput(RobotMap.limitSwitchCrashPreventer);    

    // Initialize your subsystem here
    public ClimberChassis() {
        super("ClimberChassis", Kp, Ki, Kd);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ClimberChassisDoNothing());       
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return 0.0;
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    }
    
        public void Forward(){
        climberChassis.set(Relay.Value.kReverse);
    }
    public void Back(){
        climberChassis.set(Relay.Value.kForward);
    }
    public void Stop(){
        climberChassis.set(Relay.Value.kOff);
    }
    /*
    public boolean isExtended() {
        return (limitSwitchExtend.get());
    }
    */
    public boolean isRetracted() {
        return limitSwitchRetract.get();
    }
    public double getAxisValue(){
        return OI.joystick2.getY();
    }
}
