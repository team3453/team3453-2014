/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

/**
 *
 * @author Madeline
 */
public class DumperArmForward extends CommandBase {
    
    public DumperArmForward() {
        requires(dumperArm);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        setTimeout(5.0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    dumperArm.Down();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isEnded();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private boolean isEnded() {
        if(dumperArm.isUp() || isTimedOut()) {
            return true;
        } else {
            return false;
        }
    }
}
