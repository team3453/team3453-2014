/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Relay;
import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.LidStop;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.tps.team3453.Utils;

/**
 *
 * @author Madeline
 */
public class Lid extends Subsystem {
    //private static final SpeedController lidMotor = new Victor(RobotMap.lid);
    private static final Relay lidMotor = new Relay(RobotMap.lid);
    private static final DigitalInput limitSwitchOpen = new DigitalInput(RobotMap.openLidLimitSwitch);
    private static final DigitalInput limitSwitchClose = new DigitalInput(RobotMap.closeLidLimitSwitch);
    //limit switch x2 
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new LidStop());
    }
    public void Open(){
        //lidMotor.set(1);
        lidMotor.set(Relay.Value.kForward);
    }
    public void Close(){
        lidMotor.set(Relay.Value.kReverse);
        //lidMotor.set(-1);
    }
    public void Stop() {
        lidMotor.set(Relay.Value.kOff);
        //lidMotor.set(0.0);
    }
    public boolean isOpen(){
        System.out.println("Open Limit Switch: "+limitSwitchOpen.get());
        //return false;
        return limitSwitchOpen.get();
    }
    public boolean isClosed() {
        System.out.println("Close Limit Switch: "+limitSwitchClose.get());
        //return false;
        return limitSwitchClose.get(); 
        //replace with return switch.get();
    }
}
