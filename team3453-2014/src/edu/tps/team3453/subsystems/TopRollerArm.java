/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.TopRollerArmDoNothing;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author digimo
 */
public class TopRollerArm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final SpeedController topRollerArm = new Victor(RobotMap.topRollerArmMotor);
    private static final DigitalInput limitSwitchTopRollerArmReach = new DigitalInput(RobotMap.limitSwitchTopRollerArmReach);
    private static final DigitalInput limitSwitchTopRollerArmPull = new DigitalInput(RobotMap.limitSwitchTopRollerArmPull);
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TopRollerArmDoNothing());
    }
    
    public void setMotor(double speed) {
        // should use limitswitch to stop the setting of the motor here

        if ((speed > 0) && (limitSwitchTopRollerArmPull.get())) {
            // pulling on the joystick and reached limit
            stop();
            
        } else if ((speed < 0) && (limitSwitchTopRollerArmReach.get())) {
            // pushing on the joystick and reached limit
            stop();
            
        } else {
            topRollerArm.set(speed);
        }
    }
    
    public void stop() {
        topRollerArm.set(0);
    }
}
