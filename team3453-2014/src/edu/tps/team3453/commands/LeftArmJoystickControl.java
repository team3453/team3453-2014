/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.OI;

/**
 *
 * @author admin
 */
public class LeftArmJoystickControl extends CommandBase {
    
    private boolean isRetracting, isExtending;
    private double yVal;
    
    public LeftArmJoystickControl() {
        requires(leftArm);
        requires(leftSolenoid);
        requires(rightJoystickToken);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        isRetracting = false;
        isExtending = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(yVal >= 0.150){
            /*
            leftArm.setSetpoint(-2000);
            leftSolenoid.Unlock();
            leftArm.enable();
            */
            isRetracting = true;
            isExtending = false;
            if(!isLimitPressed()) {
                if (yVal <= 0.3) {
                    leftArm.setPullMin();
                } else if (yVal <= 0.4) {
                    leftArm.setPullLow();
                } else if (yVal <= 0.5) {
                    leftArm.setPullMidLow();
                } else if (yVal <= 0.6) {
                    leftArm.setPullMid();
                } else if (yVal <= 0.7) {
                    leftArm.setPullMidHigh();
                } else if (yVal <= 0.8) {
                    leftArm.setPullHigh();
                } else {
                    leftArm.setPullMax();
                }
                leftArm.leftArmPull();
            }
            
        }
        else if (yVal <= -0.150) {
            /*
            leftArm.setSetpoint(2000);
            leftSolenoid.Unlock();
            leftArm.enable();
            */
            isRetracting = false;
            isExtending = true;
            if(!isLimitPressed()) {
                leftArm.leftArmReach();
            }
        }
        else{
            leftArm.disable();
            leftArm.stop();
            leftSolenoid.Lock();
            isRetracting = false;
            isExtending = false;
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isLimitPressed();
    }

    // Called once after isFinished returns true
    protected void end() {
            isRetracting = false;
            isExtending = false;
            leftArm.disable();
            leftArm.stop();
            leftSolenoid.Lock();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
            isRetracting = false;
            isExtending = false;
            leftArm.disable();
            leftArm.stop();
            leftSolenoid.Lock();
    }
    public boolean isLimitPressed() {
        if ((leftArm.isRetracted()) && isRetracting){
           return true; 
        } else if((leftArm.isExtended()) && isExtending) {
            return true;
        } else {
            return false;
        }
            
    }
}
