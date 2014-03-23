    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.commands;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.RobotValues;
import edu.tps.team3453.subsystems.Catapult;
import edu.tps.team3453.subsystems.TopRollerArm;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author digimo
 */
public class AutonomousMechanism extends CommandBase {

    private boolean isArmDone = false;
    private boolean isDone = false;
    private boolean requestFire = false;
    private boolean isArmIntake = false;
       
    public AutonomousMechanism() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(topRoller);
        requires(topRollerArm);
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
        isArmDone = false;
        isDone = false;
        requestFire = false;
        isArmIntake = false;
        
        // resolve conflict states
        topRollerArm.setCatapultState(catapult.getState());
        catapult.setRollerArmState(topRollerArm.setState());
        
        catapult.dispatch();
        topRollerArm.armDown();
        
        System.out.println("AutonomousMechanism is executing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        topRollerArm.setCatapultState(catapult.getState());
        catapult.setRollerArmState(topRollerArm.setState());
        
        // run the topRollerArm until we are onTarget
        if (topRollerArm.isEnabled() && topRollerArm.isOnTarget()) {
            System.out.println("AutonomousMechanism: TopRollerArm is onTarget at Down");
            topRollerArm.off();
            topRollerArm.setMotor(0.0);
            isArmDone = true;
        }  
        
/*        if (topRollerArm.setState().equals(TopRollerArm.State.kSafe)) {
            System.out.println("AutonomousMechanism: TopRollerArm is onTarget at Down");
            topRollerArm.off();
            topRollerArm.setMotor(0.0);
            isArmDone = true;              
        }
        */
        
        // now request the catapult to fire
        if ((isArmDone) && (!requestFire)) {
            System.out.println("AutonomousMechanism: Request Catapult Fire");

            catapult.requestFireState();
            if (catapult.getState().equals(Catapult.State.kFire)) {
                topRoller.setMotor(1.0);
                requestFire = true;
            }

        }
        
        // wait until the catapult arm is winched to the kReady state again
        if ((isArmDone) && (requestFire)) {
            if ((!isArmIntake) && (catapult.getState().equals(Catapult.State.kWinch))) {
                topRoller.setMotor(0.0);
//                isArmIntake = true;
//                topRollerArm.armIntake();
                
            }
            if (topRollerArm.isEnabled() && topRollerArm.isOnTarget()) {
                System.out.println("AutonomousMechanism: TopRollerArm is onTarget at InTake");
                topRollerArm.off();
                topRollerArm.setMotor(0.0);
                System.out.println("AutonomousMechanism: isDone");
                isDone = true;
            }
            if ((!isArmIntake) && (catapult.getState().equals(Catapult.State.kReady))) {
                System.out.println("AutonomousMechanism: TopRollerArm is going to InTake");                
                topRollerArm.armIntake();
                isArmIntake = true;
//                System.out.println("AutonomousMechanism: isDone");
//                isDone = true;
            }
        }

        // resolve conflict states
        topRollerArm.setCatapultState(catapult.getState());
        catapult.setRollerArmState(topRollerArm.setState());
        
        catapult.dispatch();
        
        this.dispStatus();
//        topRollerArm.updateStatus();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
        isArmDone = false;
        isDone = false;
        requestFire = false;
        isArmIntake = false;
        topRollerArm.off();
        topRoller.setMotor(0.0);
        topRollerArm.setMotor(0.0);
        catapult.initSubSystem();
        topRollerArm.initSubSystem();
        topRoller.stop();        
        System.out.println("AutonomousMechanism is finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        isArmDone = false;
        isDone = false;
        requestFire = false;
        isArmIntake = false;
        topRollerArm.off();
        topRoller.setMotor(0.0);
        topRollerArm.setMotor(0.0);
        catapult.initSubSystem();
        topRollerArm.initSubSystem();
        topRoller.stop();        
    }
    
    public void dispStatus() {
        String msg = "";
        msg += "Cat State: "+catapult.getState().value + ";            ";
        AutonomousMechanism.disp(1,msg);
        msg = "Cat output: " + catapult.getCurrentOutput() + ";           ";
        AutonomousMechanism.disp(2,msg);
        
        if (catapult.isCatapultRemoved()) {
            msg = "ACatapult Removed          ";
            AutonomousMechanism.disp(3, msg);
        } else {
            msg = "Autonomous Executing          ";
            AutonomousMechanism.disp(3, msg);
        }
        
        msg = "TopRollerArm Pos: "+topRollerArm.getArmPosition() + ";           ";
        AutonomousMechanism.disp(4,msg);
        msg = "output: " + topRollerArm.getCurrentOutput() + ";           ";
        AutonomousMechanism.disp(5,msg);

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
