/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.RobotMap;
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
        bsuck = new JoystickButton (stick, 1);
        bspit = new JoystickButton (stick, 2);        
        
        System.out.println("TopRollerTeleop is executing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        yVal = stick.getY();
        tVal = stick.getThrottle();
        
        if ((yVal >= .150) || (yVal <= -.150)) {
            topRollerArm.setMotor(yVal);
        } else {
            topRollerArm.stop();
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
