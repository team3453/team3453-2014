/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.RobotMap;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author Madeline
 */
public class DriveTeleop extends CommandBase {
    SpeedController leftMotor;
    SpeedController leftRearMotor;
    SpeedController rightMotor;
    SpeedController rightRearMotor;
    private RobotDrive drive;
    private RobotDrive forwardDrive;
    private RobotDrive reverseDrive;
    private static boolean driveForward = true;
    private static boolean driveChanged = false;
    private static boolean driveSensitiveTrimmed = false; 
    Joystick stick;
    private Button b1;
    
    public DriveTeleop() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(leftDriveMotor);
        requires(rightDriveMotor);
        requires(leftJoystickToken);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        leftMotor = leftDriveMotor.getMotor();
        leftRearMotor = leftDriveMotor.getRearMotor();
        rightMotor = rightDriveMotor.getMotor();
        rightRearMotor = rightDriveMotor.getRearMotor();
        forwardDrive = new RobotDrive(leftMotor, leftRearMotor, rightMotor, rightRearMotor);
        reverseDrive = new RobotDrive(rightMotor, rightRearMotor, leftMotor, leftRearMotor);
        drive = forwardDrive;
        stick = new Joystick(RobotMap.leftJoystick);
        b1 = new JoystickButton (stick, 1);
        
        System.out.println("DriveTeleop is executing");
    }

    public static void setForwardDrive() {
        driveChanged = true;
        driveForward = true;
    }
    
    public static void setReverseDrive() {
        driveChanged = true;
        driveForward = false;
    }
    
    protected void driveMotorsStop() {
        leftDriveMotor.off();
        rightDriveMotor.off();
    }
    
    public static void setDriveSensitiveTrimmed(boolean s) {
        driveSensitiveTrimmed = s;
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (driveChanged) {
            driveChanged = false;
            driveMotorsStop();
        }

        if (b1.get()) {
            // if the trigger is squeezed, drive in reverse
            if (driveForward) {
                // stop the motors briefly if we were driving forward previously
                driveMotorsStop();
            }
            drive = reverseDrive;
            driveForward = false;
        } else {
            if (!driveForward) {
                // stop the motors briefly if we were driving in reverse previously
                driveMotorsStop();
            }
            drive = forwardDrive;
            driveForward = true;
        }
        drive.arcadeDrive(stick,driveSensitiveTrimmed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("DriveTeleop is finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
