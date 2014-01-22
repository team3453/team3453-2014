/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author admin
 */
public class TestLimitSwitch extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    //private static final DigitalInput limitSwitchOpen = new DigitalInput(RobotMap.limitSwitchCrashPreventer);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
        public boolean isOpen(){
        //System.out.println("Open Limit Switch: "+limitSwitchOpen.get());
        //return false;
        //return limitSwitchOpen.get();
            return false;
    }

}
