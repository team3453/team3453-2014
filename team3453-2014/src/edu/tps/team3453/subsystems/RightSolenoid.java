/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.RightSolenoidLock;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Madeline
 */
public class RightSolenoid extends Subsystem {
    Relay rightSolenoid = new Relay(RobotMap.rightSolenoid);
    //automatically in "out" position
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new RightSolenoidLock());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void Unlock(){
        rightSolenoid.set(Relay.Value.kForward);
    }
    public void Lock(){
        rightSolenoid.set(Relay.Value.kOff);
    }
}
