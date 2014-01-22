/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

/**
 *
 * @author Madeline
 */
public class ArmPull extends CommandBase {
    
    public ArmPull() {
        requires(leftArm);
        requires(rightArm);
        requires(rightSolenoid);
        requires(leftSolenoid);
        setTimeout(5.0);
        // add timeout
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        rightSolenoid.Unlock();
        leftSolenoid.Unlock();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        leftArm.leftArmPull();
        rightArm.rightArmPull();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isRetracted();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private boolean isRetracted() {
        if((leftArm.isRetracted() && rightArm.isRetracted()) || isTimedOut()) {
            return true;
        } else {
            return false;
        }
    }
}
