/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

/**
 *
 * @author digimo
 */
public class AutonomousDrive extends CommandBase {
    // setpoint is the number of encoder ticks to drive to
    double setpoint;
    boolean initrun = true;
    
    public AutonomousDrive() {
        requires(leftDriveMotor);
        requires(rightDriveMotor);
    }
    
    public AutonomousDrive(double setpoint) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(leftDriveMotor);
        requires(rightDriveMotor);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (initrun) {
            leftDriveMotor.getEncoder().reset();
            leftDriveMotor.getEncoder().start();
            rightDriveMotor.getEncoder().reset();
            rightDriveMotor.getEncoder().start();
            leftDriveMotor.enable();
            rightDriveMotor.enable();
            initrun = false;
        }
        leftDriveMotor.updateStatus();
        rightDriveMotor.updateStatus();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        double leftDiff = Math.abs(Math.abs(leftDriveMotor.getEncoder().get()) - setpoint);
        double rightDiff = Math.abs(Math.abs(rightDriveMotor.getEncoder().get()) - setpoint);
        // if either are within half an encoder turn
        if ((leftDiff < 500) || (rightDiff < 500)) {
            initrun = true;
            return true;
        } else {
            return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
        initrun = true;
        leftDriveMotor.off();
        rightDriveMotor.off();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        initrun = true;
        leftDriveMotor.off();
        rightDriveMotor.off();
    }
    
    public void setSetPoint (double setpoint) {
        this.setpoint = setpoint;
    }
}
