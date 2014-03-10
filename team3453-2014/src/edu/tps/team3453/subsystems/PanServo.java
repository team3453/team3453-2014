/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.OI;
import edu.tps.team3453.RobotMap;
import edu.tps.team3453.RobotValues;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.tps.team3453.commands.PanServoDoNothing;

/**
 *
 * @author Madeline
 */
public class PanServo extends Subsystem {
    Servo servo = new Servo(2,RobotMap.panServoInput);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new PanServoDoNothing());
    }
    public void panServoDoNothing(){
        //servo.set(0);
    }
    public void panWhileOn(){
        servo.set(RobotValues.cameraPanServoOnPyramidAngle);
        SmartDashboard.putNumber("PanServo Degrees", servo.get());
    }
    public void panApproach(){
        servo.set(RobotValues.cameraPanServoPyramidApproachAngle);
        SmartDashboard.putNumber("PanServo Degrees", servo.get());
    }
    public void panServoClockwise(){
        //servo.set(1);
        servo.setAngle(servo.getAngle() + 1.0);
        SmartDashboard.putNumber("PanServo Degrees", servo.getAngle());
    }
    public void panServoCounterClockwise(){
        //servo.set(-1);
        servo.setAngle(servo.getAngle() - 1.0);
        SmartDashboard.putNumber("PanServo Degrees", servo.getAngle());
    }
    public double getJoystickValueX(){
        return OI.joystick.getX();
        
        }
}
