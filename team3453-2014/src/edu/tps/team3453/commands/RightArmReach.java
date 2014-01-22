/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

/**
 *
 * @author Madeline
 */
public class RightArmReach extends CommandBase {
    
    public RightArmReach() {
        requires(rightArm);
        requires(rightSolenoid);
        setTimeout(5.0);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        rightSolenoid.Unlock();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        rightArm.rightArmReach();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtended();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    private boolean isExtended() {
        if((rightArm.isExtended() ) || isTimedOut()) {
         return true;   
        } else{
            return false;
        }
    }
}
