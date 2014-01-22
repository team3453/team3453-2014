/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.OI;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.tps.team3453.RobotMap;
import edu.tps.team3453.Utils;
import edu.tps.team3453.commands.RearWheelDoNothing;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author admin
 */
public class RearWheel extends PIDSubsystem {

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    
Relay rearWheel = new Relay(RobotMap.rearWheel);
private static final DigitalInput limitSwitchRearWheelExtend = new DigitalInput(2, RobotMap.limitSwitchRearWheelExtend);    
private static final DigitalInput limitSwitchRearWheelPolePosition = new DigitalInput(2, RobotMap.limitSwitchRearWheelPolePosition);
private static final DigitalInput interLimitSwitchOpen = new DigitalInput(RobotMap.limitSwitchCrashPreventer);    

    // Initialize your subsystem here
    public RearWheel() {
        super("RearWheel", Kp, Ki, Kd);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RearWheelDoNothing());
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
    
    public void extend(){
        rearWheel.set(Relay.Value.kReverse);
    }
    public void retract(){
        rearWheel.set(Relay.Value.kForward);
    }
    public void stop(){
        rearWheel.set(Relay.Value.kOff);
    }
    public boolean isExtended(){
        return (limitSwitchRearWheelExtend.get());
    }
    public boolean isOnPolePosition(){
        return limitSwitchRearWheelPolePosition.get();
    }
    public double getJoystickValue(){
        return OI.joystick2.getY();
        
        }
    
    public boolean isHit() {
        return interLimitSwitchOpen.get();
    }
    }

