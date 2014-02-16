/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.RobotValues;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author digimo
 */
public class TopRollerTeleop extends CommandBase {
    
    private Joystick stick;
    private Button bsuck;
    private Button bspit;
    private Button bSetPtAdvance;
    private Button bSetPtBackup;
    private double yVal;    
    private double tVal;
    private boolean rollerOn = false;
       
    public TopRollerTeleop() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(topRoller);
        requires(topRollerArm);
        requires(rightJoystickToken);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        stick = new Joystick(RobotMap.rightJoystick);
        bsuck = new JoystickButton (stick, RobotValues.bsuck);
        bspit = new JoystickButton (stick, RobotValues.bspit);  
        bSetPtAdvance = new JoystickButton (stick, RobotValues.bSetPtAdvance);
        bSetPtBackup = new JoystickButton (stick, RobotValues.bSetPtBackup);
        
        System.out.println("TopRollerTeleop is executing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        yVal = stick.getY();
        tVal = stick.getThrottle();
        
        boolean operatorControl = false;
        
        if ((yVal >= .150) || (yVal <= -.150)) {
            // multiply the joystick position by -1 to reverse 
            //  to fit motor power input
            // i.e. stick pull is positive, but means negative motor input
            
            if (topRollerArm.isEnabled()) {
                // if PID on topRollerArm is enabled, operator control
                //    on the joystick turns off the PID
                topRollerArm.off();
            }
            // reverse the joystick output to be the motor speed input
            //  i.e. joystick pushed away from the user = forward = negative value
            //  i.e. joystick pulled towards the user = backward = positive value
            
            // topRollerArm.setMotor is called with
            //   postive input into .setMotor moves motor forward
            //   negative input into .setMotor moves motor backward
            topRollerArm.setMotor( -1 * yVal);
            operatorControl = true;
        } else {
            if (!topRollerArm.isEnabled()) {
                // stop the topRollerArm when operator control is neutral
                //   only if topRollerArm PID is disabled
                topRollerArm.stop();
            }
        }
        
        if (!operatorControl) {
            // only allow topRollerArm PID if 
            //   not under operatorControl
            if (bSetPtAdvance.get() && bSetPtBackup.get()) {
                topRollerArm.off();
            } else {
                if (bSetPtAdvance.get()) {
                    topRollerArm.stop();
                    topRollerArm.PIDadvance();
                }
                if (bSetPtBackup.get()) {
                    topRollerArm.stop();
                    topRollerArm.PIDbackup();
                }
            }
        }
        
        if ((tVal >= .150) || (tVal <= -0.150)) {
            topRoller.setMotor(tVal);
        } else {
            topRoller.stop();
        }
    
        if ((bsuck.get() && bspit.get())) {
            topRoller.stop();
        } else {
            if (bsuck.get()) {
                topRoller.setMotor(1.0);
            }
            if (bspit.get()) {
                topRoller.setMotor(-1.0);
            }
        }
        
        topRollerArm.updateStatus();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("TopRollerTeleop is finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
