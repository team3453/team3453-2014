/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.LeftSolenoidLock;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Madeline
 */
public class LeftSolenoid extends Subsystem {
    Relay leftSolenoid = new Relay(RobotMap.leftSolenoid);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new LeftSolenoidLock());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void Lock(){
        leftSolenoid.set(Relay.Value.kOff);
    }
    public void Unlock(){
        leftSolenoid.set(Relay.Value.kForward);
    }
}
