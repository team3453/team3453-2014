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
import edu.tps.team3453.commands.TiltServoDoNothing;

/**
 *
 * @author Madeline
 */
public class TiltServo extends Subsystem {
Servo servo = new Servo(RobotMap.tiltServoInput);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TiltServoDoNothing());
    }
    public void tiltServoDoNothing(){
        //servo.set(0);
        
    }
    public void tiltServoWhileOn(){
        servo.set(RobotValues.cameraTiltServoOnPyramidAngle);
        SmartDashboard.putNumber("TiltServo Degrees", servo.get());
    }
    public void tiltServoApproach(){
        servo.set(RobotValues.cameraTiltServoPyramidApproachAngle);
        SmartDashboard.putNumber("TiltServo Degrees", servo.get());
    }
    public void tiltServoClockwise(){
        servo.setAngle(servo.getAngle() + 1.0);
        SmartDashboard.putNumber("TiltServo Degrees", servo.getAngle());
    }
    public void tiltServoCounterClockwise(){
        servo.setAngle(servo.getAngle() - 1.0);
        SmartDashboard.putNumber("TiltServo Degrees", servo.getAngle());
    }
    public double getJoystickValueY(){
        return OI.joystick.getY();
        
        }
   
}
