/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.TopRollerDoNothing;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author digimo
 */
public class TopRoller extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    private static final SpeedController topRoller = new Victor(RobotMap.topRollerMotor);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TopRollerDoNothing());
    }
    
    public void setMotor(double speed) {
        topRoller.set(speed);
    }
    
    public void stop() {
        topRoller.set(0);
    }
}
