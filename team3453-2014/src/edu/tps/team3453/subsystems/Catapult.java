/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.CatapultDoNothing;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author digimo
 */
public class Catapult extends Subsystem {

    private double currentOutput;
    
    private boolean stopped = true;
    private boolean latched = false;
    private boolean geatboxlatch = false;
    private double manualPower = 0.7;
       
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final SpeedController catapultMotor1 = new Victor(RobotMap.catapultMotor1);
    private static final SpeedController catapultMotor2 = new Victor(RobotMap.catapultMotor2);
    private static final DigitalInput limitSwitchCatapultLatch = new DigitalInput(RobotMap.limitSwitchCatapultLatch);
    private static final Relay solenoid = new Relay(RobotMap.catapultSolenoid);
    private static final Relay light = new Relay(RobotMap.lightSolenoid);
    
    // Initialize your subsystem here
    public Catapult() {
//        super("Catapult", Kp, Ki, Kd, Kf);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        //leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        
        //this.getPIDController().setTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        //setAbsoluteTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        solenoid.setDirection(Relay.Direction.kBoth);
        light.setDirection(Relay.Direction.kBoth);
    }    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new CatapultDoNothing());
    }
     
    public void setManualPower (double p) {
        manualPower = p * 0.7;
        if (manualPower < 0.2) {
            manualPower = 0.2;
        }
    }
    // speed input into setMotor
    //  postive value moves arm forward
    //  negative value moves arm backward
    public void setMotor(double speed) {
        
        System.out.println("Calling setMotor: "+speed);
        currentOutput = speed;
        
        // should use limitswitch to stop the setting of the motor here
        
        if ((speed != 0) && (isLatched())) {
            // pushing on the joystick and reached limit
            System.out.println("Hit Latch limit");
            
            stop();
            
        } else {
            catapultMotor1.set( -1 * manualPower * speed);
            catapultMotor2.set( -1 * manualPower * speed);
            currentOutput = speed * -1 * manualPower;
        }
        
//        updateStatus();
    }
    
    public boolean isLatched() {
        return (limitSwitchCatapultLatch.get());
    }
    
    public void stop() {
        System.out.println("Called stop");
        catapultMotor1.set(0);
        catapultMotor2.set(0);
        currentOutput = 0;
        setStopped(true);
    }
 
    public void setStopped(boolean k) {
        stopped = k;
    }
    
    public boolean isStopped() {
        return stopped;
    }

    public void updateStatus() {
        SmartDashboard.putNumber("Catapult currentOutput: ",currentOutput);
        System.out.println("Catapult currentOutput: "+currentOutput);
    }    
}
