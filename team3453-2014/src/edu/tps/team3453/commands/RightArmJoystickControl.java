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
public class RightArmJoystickControl extends CommandBase {
    
    private boolean isRetracting, isExtending;
    private double yVal;
    
    public RightArmJoystickControl() {
        requires(leftArm);
        requires(rightArm);
        requires(leftSolenoid);
        requires(rightSolenoid);
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
        yVal = OI.joystick2.getY();
        
        if(yVal >= 0.150){
            /*
            rightArm.setSetpoint(2000);
            rightSolenoid.Unlock();
            rightArm.enable();
            */
            isRetracting = true;
            isExtending = false;
            if(!isLimitPressed()) {
                if (yVal <= 0.3) {
                    rightArm.setPullMin();
                } else if (yVal <= 0.4) {
                    rightArm.setPullLow();
                } else if (yVal <= 0.5) {
                    rightArm.setPullMidLow();
                } else if (yVal <= 0.6) {
                    rightArm.setPullMid();
                } else if (yVal <= 0.7) {
                    rightArm.setPullMidHigh();
                } else if (yVal <= 0.8) {
                    rightArm.setPullHigh();
                } else {
                    rightArm.setPullMax();
                }                
                rightArm.rightArmPull();
            }
        }
        else if (yVal <= -0.150) {
            /*
            rightArm.setSetpoint(-2000);
            rightSolenoid.Unlock();
            rightArm.enable();
            */
            isRetracting = false;
            isExtending = true;
            if(!isLimitPressed()) {
                rightArm.rightArmReach();
            }
        }
        else{
            leftArm.disable();
            leftArm.stop();
            leftSolenoid.Lock();
            rightArm.disable();
            rightArm.stop();
            rightSolenoid.Lock();
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
            rightArm.disable();
            rightArm.stop();
            rightSolenoid.Lock();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
            isRetracting = false;
            isExtending = false;
            leftArm.disable();
            leftArm.stop();
            leftSolenoid.Lock();
            rightArm.disable();
            rightArm.stop();
            rightSolenoid.Lock();
    }
    public boolean isLimitPressed(){
        if (( rightArm.isRetracted()) && isRetracting){
           return true; 
        } else if(( rightArm.isExtended()) && isExtending) {
            return true;
        } else {
            return false;
        }
            
    }
}
