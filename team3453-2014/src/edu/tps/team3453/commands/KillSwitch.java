/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.OI;

/**
 *
 * @author digimo
 */
public class KillSwitch extends CommandBase {
    
    public KillSwitch() {
        // Use requires() here to declare subsystem dependencies
       requires(leftJoystickToken);
       requires(rightJoystickToken);
       requires(climberChassis);
       requires(leftArm);
       requires(rightArm);
       requires(leftDriveMotor);
       requires(rightDriveMotor);
       requires(leftSolenoid);
       requires(rightSolenoid);
       requires(lid);
       requires(rearWheel);
       requires(dumperArm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if ((OI.joystick.getRawButton(2) && OI.joystick.getRawButton(3)) ||
               (OI.joystick2.getRawButton(2) && OI.joystick2.getRawButton(3))) {
            
            climberChassis.Stop();
            rearWheel.stop();

            leftArm.stop();
            rightArm.stop();
            //leftArm.disable();
            //rightArm.disable();
            //leftSolenoid.Lock();
            //rightSolenoid.Lock();

            leftDriveMotor.off();
            leftDriveMotor.disable();
            rightDriveMotor.off();
            rightDriveMotor.disable();

            lid.Stop();
            dumperArm.Stop();
            
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
     protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

