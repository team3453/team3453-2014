/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.RobotValues;
import edu.wpi.first.wpilibj.DriverStationLCD;
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
    private Button boverride;
    private Button bFire1;
    private Button bFire2;
    private double yVal;    
    private double tVal;
    private boolean rollerOn = false;
       
    public TopRollerTeleop() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(topRoller);
        requires(topRollerArm);
        requires(catapult);
        requires(rightJoystickToken);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        stick = new Joystick(RobotMap.rightJoystick);
        bsuck = new JoystickButton (stick, RobotValues.bsuck);
        bspit = new JoystickButton (stick, RobotValues.bspit); 
        boverride = new JoystickButton (stick, RobotValues.bArmCatapultOverRide);
        bFire1 = new JoystickButton (stick, RobotValues.bCatapultFire1);
        bFire2 = new JoystickButton (stick, RobotValues.bCatapultFire2);
        bSetPtAdvance = new JoystickButton (stick, RobotValues.bSetPtAdvance);
        bSetPtBackup = new JoystickButton (stick, RobotValues.bSetPtBackup);
        
        System.out.println("TopRollerTeleop is executing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        yVal = stick.getY();
        tVal = stick.getThrottle();
        
        boolean operatorControl = false;
        
        if (topRollerArm.isEnabled() && topRollerArm.isOnTarget()) {
            System.out.println("TopRollerArm is onTarget");
            topRollerArm.off();
        }

        // resolve conflict states
        topRollerArm.setCatapultState(catapult.getState());
        catapult.setRollerArmState(topRollerArm.setState());
        
        if (boverride.get()) {
            catapult.requestOverride();
            topRollerArm.setCatapultState(catapult.getState());
        }
        
        if (bFire1.get() || bFire2.get()) {
            catapult.requestFireState();
        }
        
        catapult.dispatch();
        
        // Throttle reading is -1 on fulltop and +1 on fullbottom
        //   have to invert and scale to 0-1 before sending to call
        topRollerArm.setManualPower(((-1*stick.getZ()) +1 ) /2);
        
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
            if (!topRollerArm.isStopped() && !topRollerArm.isEnabled()) {
                // stop the topRollerArm when operator control is neutral
                //   only if the motor is running and topRollerArm PID is disabled
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
            } else if (bspit.get()) {
                topRoller.setMotor(-1.0);
            } else {
//                topRoller.stop();
            }
        }
        
        this.dispStatus();
//        topRollerArm.updateStatus();
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

    public void dispStatus() {
        String msg = "";
        
        msg += "Cat State: "+catapult.getState().value + "; ";
        TopRollerTeleop.disp(1,msg);
        msg = "Cat output: "+ catapult.getCurrentOutput();
        TopRollerTeleop.disp(2,msg);
        
        if (catapult.isCatapultRemoved()) {
            msg = "TCatapult Removed";
            TopRollerTeleop.disp(3, msg);
        } else {
            msg = "Teleop Executing";
            TopRollerTeleop.disp(3, msg);
        }
        
        msg = "TopRollerArm Pos: "+topRollerArm.getArmPosition() + "; ";
        TopRollerTeleop.disp(4,msg);
        msg = "output: " + topRollerArm.getCurrentOutput() + "; ";
        TopRollerTeleop.disp(5,msg);

    }      
    
    protected void lcd_catapult (String msg) {
        TopRollerTeleop.disp(1,msg);
    }
    
    protected void lcd_topRollerArm (String msg) {
        TopRollerTeleop.disp(3,msg);
    }
    
    public static void disp(int line, String msg) {
        DriverStationLCD.Line l;
        switch (line)
        {
        case 1:
        l = DriverStationLCD.Line.kUser2;
        break;
        case 2:
        l = DriverStationLCD.Line.kUser3;
        break;
        case 3:
        l = DriverStationLCD.Line.kUser4;
        break;
        case 4:
        l = DriverStationLCD.Line.kUser5;
        break;
        case 5:
        l = DriverStationLCD.Line.kUser6;
        break;
        case 6:
        l = DriverStationLCD.Line.kMain6;
        break;
        default:
        l = DriverStationLCD.Line.kUser2;
        break;
        }

        DriverStationLCD.getInstance().println(l, 1, msg);
        DriverStationLCD.getInstance().updateLCD();
        
    }
}
