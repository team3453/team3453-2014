/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author admin
 */
public class RearWheelJoystickControl extends CommandBase {
    
    private boolean isForwards, isBackwards;
    
    public RearWheelJoystickControl() {
        // Use requires() here to declare subsystem dependencies
        requires(rearWheel);
        requires(rightJoystickToken);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        isForwards = false;
        isBackwards = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(rearWheel.getJoystickValue() <= -0.150){
           isForwards = true;
           isBackwards = false;
           if(!isLimitPressed()) {
               rearWheel.extend();
           }
        } else if (rearWheel.getJoystickValue() >= 0.150){
           isForwards = false;
           isBackwards = true;
           if(!isLimitPressed()) {
               rearWheel.retract();
           }
        } else {
           isForwards = false;
           isBackwards = false;
           rearWheel.stop();
        }
    
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isLimitPressed();
    }

    // Called once after isFinished returns true
    protected void end() {
           isForwards = false;
           isBackwards = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
           isForwards = false;
           isBackwards = false;
    }
    public boolean isLimitPressed(){
        if (rearWheel.isExtended() && isForwards) {
        return true;
    } else if(rearWheel.isHit() && isBackwards) {
        return true; 
    } else { 
        return false;
    }
}
}